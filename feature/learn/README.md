# :feature:learn · O‘rganish + Test savoli

**Work package:** WP-07

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| O‘rganish | `Organish.dc.html` | `hub/LearnScreen.kt` | `LearnRoute` (tab) |
| Test savoli | `Test.dc.html` | `quiz/QuizScreen.kt` | `QuizRoute(quizId)` |

## Folder layout

```
learn/
├── navigation/  LearnNavigation.kt: learnScreens(), the only file the app module uses
├── hub/         LearnScreen · LearnViewModel · LearnUiState · LearnAction · components/
├── quiz/        QuizScreen · QuizViewModel · QuizUiState · QuizAction · components/
└── ui/          LearnVisuals.kt: icon and colour choices shared by both screens
```

## API endpoints used (through `LearningRepository`)

| Endpoint | Token | Used for |
|---|---|---|
| `GET /v1/learn` | optional | The whole O‘rganish screen. Guests get no overview, points or streak. |
| `POST /v1/quizzes/{id}/attempts` | required | Opens a test, or resumes the unfinished attempt ("Davom etish"). |
| `POST /v1/attempts/{id}/answers` | required | Answers one question. Returns the right option, explanation, article and, on the last question, the result. |
| `POST /v1/attempts/{id}/finish` | required | "Tugatish", or when nothing is left to answer. |

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## How it behaves

- **Levels.** The list starts on O‘rta (as in the design), or the first level that has tests. A level the user picks stays selected across refreshes.
- **Row looks.** Never tried → "Boshlash" button. Tried or open attempt → percent + bar ("… · Davom etish" if there's an open attempt). Perfect score → green "Tugallangan" with a check.
- **Guests.** O‘rganish works without a token: the progress card becomes a "Kirish" card and the pills are hidden. Opening a test asks to sign in (the API needs a token for attempts).
- **Answering.** Tapping an option sends it right away, as in the design. Options are coloured only after the server answers (it never sends the right answer in advance). A question can be answered once.
- **"Qaytadan".** The design's panel has "Qaytadan", but the API rejects a second answer (409), so the panel's left button is **"Tugatish"** (finish now). "Qaytadan" is on the result screen and opens a new attempt.
- **Timer.** Counts the time on the current question and sends it as `seconds` with the answer.
- **Refresh.** O‘rganish reloads quietly when you come back from a test.

## Done

- [x] Screens match the design artboards (spacing, colours, fonts, states)
- [x] Loading, empty, error and sign-in states
- [x] Works on `FakeLearningRepository` from `core:testing`
- [x] ViewModel unit tests (`LearnViewModelTest`, `QuizViewModelTest`)
- [x] Previews for each screen and component
- [x] Only depends on `core:*` modules
