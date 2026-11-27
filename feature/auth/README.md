# :feature:auth · Kirish (Log in)

**Work package:** WP-03. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Kirish | `Kirish.dc.html` | `login/LoginScreen.kt` | `login` |
| Ro‘yxatdan o‘tish | (no artboard, built in the Kirish style) | `register/RegisterScreen.kt` | `register` |

## Two ways in

Phone numbers and OneID were removed. Sign-in is:

1. **Google** — one button at the top.
2. **Email code** — the user types an address, the server mails a 6-digit code from
   **support@prteam.uz**, and the code screen warns that it may land in Spam or Promotions.

The artboard still shows the phone field; the screens follow this flow instead.

## Folder layout

```
auth/
├── navigation/   public entry point: the only file the app module uses
├── login/     LoginScreen · LoginViewModel · LoginUiState · LoginAction · components/
├── register/  RegisterScreen · RegisterViewModel · RegisterUiState · RegisterAction · components/
├── common/    shared by both screens: OtpState, OtpController (email → code flow) · components/
├── data/      EmailAuthRepository + its Retrofit calls, models and Hilt module (see below)
```

`data/` keeps the sign-in calls inside this module so the feature needs no `core:*` changes.
A successful verify saves the token in `core:datastore`'s `TokenStore`, so
`AuthRepository.isSignedIn` (core:data) turns true everywhere.

## API endpoints used (through `data/EmailAuthRepository`)

- `POST /v1/auth/request-code` — `{ "email": "…" }`, mails the code
- `POST /v1/auth/verify` — `{ "email": "…", "code": "123456" }`, creates the account on first sign-in
- `PATCH /v1/me` — saves the name after registration
- `POST /v1/auth/oauth` — `{ "provider": "google", "subject": "…" }`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Server side

Live since 27-noyabr 2026. `POST /v1/auth/request-code` and `POST /v1/auth/verify` take
`email` only — the phone flow was removed from the server too, so the two sides match.

Server files: `lib/mail.php` (validation, mail, the code flow) and `routes/auth.php`.
The code is mailed with PHP `mail()` from **support@prteam.uz**; `auth_codes` is keyed by
email, and `lib/mail.php` migrates that table itself on first use.

## Blocked on Google

The Google button needs the Google Sign-In SDK and a client id, neither of which is in the
project. `LoginViewModel.OnGoogleClick` raises `LoginEvent.GoogleUnavailable` and the screen
says so. `EmailAuthRepository.signInWithProvider(GOOGLE, subject, …)` is already written, so
wiring it up is: add the dependency, get the id token, pass its `subject`.

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] The code screen warns about the spam folder and names the sender
- [x] ViewModel unit tests pass
- [x] Previews exist for each screen and component
- [x] Only depends on `core:*` modules
- [x] The backend accepts `email` on request-code / verify
- [ ] Google Sign-In SDK and client id
- [ ] SPF / DKIM / DMARC on prteam.uz so the code does not land in Spam
