# :feature:dictionary · Konstitutsiyaviy lug'at

**Work package:** WP-06. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Konstitutsiyaviy lug'at | `Lugat.dc.html` | `DictionaryScreen.kt` | `dictionary?termSlug=` |

`DictionaryRoute(termSlug)` with a slug opens that term's sheet straight away, so other
screens can link to one term ("Lug'atda" chips on the Modda reader).

## Folder layout

```
dictionary/
├── navigation/   public entry point: the only file the app module uses
├── (root)/   DictionaryScreen · DictionaryViewModel · DictionaryUiState · DictionaryAction
├── components/   DictionarySearchField · SavedTermsFilter · TermOfTheDayCard ·
│                 TermListItem · AlphabetIndex · TermDetailSheet
```

## API endpoints used (through `core:data`'s DictionaryRepository)

- `GET /v1/terms` (`letter`, `q`, `saved=1`, paging) — the A–Z list plus the alphabet rail
- `GET /v1/terms/of-the-day` — the «Kun atamasi» card
- `GET /v1/terms/{slug}` — the term sheet
- `POST` / `DELETE /v1/me/bookmarks` — saving a term

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

The list endpoint sends `articles` as bare numbers while the detail endpoint sends full
objects, so `TermSummary` carries `articleNumbers` and `TermDetail` carries `articles`.

## Not built

- The "Tinglash" (text-to-speech) button on the design's term card: the project has no TTS
  yet, so the button is left out rather than shipped dead.
- Saving needs a signed-in user; as a guest the API answers 401 and the row shows the error.

## Done when

- [x] Screen matches the design artboard (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] Search is debounced, so typing does not hammer the API
- [x] ViewModel unit tests pass
- [x] Previews exist for the screen
- [x] Only depends on `core:*` modules
