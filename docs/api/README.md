# Asos API

The contract is [`openapi.yaml`](openapi.yaml) (OpenAPI 3.1). The backend and the Android client (`core:network`) both follow it.

## Rules

1. **Contract first.** Change `openapi.yaml` in its own PR before touching DTOs (`core/network/.../dto`) or Retrofit interfaces (`core/network/.../api`).
2. **Public vs private.** Endpoints without 🔒 work without logging in: the Constitution text, topics, dictionary, search, cases, the community feed, lawyers and the hotline. 🔒 endpoints need `Authorization: Bearer <token>`. `AuthInterceptor` adds it.
3. **Screens never call the API directly.** Screen → ViewModel → (use case) → Repository (`core:data`) → `*Api` (`core:network`). Swap in `FakeAsosApi` / `Fake*Repository` while the backend isn't ready.
4. **Base URL:** `/api/v1`. Send `Accept-Language: uz-Latn`. Errors use the `Error` schema with an Uzbek `message` you can show to the user.
5. **Paging:** `?page=1` (default page size 20). Chat messages use a `before` cursor.

## Endpoints shared by several screens

These are the endpoints used across screens. Build and test them first.

| Endpoint | Screens |
|---|---|
| `DELETE /me/bookmarks/{type}/{id}` | Profil, Modda, Bosh sahifa, Kazus, Muhokama, Lug‘at |
| `GET /articles/{number}` | Modda, Bosh sahifa, Chat, Kazus, Test, Muhokama |
| `PUT /me/bookmarks/{type}/{id}` | Modda, Bosh sahifa, Kazus, Muhokama, Lug‘at |
| `GET /me/bookmarks` | Profil, Konstitutsiya, Hamjamiyat, Lug‘at |
| `GET /search` | Konstitutsiya, Bosh sahifa, Hamjamiyat, Lug‘at |
| `POST /posts/{postId}/vote` | Bosh sahifa, Muhokama, Hamjamiyat |
| `GET /learning/progress` | Profil, O‘rganish |
| `GET /me` | Profil, Bosh sahifa |
| `GET /me/stats` | Profil, O‘rganish |
| `GET /me/achievements` | Profil, O‘rganish |
| `GET /me/progress/articles` | Profil, Konstitutsiya |
| `GET /terms/{termId}` | Modda, Lug‘at |
| `GET /topics` | Bosh sahifa, O‘rganish |
| `POST /quizzes/{quizId}/answers` | Bosh sahifa, Test |
| `GET /cases/featured` | Bosh sahifa, Kazuslar |
| `POST /assistant/messages` | Bosh sahifa, Huquqiy yordam |
| `GET /me/continue` | Bosh sahifa, O‘rganish |
| `GET /lawyers/{lawyerId}` | Chat, Huquqiy yordam |
| `POST /chats` | Muhokama, Huquqiy yordam |

## Endpoints per screen

### Kirish  ·  `:feature:auth`

- `POST /auth/otp/request`
- `POST /auth/otp/verify`
- `POST /auth/oneid`
- `POST /auth/google`

### Bosh sahifa  ·  `:feature:home`

- `GET /articles/{number}`
- `GET /articles/daily`
- `GET /topics`
- `GET /search`
- `GET /quizzes/daily`
- `POST /quizzes/{quizId}/answers` 🔒
- `GET /cases/featured`
- `GET /posts/highlights`
- `POST /posts/{postId}/vote` 🔒
- `POST /assistant/messages` 🔒
- `GET /me` 🔒
- `GET /me/continue` 🔒
- `PUT /me/bookmarks/{type}/{id}` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒
- `GET /home`

### Konstitutsiya  ·  `:feature:constitution`

- `GET /constitution`
- `GET /constitution/sections`
- `GET /constitution/chapters/{chapterId}/articles`
- `GET /topics/{topicId}/articles`
- `GET /search`
- `GET /me/progress/articles` 🔒
- `GET /me/bookmarks` 🔒

### Modda  ·  `:feature:constitution`

- `GET /articles/{number}`
- `GET /articles/{number}/related`
- `GET /terms/{termId}`
- `PUT /me/progress/articles/{number}` 🔒
- `PUT /me/bookmarks/{type}/{id}` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒

### Lug‘at  ·  `:feature:dictionary`

- `GET /search`
- `GET /terms`
- `GET /terms/daily`
- `GET /terms/{termId}`
- `GET /me/bookmarks` 🔒
- `PUT /me/bookmarks/{type}/{id}` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒

### O‘rganish  ·  `:feature:learn`

- `GET /topics`
- `GET /quizzes/topics`
- `GET /learning/progress` 🔒
- `GET /me/stats` 🔒
- `GET /me/achievements` 🔒
- `GET /me/continue` 🔒

### Test  ·  `:feature:learn`

- `GET /articles/{number}`
- `GET /quizzes/{quizId}`
- `POST /quizzes/{quizId}/answers` 🔒

### Kazuslar  ·  `:feature:cases`

- `GET /cases`
- `GET /cases/featured`

### Kazus  ·  `:feature:cases`

- `GET /articles/{number}`
- `GET /cases/{caseId}`
- `POST /cases/{caseId}/solutions` 🔒
- `PUT /me/bookmarks/{type}/{id}` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒

### Hamjamiyat  ·  `:feature:community`

- `GET /search`
- `GET /communities`
- `GET /posts`
- `POST /posts` 🔒
- `POST /posts/{postId}/vote` 🔒
- `GET /me/bookmarks` 🔒

### Muhokama  ·  `:feature:community`

- `GET /articles/{number}`
- `GET /posts/{postId}`
- `GET /posts/{postId}/replies`
- `POST /posts/{postId}/replies` 🔒
- `POST /posts/{postId}/vote` 🔒
- `POST /replies/{replyId}/vote` 🔒
- `POST /chats` 🔒
- `PUT /me/bookmarks/{type}/{id}` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒

### Huquqiy yordam  ·  `:feature:legalhelp`

- `POST /assistant/messages` 🔒
- `GET /legal-help/hotline`
- `GET /lawyers`
- `GET /lawyers/{lawyerId}`
- `POST /lawyers/{lawyerId}/bookings` 🔒
- `GET /chats` 🔒
- `POST /chats` 🔒

### Chat  ·  `:feature:legalhelp`

- `GET /articles/{number}`
- `GET /lawyers/{lawyerId}`
- `GET /chats/{chatId}` 🔒
- `GET /chats/{chatId}/messages` 🔒
- `POST /chats/{chatId}/messages` 🔒

### Profil  ·  `:feature:profile`

- `POST /auth/logout` 🔒
- `GET /learning/progress` 🔒
- `GET /me` 🔒
- `PATCH /me` 🔒
- `GET /me/stats` 🔒
- `GET /me/activity` 🔒
- `GET /me/achievements` 🔒
- `GET /me/progress/articles` 🔒
- `GET /me/bookmarks` 🔒
- `DELETE /me/bookmarks/{type}/{id}` 🔒
- `PATCH /me/settings` 🔒

## Open questions for the backend team

- Final domain, and staging vs production URLs (currently placeholders `api.asos.uz`).
- OneID integration details (redirect flow vs SDK).
- Which AI model and provider the assistant uses, and how answers cite articles.
- Real hotline number (the design uses the placeholder `+998 (71) 000-00-00`).
- File upload endpoint for chat attachments (`fileId` in `sendChatMessage`), e.g. `POST /files`.
- Real-time chat transport (WebSocket vs polling).
