# :feature:profile · Profil (Profile & settings)

**Work package:** WP-11. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Profil | `Profil.dc.html` | `ProfileScreen.kt` | `profile` |

## Folder layout

```
profile/
├── navigation/        public entry point: the only file the app module uses
├── (root)/  ProfileScreen · ProfileViewModel · ProfileUiState · ProfileAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /me`
- `GET /me/achievements`
- `GET /me/activity`
- `GET /me/bookmarks`
- `GET /me/stats`
- `PATCH /me/settings`
- `POST /auth/logout`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
