# :feature:legalhelp · Huquqiy yordam + Chat (Legal help)

**Work package:** WP-10. Owner: Charos Hakimova

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Huquqiy yordam | `Yordam.dc.html` | `help/LegalHelpScreen.kt` | `legalHelp` |
| Advokat bilan chat | `Chat.dc.html` | `chat/ChatScreen.kt` | `chat/{chatId}` |

## Folder layout

```
legalhelp/
├── navigation/        public entry point: the only file the app module uses
├── help/  LegalHelpScreen · LegalHelpViewModel · LegalHelpUiState · LegalHelpAction · components/
├── chat/  ChatScreen · ChatViewModel · ChatUiState · ChatAction · components/
```

## API endpoints used (through `core:data` LegalHelpRepository)

| Endpoint | Used for | Needs sign-in |
|---|---|---|
| `GET /v1/help` | the whole Huquqiy yordam screen | no |
| `POST /v1/ai/ask` | Asos AI yordamchi | yes |
| `POST /v1/lawyers/{id}/conversations` | "Yozish" → opens the chat | yes |
| `GET /v1/conversations/{id}` | chat messages (re-read every 8 s while open) | yes |
| `POST /v1/conversations/{id}/messages` | send a message | yes |
| `POST /v1/conversations/{id}/read` | mark the lawyer's messages read | yes |
| `POST /v1/callback-requests` | "Menga qo‘ng‘iroq" (asks for a phone when signed out) | no |
| `POST /v1/bookings` | "Vaqt band qilish" (lawyer's next free slot) | yes |

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml). Without sign-in (`:feature:auth`
isn't built yet) the screen still loads; signed-in actions show "Kirish" → `LoginRoute`.

Not in the API yet, so they show "Tez kunda": voice input, phone/video calls with a lawyer,
file upload in chat.

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [x] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
