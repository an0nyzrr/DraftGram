// Java · DraftgramSettingsActivity.java · Android (Telegram-Android fork) · screen "Настройки Draftgram"
package org.telegram.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.DraftgramAccountCard;
import org.telegram.ui.Components.DraftgramShapes;
import org.telegram.ui.Components.IconBackgroundColors;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.utils.settings.BooleanSetting;
import org.telegram.utils.settings.DraftgramSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DraftgramSettingsActivity extends UniversalFragment {

    private static final int SECTION_ROOT = 0;
    private static final int SECTION_MAIN = 1;
    private static final int SECTION_UI = 2;
    private static final int SECTION_CHATS = 3;
    private static final int SECTION_OTHER = 4;

    private static final int ID_CAT_MAIN = 700;
    private static final int ID_CAT_UI = 701;
    private static final int ID_CAT_CHATS = 702;
    private static final int ID_CAT_OTHER = 703;

    private static final int ID_LINK_SOURCE = 800;
    private static final int ID_LINK_CREATOR = 801;

    private static final int ID_PROXY = 100;

    private static final int ID_GHOST_ONLINE = 200;
    private static final int ID_GHOST_FORWARD_NAME = 201;
    private static final int ID_GHOST_VIA = 202;
    private static final int ID_GHOST_STORIES = 203;
    private static final int ID_GHOST_SPONSORED = 204;
    private static final int ID_GHOST_EMOJI_STATUS = 205;

    private static final int ID_KEEP_DELETED = 300;
    private static final int ID_KEEP_EDITED = 301;
    private static final int ID_KEEP_BOTS_CHANNELS = 302;

    private static final int ID_LIQUID_GLASS = 400;
    private static final int ID_AVATAR_SHAPE = 410;
    private static final int ID_ACCENT = 411;

    private static final int ID_SHOW_USER_ID = 500;
    private static final int ID_SHOW_DATA_CENTER = 501;

    private static final int ID_ABOUT_VERSION = 600;
    private static final int ID_ABOUT_CREATOR = 601;

    private static final String SOURCE_URL = "https://github.com/an0nyzrr/DraftGram";
    private static final String CREATOR_URL = "https://t.me/xedazu";

    private static final Map<Integer, BooleanSetting> toggles = new HashMap<>();

    static {
        toggles.put(ID_PROXY, DraftgramSettings.proxyEnabled);
        toggles.put(ID_GHOST_ONLINE, DraftgramSettings.ghostHideOnline);
        toggles.put(ID_GHOST_FORWARD_NAME, DraftgramSettings.ghostHideForwardName);
        toggles.put(ID_GHOST_VIA, DraftgramSettings.ghostHideVia);
        toggles.put(ID_GHOST_STORIES, DraftgramSettings.ghostHideStories);
        toggles.put(ID_GHOST_SPONSORED, DraftgramSettings.ghostHideSponsored);
        toggles.put(ID_GHOST_EMOJI_STATUS, DraftgramSettings.ghostHideEmojiStatus);
        toggles.put(ID_KEEP_DELETED, DraftgramSettings.keepDeleted);
        toggles.put(ID_KEEP_EDITED, DraftgramSettings.keepEdited);
        toggles.put(ID_KEEP_BOTS_CHANNELS, DraftgramSettings.keepFromBotsChannels);
        toggles.put(ID_LIQUID_GLASS, DraftgramSettings.liquidGlass);
        toggles.put(ID_SHOW_USER_ID, DraftgramSettings.showUserId);
        toggles.put(ID_SHOW_DATA_CENTER, DraftgramSettings.showDataCenter);
    }

    private final int section;
    private View headerView;

    public DraftgramSettingsActivity() {
        this(SECTION_ROOT);
    }

    public DraftgramSettingsActivity(int section) {
        this.section = section;
    }

    @Override
    public View createView(Context context) {
        fragmentView = super.createView(context);
        listView.setSections();
        listView.setSectionsDrawBackground(true);
        return fragmentView;
    }

    @Override
    protected CharSequence getTitle() {
        switch (section) {
            case SECTION_MAIN: return "Основные";
            case SECTION_UI: return "Оформление";
            case SECTION_CHATS: return "Чаты";
            case SECTION_OTHER: return "Другое";
            default: return "Настройки Draftgram";
        }
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asSpace(ActionBar.getCurrentActionBarHeight()));
        switch (section) {
            case SECTION_MAIN: fillMain(items); break;
            case SECTION_UI: fillUi(items); break;
            case SECTION_CHATS: fillChats(items); break;
            case SECTION_OTHER: fillOther(items); break;
            default: fillRoot(items); break;
        }
    }

    private void fillRoot(ArrayList<UItem> items) {
        if (headerView == null) {
            headerView = buildHeader(getContext());
        }
        items.add(UItem.asCustom(headerView));

        items.add(UItem.asHeader("Категории"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_CAT_MAIN, IconBackgroundColors.BLUE.top, IconBackgroundColors.BLUE.bottom, R.drawable.settings_power, "Основные", "Прокси и Ghost Mode"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_CAT_UI, IconBackgroundColors.ORANGE.top, IconBackgroundColors.ORANGE.bottom, R.drawable.settings_features, "Оформление", "Аватарка, акцент, Liquid Glass"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_CAT_CHATS, IconBackgroundColors.GREEN.top, IconBackgroundColors.GREEN.bottom, R.drawable.settings_chat, "Чаты", "Сохранение удалённых и изменённых"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_CAT_OTHER, 0xFF7C5CFA, 0xFF5B3FE0, R.drawable.settings_data, "Другое", "Профиль и о приложении"));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Ссылки"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_LINK_SOURCE, IconBackgroundColors.GREEN.top, IconBackgroundColors.GREEN.bottom, R.drawable.settings_faq, "Исходный код", "github.com/an0nyzrr/DraftGram"));
        items.add(SettingsActivity.SettingCell.Factory.of(ID_LINK_CREATOR, IconBackgroundColors.BLUE.top, IconBackgroundColors.BLUE.bottom, R.drawable.settings_ask, "Создатель", "@xedazu"));
        items.add(UItem.asShadow(null));
    }

    private View buildAccountCard(boolean premium) {
        TLRPC.User user = UserConfig.getInstance(currentAccount).getCurrentUser();
        if (user == null) {
            TextView empty = new TextView(getContext());
            empty.setText("Войдите в аккаунт, чтобы увидеть превью");
            empty.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
            empty.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(12), AndroidUtilities.dp(16), AndroidUtilities.dp(12));
            return empty;
        }
        return new DraftgramAccountCard(getContext(), currentAccount, user, premium, accentColor(), avatarShape());
    }

    private void applyDraftgramVisualChange() {
        Theme.reloadAllResources(getContext());
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetNewTheme, false);
    }

    private int avatarShape() {
        return clamp(DraftgramSettings.avatarShape.get(), DraftgramShapes.shapeCount());
    }

    private int accentIndex() {
        return clamp(DraftgramSettings.accent.get(), DraftgramShapes.accentCount());
    }

    private int accentColor() {
        return DraftgramShapes.ACCENT_COLORS[accentIndex()];
    }

    private static int clamp(int value, int count) {
        return value >= 0 && value < count ? value : 0;
    }

    private void fillMain(ArrayList<UItem> items) {
        items.add(UItem.asHeader("Прокси"));
        items.add(UItem.asCheck(ID_PROXY, "Локальный прокси (tg-ws-proxy)").setChecked(DraftgramSettings.proxyEnabled.get()));
        items.add(UItem.asShadow("Пока сохраняет настройку. Подключение прокси добавляется отдельным шагом."));

        items.add(UItem.asHeader("Ghost Mode"));
        items.add(UItem.asCheck(ID_GHOST_ONLINE, "Не отправлять онлайн").setChecked(DraftgramSettings.ghostHideOnline.get()));
        items.add(UItem.asCheck(ID_GHOST_FORWARD_NAME, "Скрывать имя при пересылке").setChecked(DraftgramSettings.ghostHideForwardName.get()));
        items.add(UItem.asCheck(ID_GHOST_VIA, "Скрывать «via»").setChecked(DraftgramSettings.ghostHideVia.get()));
        items.add(UItem.asCheck(ID_GHOST_STORIES, "Скрывать сторис").setChecked(DraftgramSettings.ghostHideStories.get()));
        items.add(UItem.asCheck(ID_GHOST_SPONSORED, "Скрывать спонсорские каналы").setChecked(DraftgramSettings.ghostHideSponsored.get()));
        items.add(UItem.asCheck(ID_GHOST_EMOJI_STATUS, "Скрывать эмодзи-статус в списке чатов").setChecked(DraftgramSettings.ghostHideEmojiStatus.get()));
        items.add(UItem.asShadow(null));
    }

    private void fillUi(ArrayList<UItem> items) {
        items.add(UItem.asHeader("Аккаунт"));
        items.add(UItem.asButton(ID_AVATAR_SHAPE, "Фигура аватарки", DraftgramShapes.SHAPE_NAMES[avatarShape()]));
        items.add(UItem.asButton(ID_ACCENT, "Акцентный цвет", DraftgramShapes.ACCENT_NAMES[accentIndex()]));
        items.add(UItem.asShadow("Фигура пока применяется к превью аккаунта. Во всех аватарках клиента и акцент на фон — следующие шаги."));

        items.add(UItem.asHeader("Интерфейс"));
        items.add(UItem.asCheck(ID_LIQUID_GLASS, "Liquid Glass").setChecked(DraftgramSettings.liquidGlass.get()));
        items.add(UItem.asShadow("Основной стиль — Material 3 Expressive. Liquid Glass включается вместо него. Применение темы — отдельный шаг."));
    }

    private void fillChats(ArrayList<UItem> items) {
        items.add(UItem.asHeader("Сохранение сообщений"));
        items.add(UItem.asCheck(ID_KEEP_DELETED, "Сохранять удалённые").setChecked(DraftgramSettings.keepDeleted.get()));
        items.add(UItem.asCheck(ID_KEEP_EDITED, "Сохранять изменённые").setChecked(DraftgramSettings.keepEdited.get()));
        items.add(UItem.asCheck(ID_KEEP_BOTS_CHANNELS, "Сохранять от ботов и каналов").setChecked(DraftgramSettings.keepFromBotsChannels.get()));
        items.add(UItem.asShadow(null));
    }

    private void fillOther(ArrayList<UItem> items) {
        items.add(UItem.asHeader("В профиле"));
        items.add(UItem.asCheck(ID_SHOW_USER_ID, "Показывать ID пользователя").setChecked(DraftgramSettings.showUserId.get()));
        items.add(UItem.asCheck(ID_SHOW_DATA_CENTER, "Показывать дата-центр").setChecked(DraftgramSettings.showDataCenter.get()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("О нас"));
        items.add(UItem.asButton(ID_ABOUT_VERSION, "Версия", BuildVars.BUILD_VERSION_STRING));
        items.add(UItem.asButton(ID_ABOUT_CREATOR, "Создатель", "@xedazu"));
    }

    private View buildHeader(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setPadding(AndroidUtilities.dp(16), AndroidUtilities.dp(24), AndroidUtilities.dp(16), AndroidUtilities.dp(16));

        ImageView logo = new ImageView(context);
        logo.setImageResource(Theme.isCurrentThemeDark() ? R.drawable.dg_logo_light : R.drawable.dg_logo_dark);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        layout.addView(logo, new LinearLayout.LayoutParams(AndroidUtilities.dp(96), AndroidUtilities.dp(96)));

        TextView title = new TextView(context);
        title.setText("Draftgram");
        title.setTextSize(22);
        title.setTypeface(AndroidUtilities.bold());
        title.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = AndroidUtilities.dp(12);
        layout.addView(title, titleParams);

        TextView description = new TextView(context);
        description.setText("Telegram с дополнениями: режим призрака, сохранение сообщений и свои настройки.");
        description.setTextSize(14);
        description.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        description.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParams.topMargin = AndroidUtilities.dp(6);
        layout.addView(description, descParams);

        return layout;
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id >= ID_CAT_MAIN && item.id <= ID_CAT_OTHER) {
            presentFragment(new DraftgramSettingsActivity(item.id - ID_CAT_MAIN + 1));
            return;
        }
        if (item.id == ID_LINK_SOURCE) {
            Browser.openUrl(getContext(), SOURCE_URL);
            return;
        }
        if (item.id == ID_LINK_CREATOR || item.id == ID_ABOUT_CREATOR) {
            Browser.openUrl(getContext(), CREATOR_URL);
            return;
        }
        if (item.id == ID_AVATAR_SHAPE) {
            DraftgramSettings.avatarShape.set((avatarShape() + 1) % DraftgramShapes.shapeCount());
            applyDraftgramVisualChange();
            listView.adapter.update(true);
            return;
        }
        if (item.id == ID_ACCENT) {
            DraftgramSettings.accent.set((accentIndex() + 1) % DraftgramShapes.accentCount());
            applyDraftgramVisualChange();
            listView.adapter.update(true);
            return;
        }

        final BooleanSetting setting = toggles.get(item.id);
        if (setting == null) {
            return;
        }
        setting.toggle();
        listView.adapter.update(true);
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }
}
