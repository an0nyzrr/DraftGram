<p align="center">
  <img src="docs/logo.png" width="120" alt="Draftgram logo" />
</p>

<h1 align="center">Draftgram</h1>

<p align="center">
  Telegram для Android с дополнениями: режим призрака, сохранение сообщений,
  свои настройки интерфейса и экспорт чатов.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-6.0%2B-3DDC84?logo=android&logoColor=white" alt="Android 6.0+" />
  <img src="https://img.shields.io/badge/language-Java-ED8B00" alt="Java" />
  <img src="https://img.shields.io/badge/license-GPL--2.0-blue" alt="License" />
  <img src="https://img.shields.io/badge/status-in%20development-orange" alt="Status" />
</p>

---

## Что внутри

Всё, что добавлено поверх Telegram, живёт в разделе **«Настройки Draftgram»**, который стоит первым в настройках.

### Ghost Mode
Скрывает, что вы в сети и откуда вы что-то переслали.

| Опция | Что делает |
|---|---|
| Не отправлять онлайн | Не шлёт статус «в сети» на сервер |
| Скрывать имя при пересылке | Пересылка без ссылки на автора (`drop_author`) |
| Скрывать «via» | Не показывает подпись «via @bot» у сообщений |
| Скрывать сторис | Не отправляет отметки о прочтении и просмотры |
| Скрывать спонсорские каналы | Не запрашивает спонсорские сообщения |
| Скрывать эмодзи-статус в списке чатов | В планах |

### Сохранение сообщений
Удалённые и изменённые сообщения сохраняются в локальную базу `draftgram_archive.db`.
Удаление сохраняется; сохранение изменений и входящих удалений пока не подключено.

### Профиль
- **ID** пользователя и **DC** (дата-центр) выводятся в профиле, если включены в настройках.

### Интерфейс
- Переключатель **Liquid Glass** (сохраняется, визуальная часть в работе).
- Основной стиль проекта — **Material 3 Expressive**. Перевод всего приложения на него в работе.

### В разработке
- Локальный прокси (`tg-ws-proxy`). Настройка сохраняется, подключение пока не реализовано.
- Material 3 Expressive по всему приложению.
- Экспорт чатов в HTML, автоформатирование, счётчик символов, голосовые и кружочки, локальные звёзды и подарки.

## Сборка

Проект — форк [Telegram-Android](https://github.com/TelegramMessenger/Telegram-Android), поэтому нужны сабмодули.

```bash
git clone --recurse-submodules https://github.com/an0nyzrr/DraftGram.git
cd DraftGram
./gradlew :TMessagesProj_App:assembleAfatDebug -PdgAbi=arm64-v8a
```

Требования: JDK 17, Android SDK 36, NDK `27.2.12479018`.

`-PdgAbi` ограничивает ABI для быстрой сборки. По умолчанию собираются все четыре.

### Свои API-ключи
Upstream-ключ `api_id` Telegram часто блокируется при сборке. Получите свой на [my.telegram.org](https://my.telegram.org) и передайте его в CI через секреты `TG_APP_ID` и `TG_APP_HASH`. Workflow подставляет их в `BuildVars.java`.

### Push-уведомления
`google-services.json` привязан к пакету приложения. При смене пакета нужен новый файл, иначе сборка и FCM не заработают.

### CI
Workflow `.github/workflows/build.yml` собирает debug APK для arm64 и выкладывает его артефактом `draftgram-apk` (хранится 14 дней).

## Структура

| Путь | Что там |
|---|---|
| `TMessagesProj/src/main/java/org/telegram/ui/DraftgramSettingsActivity.java` | Экран «Настройки Draftgram» |
| `TMessagesProj/src/main/java/org/telegram/utils/settings/` | Переключатели Draftgram (`DraftgramSettings`) |
| `TMessagesProj/src/main/java/org/telegram/messenger/DraftgramMessageArchive.java` | Архив удалённых сообщений |
| `TMessagesProj/src/main/res/raw/draftgram_sticker.json` | Анимированный стикер на интро |

## Лицензия

Код основан на Telegram-Android и распространяется по лицензии из файла [LICENSE](LICENSE).
Оригинальный проект: [TelegramMessenger/Telegram-Android](https://github.com/TelegramMessenger/Telegram-Android).

Автор дополнений: [@xedazu](https://t.me/xedazu).
