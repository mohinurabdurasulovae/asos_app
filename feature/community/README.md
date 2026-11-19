# :feature:community · Hamjamiyat + Muhokama (Community Q&A)

**Work package:** WP-09. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Hamjamiyat | `Hamjamiyat.dc.html` | `feed/CommunityFeedScreen.kt` | `community` |
| Muhokama | `Post.dc.html` | `post/PostDetailScreen.kt` | `post/{postId}` |

## Folder layout

```
community/
├── navigation/        public entry point: the only file the app module uses
├── feed/  CommunityFeedScreen · CommunityFeedViewModel · CommunityFeedUiState · CommunityFeedAction · components/
├── post/  PostDetailScreen · PostDetailViewModel · PostDetailUiState · PostDetailAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /communities`
- `GET /posts`
- `GET /posts/{postId}`
- `GET /posts/{postId}/replies`
- `POST /posts`
- `POST /posts/{postId}/replies`
- `POST /posts/{postId}/vote`
- `POST /replies/{replyId}/vote`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
