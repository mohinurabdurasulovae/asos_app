# :feature:cases · Kazuslar + Kazus (Real-life cases)

**Work package:** WP-08. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Kazuslar | `Kazuslar.dc.html` | `list/CasesScreen.kt` | `cases` |
| Kazus — yechim | `Kazus.dc.html` | `detail/CaseDetailScreen.kt` | `case/{caseId}` |

## Folder layout

```
cases/
├── navigation/        public entry point: the only file the app module uses
├── list/  CasesScreen · CasesViewModel · CasesUiState · CasesAction · components/
├── detail/  CaseDetailScreen · CaseDetailViewModel · CaseDetailUiState · CaseDetailAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /cases`
- `GET /cases/featured`
- `GET /cases/{caseId}`
- `POST /cases/{caseId}/solutions`
- `PUT /me/bookmarks/{type}/{id}`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
