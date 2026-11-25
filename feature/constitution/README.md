# :feature:constitution · Konstitutsiya + Modda (Constitution browser & article reader)

**Work package:** WP-05. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Konstitutsiya | `Konstitutsiya.dc.html` | `browse/ConstitutionScreen.kt` | `ConstitutionRoute` |
| Modda (50-modda) | `Modda.dc.html` | `article/ArticleScreen.kt` | `ArticleRoute(key)` · deep link `asos://article/{key}` |

## Folder layout

```
constitution/
├── navigation/        public entry point: the only file the app module uses
├── browse/  ConstitutionScreen · ConstitutionViewModel · ConstitutionUiState · ConstitutionAction · components/
├── article/  ArticleScreen · ArticleViewModel · ArticleUiState · ArticleAction · components/
```

## API endpoints used (through `core:data` repositories)

| Endpoint | Token | Used for |
|---|---|---|
| `GET /v1/constitution` | optional | Section / chapter tree, chapter progress |
| `GET /v1/topics` | — | Topic chips, Mavzular tab |
| `GET /v1/chapters/{roman}` | optional | Articles of an opened chapter |
| `GET /v1/articles?topic=&q=&section=&status=saved` | optional | Topic list, search, section VI, Saqlangan |
| `GET /v1/me/progress` | required | "Oxirgi o‘qilgan" card |
| `GET /v1/articles/{key}` | optional | The whole reader (key = `50` or `muqaddima`) |
| `POST /v1/articles/{key}/progress` | required | `reading` on open, `read` at the end (+10 ball once) |
| `POST /v1/me/bookmarks` · `DELETE /v1/me/bookmarks/article/{id}` | required | Save / unsave |

Guests can read everything; progress, the continue card and bookmarks appear after sign-in.

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [x] ViewModel unit tests (`./gradlew :feature:constitution:testDebugUnitTest`)
- [x] Previews exist for each screen and component
- [x] Only depends on `core:*` modules

## Behaviour notes

- **Konstitutsiya:** section II opens by default (or the section you are reading). Tapping a
  chapter lists its articles underneath. Section VI has no chapters, so it lists articles 154–155
  directly. Search waits 300 ms after typing stops.
- **Modda:** the first three parts show, then "To‘liq matnni ochish". Reaching the end after at
  least 5 seconds marks the article as read (once). "Aa" changes the official text size
  (17 / 19 / 21 / 24 sp) and is remembered on the device. Previous / next replaces the reader
  instead of stacking screens.
- Interface text shows `ʻ` as `‘` (the UI font has no `ʻ`); the official text is untouched.
