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
```

## API endpoints used (through `core:data` repositories)

- `GET /articles/daily`
- `GET /home`
- `GET /me/continue`
- `GET /posts/highlights`
- `GET /quizzes/daily`
- `GET /topics`
- `PUT /me/bookmarks/{type}/{id}`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
