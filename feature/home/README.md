# :feature:home · Bosh sahifa (Home)

**Work package:** WP-04. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Bosh sahifa | `Main.dc.html` | `HomeScreen.kt` | `home` |

## Folder layout

```
home/
├── navigation/        public entry point: the only file the app module uses
├── (root)/  HomeScreen · HomeViewModel · HomeUiState · HomeAction · components/
├── data/      HomeRepository + its Retrofit calls, models and Hilt module
```

`data/` keeps the home feed calls inside this module so the feature needs no `core:*` changes.
When `core:data` gets a HomeRepository, move `data/` there and switch the ViewModel to it.

## API endpoints used (through `data/HomeRepository`)

- `GET /v1/home` (greeting, article of the day, topics, continue, case, daily question, posts)
- `POST /v1/daily-question/answer`
- `POST /v1/me/bookmarks` (save the article of the day)

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
