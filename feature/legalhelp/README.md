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

## API endpoints used (through `core:data` repositories)

- `GET /chats/{chatId}`
- `GET /chats/{chatId}/messages`
- `GET /lawyers`
- `GET /legal-help/hotline`
- `POST /assistant/messages`
- `POST /chats`
- `POST /chats/{chatId}/messages`
- `POST /lawyers/{lawyerId}/bookings`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
      (for now: a temporary in-memory repository in `data/temp/`, until WP-02 lands)
- [ ] ViewModel unit tests pass
- [x] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
