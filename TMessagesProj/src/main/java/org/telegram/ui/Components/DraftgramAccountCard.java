// Java · DraftgramAccountCard.java · Android (Telegram-Android fork) · account preview card for Draftgram settings

package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;

public class DraftgramAccountCard extends LinearLayout {

    private final RectF rect = new RectF();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final boolean premium;
    private final int accentColor;
    private final float radius;

    public DraftgramAccountCard(Context context, int currentAccount, TLRPC.User user, boolean premium, int accentColor, int shape) {
        super(context);
        this.premium = premium;
        this.accentColor = accentColor;
        this.radius = AndroidUtilities.dp(28);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16), AndroidUtilities.dp(16));

        DraftgramAvatarView avatar = new DraftgramAvatarView(context);
        avatar.setShape(shape);
        avatar.setForUserOrChat(user, new org.telegram.ui.Components.AvatarDrawable(user));
        addView(avatar, LayoutHelper.createLinear(88, 88, Gravity.CENTER_VERTICAL, 0, 0, 16, 0));

        LinearLayout texts = new LinearLayout(context);
        texts.setOrientation(VERTICAL);

        TextView name = new TextView(context);
        name.setText(UserObject.getUserName(user));
        name.setTextSize(20);
        name.setTypeface(AndroidUtilities.bold());
        name.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        name.setSingleLine(true);
        name.setEllipsize(TextUtils.TruncateAt.END);
        texts.addView(name);

        StringBuilder line = new StringBuilder();
        if (!TextUtils.isEmpty(user.phone)) {
            line.append(PhoneFormat.getInstance().format("+" + user.phone));
        }
        String username = UserObject.getPublicUsername(user);
        if (!TextUtils.isEmpty(username)) {
            if (line.length() > 0) line.append(" · ");
            line.append('@').append(username);
        }
        TextView contacts = new TextView(context);
        contacts.setText(line.toString());
        contacts.setTextSize(14);
        contacts.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        contacts.setSingleLine(true);
        contacts.setEllipsize(TextUtils.TruncateAt.END);
        texts.addView(contacts);

        TLRPC.UserFull full = MessagesController.getInstance(currentAccount).getUserFull(user.id);
        if (full != null && !TextUtils.isEmpty(full.about)) {
            TextView bio = new TextView(context);
            bio.setText(full.about);
            bio.setTextSize(13);
            bio.setMaxLines(3);
            bio.setEllipsize(TextUtils.TruncateAt.END);
            bio.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            texts.addView(bio);
        }
        addView(texts, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rect.set(0, 0, w, h);
        if (premium) {
            int top = accentColor & 0x00FFFFFF | 0x66000000;
            int bottom = Theme.getColor(Theme.key_windowBackgroundGray);
            paint.setShader(new LinearGradient(0, 0, w, h, top, bottom, Shader.TileMode.CLAMP));
        } else {
            paint.setShader(null);
            paint.setColor(Theme.getColor(Theme.key_windowBackgroundGray));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(rect, radius, radius, paint);
        super.onDraw(canvas);
    }
}
