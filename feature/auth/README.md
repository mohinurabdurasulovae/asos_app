# :feature:auth · Kirish (Login)

**Work package:** WP-03. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Kirish | `Kirish.dc.html` | `login/LoginScreen.kt` | `login` |

## Folder layout

```
auth/
├── navigation/        public entry point: the only file the app module uses
├── login/  LoginScreen · LoginViewModel · LoginUiState · LoginAction · components/
```

## API endpoints used (through `core:data` repositories)

- `POST /auth/google`
- `POST /auth/oneid`
- `POST /auth/otp/request`
- `POST /auth/otp/verify`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
