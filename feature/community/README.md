# :feature:community · Hamjamiyat

**Work package:** WP-09. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Hamjamiyat | `Hamjamiyat.dc.html` | `feed/CommunityFeedScreen.kt` | `community?articleNumber=` |
| Muhokama | `Post.dc.html` | `post/PostDetailScreen.kt` | `post/{postId}` |

Hamjamiyat is a tab, so it has no back button and the app module draws the bottom bar.
`CommunityRoute(articleNumber)` narrows the feed to one article, which is how the Modda
reader links into it.

## Folder layout

```
community/
├── navigation/   public entry point: the only file the app module uses
├── feed/     CommunityFeedScreen · ViewModel · UiState · Action · components/
├── post/     PostDetailScreen · ViewModel · UiState · Action · components/
├── ui/       CommunityFormat: "2 soat", "2,4 ming a'zo"
```

`feed/components/VoteColumn.kt` is local because `core:designsystem/VoteControl.kt` is still
a placeholder; move it to core when that lands.

## API endpoints used (through `core:data`'s CommunityRepository)

- `GET /v1/communities` — the topic chips
- `GET /v1/posts` (`community`, `sort`, `q`, `article`, paging) — the feed
- `GET /v1/posts/{id}` — one discussion with its comment tree
- `POST /v1/posts` — "Savol berish"
- `POST /v1/posts/{id}/vote`, `POST /v1/comments/{id}/vote`
- `POST /v1/posts/{id}/comments` — replies
- `POST` / `DELETE /v1/communities/{slug}/join`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

Votes move the arrow and the count before the server answers, and roll back on failure.
Comments nest one level: the API lifts anything deeper to the root.

## Not built

- Saving a discussion and "Ulashish" on the Muhokama action bar: no endpoint is wired yet,
  so saving shows a notice instead of pretending.
- The composer avatar shows initials only once `GET /v1/me/profile` has been read elsewhere;
  guests get a generic icon.

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] Guests are asked to sign in instead of hitting a 401
- [x] ViewModel unit tests pass
- [x] Previews exist for each screen
- [x] Only depends on `core:*` modules
