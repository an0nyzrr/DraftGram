// Java · DraftgramSettingsActivity.java · Android (Telegram-Android fork) · screen "Настройки Draftgram"
package org.telegram.ui;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.BuildVars;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.utils.settings.BooleanSetting;
import org.telegram.utils.settings.DraftgramSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DraftgramSettingsActivity extends UniversalFragment {

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

    private static final int ID_SHOW_USER_ID = 500;
    private static final int ID_SHOW_DATA_CENTER = 501;

    private static final int ID_ABOUT_VERSION = 600;
    private static final int ID_ABOUT_CREATOR = 601;

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

    @Override
    public View createView(Context context) {
        fragmentView = super.createView(context);
        listView.setSections();
        listView.setSectionsDrawBackground(true);
        return fragmentView;
    }

    @Override
    protected CharSequence getTitle() {
        return "Настройки Draftgram";
    }

    @Override
    protected RecyclerListView getListViewForSimpleGlass() {
        return listView;
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
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

        items.add(UItem.asHeader("Сохранение сообщений"));
        items.add(UItem.asCheck(ID_KEEP_DELETED, "Сохранять удалённые").setChecked(DraftgramSettings.keepDeleted.get()));
        items.add(UItem.asCheck(ID_KEEP_EDITED, "Сохранять изменённые").setChecked(DraftgramSettings.keepEdited.get()));
        items.add(UItem.asCheck(ID_KEEP_BOTS_CHANNELS, "Сохранять от ботов и каналов").setChecked(DraftgramSettings.keepFromBotsChannels.get()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("Интерфейс"));
        items.add(UItem.asCheck(ID_LIQUID_GLASS, "Liquid Glass").setChecked(DraftgramSettings.liquidGlass.get()));
        items.add(UItem.asShadow("Основной стиль — Material 3 Expressive. Liquid Glass включается вместо него. Применение темы — отдельный шаг."));

        items.add(UItem.asHeader("В профиле"));
        items.add(UItem.asCheck(ID_SHOW_USER_ID, "Показывать ID пользователя").setChecked(DraftgramSettings.showUserId.get()));
        items.add(UItem.asCheck(ID_SHOW_DATA_CENTER, "Показывать дата-центр").setChecked(DraftgramSettings.showDataCenter.get()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader("О нас"));
        items.add(UItem.asButton(ID_ABOUT_VERSION, "Версия", BuildVars.BUILD_VERSION_STRING));
        items.add(UItem.asButton(ID_ABOUT_CREATOR, "Создатель", "@xedazu"));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == ID_ABOUT_CREATOR) {
            Browser.openUrl(getContext(), "https://t.me/xedazu");
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
