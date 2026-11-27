# :feature:cases · Kazuslar (Cases)

**Work package:** WP-08. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Kazuslar | `Kazuslar.dc.html` | `list/CasesScreen.kt` | `cases` |
| Kazus — yechim | `Kazus.dc.html` | `detail/CaseDetailScreen.kt` | `case/{caseId}` |

Ported from the standalone Kazuslar prototype (one 2 000-line `MainActivity.kt`). The two
screens were split into the structure below; the prototype's own `MainActivity`,
`MaterialTheme` wrapper and bottom navigation bar were dropped, because the app module owns
the tab bar and Kazuslar is not a tab — it is reached from O'rganish and has a back button.

The prototype's `AsosColors` / `AsosFonts` objects were replaced by `core:designsystem`
tokens (`AsosColors`, `AsosTextStyles`, `AsosShapes`, `AsosSpacing`, `AsosTones`) and its
Material icons by `AsosIcons`, so the section no longer carries its own design system.

## Folder layout

```
cases/
├── navigation/   public entry point: the only file the app module uses
├── list/     CasesScreen · CasesViewModel · CasesUiState · CasesAction · components/
├── detail/   CaseDetailScreen · CaseDetailViewModel · CaseDetailUiState · CaseDetailAction · components/
├── ui/       shared by both screens: topic tones, difficulty bars
├── data/     the cases, the progress store and the Hilt module (see below)
```

`data/` keeps the cases inside this module, as the prototype did, so the section needs no
`core:*` changes. `CasesSampleData` holds the five cases and the six Constitution articles
they offer, word for word from the prototype.

## What is still a stand-in

`data/InMemoryCasesRepository` serves `CasesSampleData` and keeps progress in memory, so a
solved case is remembered while the app runs but not across restarts. The endpoints below
already exist in the API contract; when they land, replace that one class and nothing above
`data/CasesRepository` changes.

- `GET /v1/cases`, `GET /v1/cases/of-the-week`
- `GET /v1/cases/{key}`
- `PUT /v1/cases/{key}/draft`
- `POST /v1/cases/{key}/check`
- `POST /v1/cases/{key}/reset`
- `POST /v1/me/bookmarks` (save a case)

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml). Checking an answer currently
compares the picked articles with the case's correct set, which is the prototype's rule; the
backend takes that over with `/check`.

## Done when

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] UiState covers loading, empty and error states
- [x] Works without the backend, so it doesn't wait for the API
- [x] ViewModel unit tests pass
- [x] Previews exist for each screen
- [x] Only depends on `core:*` modules
- [ ] Reads the real `/v1/cases` endpoints instead of `CasesSampleData`
