# Architecture

## MVVM, layer by layer

```
 ┌───────────── feature module ─────────────┐
 │  XScreen (Compose)  ──Action──▶  XViewModel │
 │        ▲                          │        │
 │        └──── StateFlow<XUiState> ─┘        │
 └───────────────────────┬──────────────────┘
                         │ calls
               core:domain  (use cases shared by several screens, optional)
                         │
               core:data    (Repository interfaces → Default…Repository)
                  │          │            │
        core:network   core:database   core:datastore
        (Retrofit API)   (Room)        (preferences, tokens)
```

| Layer | Lives in | Knows about | Must NOT know about |
|---|---|---|---|
| **View**: `XScreen.kt`, `components/` | `feature:*` | `XUiState`, `XAction`, `core:designsystem`, `core:ui` | ViewModel internals, repositories, DTOs |
| **ViewModel**: `XViewModel.kt` | `feature:*` | use cases, repository interfaces, `core:model` | Compose, Context, Retrofit, Room |
| **Model / Data**: repositories | `core:data` | network, database, datastore, `core:model` | Any feature |

### Per-screen pattern

- `XUiState`: one immutable data class or sealed interface (`Loading`, `Success`, `Error`). The screen renders only this.
- `XAction`: a sealed interface of user intents (`OnBookmarkClick`, `OnRetry`, `OnArticleClick(number)`…).
- `XViewModel`: `val uiState: StateFlow<XUiState>` and `fun onAction(action: XAction)`. One-off navigation events go out as a callback or a `Channel`.
- `XScreen`: two composables.
  - `XRoute(viewModel = hiltViewModel(), onNavigate)` collects state and is the only one that touches the ViewModel.
  - `XScreen(uiState, onAction)` is stateless, previewable and testable.

## Module dependency rules

```
app ──▶ feature:* ──▶ core:designsystem, core:ui, core:navigation, core:domain, core:data, core:model
                       core:data ──▶ core:network, core:database, core:datastore, core:model, core:common
```

1. **A feature never depends on another feature.** To open another screen, navigate to its route from `core:navigation/AsosRoutes.kt`.
2. **Only `app` knows every feature.** `AsosNavHost` calls each feature's `navigation/<Feature>Navigation.kt`.
3. **Shared UI goes in `core:designsystem`** (for example `VoteControl`, `ArticleLinkCard`, `AnswerOption`, `VerifiedBadge`). If two features need the same composable, move it there.
4. **Shared logic goes in `core:domain`** (for example `ToggleBookmarkUseCase`, used by 5 screens).
5. **Icons only from `AsosIcons`**, colours and type only from the theme. No hard-coded hex values in features.

## How the parts connect at the end

| Contract | File | Owner |
|---|---|---|
| Routes / navigation | `core/navigation/.../AsosRoutes.kt` | WP-00 |
| Data access | `core/data/.../repository/*Repository.kt` (interfaces) | WP-02 |
| Backend | `docs/api/openapi.yaml` | WP-02 + backend team |
| Look & feel | `core/designsystem` (theme, `AsosIcons`, components) | WP-01 |

Features build against **interfaces + fakes** (`core:testing/Fake*Repository`). Integration swaps the fakes for the real implementations in Hilt modules. Screen code doesn't change.

## Offline-first

The Constitution text, dictionary and bookmarks are cached in Room. Repositories return `Flow` from the database and refresh from the network in the background, so the reader works offline.

## Naming

| Thing | Convention | Example |
|---|---|---|
| Package | `uz.asos.<layer>.<module>[.<screen>]` | `uz.asos.feature.constitution.article` |
| Screen files | `<Name>Screen/ViewModel/UiState/Action.kt` | `ArticleScreen.kt` |
| Strings | `<module>_<key>` in the module's `strings.xml` | `home_daily_quiz_title` |
| Routes | lower camel case | `article/{number}` |
| Tests | `<Class>Test.kt` | `ArticleViewModelTest.kt` |
