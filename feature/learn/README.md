# :feature:learn · O‘rganish + Test (Learning hub & quiz)

**Work package:** WP-07. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| O‘rganish | `Organish.dc.html` | `hub/LearnScreen.kt` | `learn` |
| Test savoli | `Test.dc.html` | `quiz/QuizScreen.kt` | `quiz/{quizId}` |

## Folder layout

```
learn/
├── navigation/        public entry point: the only file the app module uses
├── hub/  LearnScreen · LearnViewModel · LearnUiState · LearnAction · components/
├── quiz/  QuizScreen · QuizViewModel · QuizUiState · QuizAction · components/
```

## API endpoints used (through `core:data` repositories)

- `GET /learning/progress`
- `GET /me/achievements`
- `GET /quizzes/topics`
- `GET /quizzes/{quizId}`
- `POST /quizzes/{quizId}/answers`

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

## Done when

- [ ] Screens match the design artboards (spacing, colours, fonts, states)
- [ ] UiState covers loading, empty and error states
- [ ] Works on the fake repositories from `core:testing`, so it doesn't wait for the backend
- [ ] ViewModel unit tests pass
- [ ] Previews exist for each screen and component
- [ ] Only depends on `core:*` modules
