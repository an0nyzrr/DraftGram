// Java · DraftgramSettings.java · Android (Telegram-Android fork) · storage for "Настройки Draftgram"
package org.telegram.utils.settings;

public final class DraftgramSettings {

    // Прокси (tg-ws-proxy). Пока только хранит флаг, подключение — следующий шаг.
    public static final BooleanSetting proxyEnabled = BooleanSetting.of("dg_proxy_enabled", false);

    // Ghost Mode
    public static final BooleanSetting ghostHideOnline = BooleanSetting.of("dg_ghost_hide_online", false);
    public static final BooleanSetting ghostHideForwardName = BooleanSetting.of("dg_ghost_hide_forward_name", false);
    public static final BooleanSetting ghostHideVia = BooleanSetting.of("dg_ghost_hide_via", false);
    public static final BooleanSetting ghostHideStories = BooleanSetting.of("dg_ghost_hide_stories", false);
    public static final BooleanSetting ghostHideSponsored = BooleanSetting.of("dg_ghost_hide_sponsored", false);
    public static final BooleanSetting ghostHideEmojiStatus = BooleanSetting.of("dg_ghost_hide_emoji_status", false);

    // Сохранение сообщений
    public static final BooleanSetting keepDeleted = BooleanSetting.of("dg_keep_deleted", false);
    public static final BooleanSetting keepEdited = BooleanSetting.of("dg_keep_edited", false);
    public static final BooleanSetting keepFromBotsChannels = BooleanSetting.of("dg_keep_from_bots_channels", false);

    // Интерфейс. Основной стиль — Material 3 Expressive, Liquid Glass включается отдельно.
    public static final BooleanSetting liquidGlass = BooleanSetting.of("dg_liquid_glass", false);

    // Показ в профиле
    public static final BooleanSetting showUserId = BooleanSetting.of("dg_show_user_id", false);
    public static final BooleanSetting showDataCenter = BooleanSetting.of("dg_show_data_center", false);

    // Внешний вид аккаунта: фигура аватарки и акцентный цвет (индексы в DraftgramShapes)
    public static final IntSetting avatarShape = IntSetting.of("dg_avatar_shape", 0);
    public static final IntSetting accent = IntSetting.of("dg_accent", 0);

    private DraftgramSettings() {
    }
}
