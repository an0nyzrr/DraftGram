// Java · DraftgramMessageArchive.java · Android (Telegram-Android fork) · local archive of deleted and edited messages
package org.telegram.messenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLRPC;
import org.telegram.utils.settings.DraftgramSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps copies of messages in a separate SQLite file, outside Telegram's own database,
 * so the archive survives the server-side deletion and is never touched by sync.
 */
public final class DraftgramMessageArchive {

    public static final int KIND_DELETED = 1;
    public static final int KIND_EDITED = 2;

    private static final String DB_NAME = "draftgram_archive.db";

    private static SQLiteDatabase database;

    private DraftgramMessageArchive() {
    }

    public static final class Record {
        public final long dialogId;
        public final int messageId;
        public final int kind;
        public final long savedAt;
        public final TLRPC.Message message;

        private Record(long dialogId, int messageId, int kind, long savedAt, TLRPC.Message message) {
            this.dialogId = dialogId;
            this.messageId = messageId;
            this.kind = kind;
            this.savedAt = savedAt;
            this.message = message;
        }
    }

    /**
     * Call on the main thread before the request to delete messages is sent.
     * Reads the current copies from the local storage, so it must not run on the storage queue.
     */
    public static void archiveBeforeDelete(int account, long dialogId, List<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return;
        }
        final boolean keepAll = DraftgramSettings.keepDeleted.get();
        final boolean keepBotsChannels = DraftgramSettings.keepFromBotsChannels.get();
        if (!keepAll && !keepBotsChannels) {
            return;
        }
        for (int messageId : messageIds) {
            final TLRPC.Message message = MessagesStorage.getInstance(account).getMessage(dialogId, messageId);
            if (message == null) {
                continue;
            }
            if (keepAll || (keepBotsChannels && isFromBotOrChannel(account, message))) {
                store(KIND_DELETED, account, dialogId, message);
            }
        }
    }

    /** Call with the version of the message that is about to be replaced by an edit. */
    public static void archiveBeforeEdit(int account, long dialogId, TLRPC.Message previous) {
        if (previous == null || !DraftgramSettings.keepEdited.get()) {
            return;
        }
        store(KIND_EDITED, account, dialogId, previous);
    }

    /** All archived records of one kind for the account, newest first. Blocking, call off the main thread. */
    public static List<Record> load(int account, int kind) {
        final List<Record> result = new ArrayList<>();
        final SQLiteDatabase db = db();
        if (db == null) {
            return result;
        }
        try (Cursor cursor = db.rawQuery(
            "SELECT dialog_id, mid, saved_at, payload FROM archived_messages WHERE account = ? AND kind = ? ORDER BY saved_at DESC",
            new String[]{String.valueOf(account), String.valueOf(kind)})) {
            while (cursor.moveToNext()) {
                final byte[] payload = cursor.getBlob(3);
                final TLRPC.Message message = deserialize(payload);
                if (message != null) {
                    result.add(new Record(cursor.getLong(0), cursor.getInt(1), kind, cursor.getLong(2), message));
                }
            }
        }
        return result;
    }

    private static void store(int kind, int account, long dialogId, TLRPC.Message message) {
        final byte[] payload = serialize(message);
        final SQLiteDatabase db = db();
        if (payload == null || db == null) {
            return;
        }
        final ContentValues values = new ContentValues();
        values.put("account", account);
        values.put("dialog_id", dialogId);
        values.put("mid", message.id);
        values.put("kind", kind);
        values.put("saved_at", System.currentTimeMillis());
        values.put("payload", payload);
        db.insert("archived_messages", null, values);
    }

    private static boolean isFromBotOrChannel(int account, TLRPC.Message message) {
        if (message.post || message.from_id instanceof TLRPC.TL_peerChannel) {
            return true;
        }
        if (message.from_id instanceof TLRPC.TL_peerUser) {
            final TLRPC.User user = MessagesController.getInstance(account).getUser(((TLRPC.TL_peerUser) message.from_id).user_id);
            return user != null && user.bot;
        }
        return false;
    }

    private static byte[] serialize(TLRPC.Message message) {
        try {
            final SerializedData data = new SerializedData();
            message.serializeToStream(data);
            return data.toByteArray();
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private static TLRPC.Message deserialize(byte[] payload) {
        if (payload == null) {
            return null;
        }
        try {
            final SerializedData data = new SerializedData(payload);
            final int constructor = data.readInt32(false);
            return TLRPC.Message.TLdeserialize(data, constructor, false);
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private static synchronized SQLiteDatabase db() {
        if (database == null) {
            final Context context = ApplicationLoader.applicationContext;
            if (context == null) {
                return null;
            }
            database = new Helper(context).getWritableDatabase();
        }
        return database;
    }

    private static final class Helper extends SQLiteOpenHelper {
        Helper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS archived_messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "account INTEGER NOT NULL, " +
                "dialog_id INTEGER NOT NULL, " +
                "mid INTEGER NOT NULL, " +
                "kind INTEGER NOT NULL, " +
                "saved_at INTEGER NOT NULL, " +
                "payload BLOB NOT NULL)");
            db.execSQL("CREATE INDEX IF NOT EXISTS archived_by_account_kind ON archived_messages(account, kind)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
