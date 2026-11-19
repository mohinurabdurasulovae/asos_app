# :feature:dictionary · Lug‘at (Legal dictionary)

**Work package:** WP-06. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Konstitutsiyaviy lug‘at | `Lugat.dc.html` | `DictionaryScreen.kt` | `dictionary` |

## Folder layout

```
dictionary/
├── navigation/        public entry point: the only file the app module uses
├── (root)/  DictionaryScreen · DictionaryViewModel · DictionaryUiState · DictionaryAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /terms`
- `GET /terms/daily`
- `GET /terms/{id}`
- `PUT /me/bookmarks/{type}/{id}`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
