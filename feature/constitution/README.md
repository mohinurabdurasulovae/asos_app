# :feature:constitution · Konstitutsiya + Modda (Constitution browser & article reader)

**Work package:** WP-05. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Konstitutsiya | `Konstitutsiya.dc.html` | `browse/ConstitutionScreen.kt` | `constitution` |
| Modda (50-modda) | `Modda.dc.html` | `article/ArticleScreen.kt` | `article/{number}` |

## Folder layout

```
constitution/
├── navigation/        public entry point: the only file the app module uses
├── browse/  ConstitutionScreen · ConstitutionViewModel · ConstitutionUiState · ConstitutionAction · components/
├── article/  ArticleScreen · ArticleViewModel · ArticleUiState · ArticleAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /articles/{number}`
- `GET /articles/{number}/related`
- `GET /constitution`
- `GET /constitution/chapters/{chapterId}/articles`
- `GET /constitution/sections`
- `GET /me/progress/articles`
- `GET /search`
- `PUT /me/bookmarks/{type}/{id}`
- `PUT /me/progress/articles/{number}`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
