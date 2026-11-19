# Screen map

All 14 screens in the design, where each one lives in the code, and how they link. Paths are relative to `feature/<module>/src/main/kotlin/uz/asos/feature/<module>/`.

| # | Screen (uz) | Design artboard | Module · WP | Folder | Route |
|---|---|---|---|---|---|
| 1 | Kirish | `Kirish.dc.html` | `:feature:auth` · WP-03 | `login/` | `login` |
| 2 | Bosh sahifa | `Main.dc.html` | `:feature:home` · WP-04 | `(root)` | `home` ★ |
| 3 | Konstitutsiya | `Konstitutsiya.dc.html` | `:feature:constitution` · WP-05 | `browse/` | `constitution` ★ |
| 4 | Modda (50-modda) | `Modda.dc.html` | `:feature:constitution` · WP-05 | `article/` | `article/{number}` |
| 5 | Lug‘at | `Lugat.dc.html` | `:feature:dictionary` · WP-06 | `(root)` | `dictionary` |
| 6 | O‘rganish | `Organish.dc.html` | `:feature:learn` · WP-07 | `hub/` | `learn` ★ |
| 7 | Test | `Test.dc.html` | `:feature:learn` · WP-07 | `quiz/` | `quiz/{quizId}` |
| 8 | Kazuslar | `Kazuslar.dc.html` | `:feature:cases` · WP-08 | `list/` | `cases` |
| 9 | Kazus | `Kazus.dc.html` | `:feature:cases` · WP-08 | `detail/` | `case/{caseId}` |
| 10 | Hamjamiyat | `Hamjamiyat.dc.html` | `:feature:community` · WP-09 | `feed/` | `community` ★ |
| 11 | Muhokama | `Post.dc.html` | `:feature:community` · WP-09 | `post/` | `post/{postId}` |
| 12 | Huquqiy yordam | `Yordam.dc.html` | `:feature:legalhelp` · WP-10 | `help/` | `legalHelp` |
| 13 | Chat | `Chat.dc.html` | `:feature:legalhelp` · WP-10 | `chat/` | `chat/{chatId}` |
| 14 | Profil | `Profil.dc.html` | `:feature:profile` · WP-11 | `(root)` | `profile` ★ |
| — | TabBar | `TabBar.dc.html` | `:core:designsystem` · WP-01 | `component/AsosBottomBar.kt` | — |
| — | Komponentlar | `Komponentlar.dc.html` | `:core:designsystem` · WP-01 | `theme/`, `component/`, `icon/` | — |

★ = bottom-bar tab (Bosh sahifa · Konstitutsiya · O‘rganish · Hamjamiyat · Profil). The bottom bar is hidden on the other screens.

## Navigation flow

```
Kirish ──▶ Bosh sahifa
Bosh sahifa ──▶ Profil, Modda, Konstitutsiya, O‘rganish, Test, Kazus, Huquqiy yordam, Lug‘at, Hamjamiyat, Muhokama
Konstitutsiya ──▶ Modda
Modda ──▶ Kazuslar, Lug‘at, Hamjamiyat, Test
Lug‘at ──▶ Modda
O‘rganish ──▶ Test, Kazuslar, Profil
Test ──▶ O‘rganish
Kazuslar ──▶ Kazus
Kazus ──▶ Huquqiy yordam, Kazuslar (next case)
Hamjamiyat ──▶ Muhokama
Muhokama ──▶ Chat
Huquqiy yordam ──▶ Chat
Chat ──▶ Modda
Profil ──▶ Konstitutsiya, Hamjamiyat, Lug‘at, Kirish (logout)
```

## Shared components (reused across screens, in `core:designsystem/component/`)

| Component | Used on |
|---|---|
| `AsosBottomBar` | The 5 tab screens |
| `AsosTopBar` | Modda, Lug‘at, Kazuslar, Kazus, Muhokama, Huquqiy yordam, Chat |
| `VoteControl` | Bosh sahifa, Hamjamiyat, Muhokama |
| `ArticleLinkCard` / `ArticleNumberBadge` | Modda, Kazus, Chat, Muhokama, Konstitutsiya |
| `AnswerOption` | Test, Bosh sahifa (daily quiz) |
| `VerifiedBadge` | Bosh sahifa, Kazus, Hamjamiyat, Muhokama, Huquqiy yordam, Chat |
| `DifficultyBars` | Kazuslar, O‘rganish |
| `ProgressRing` / `ProgressBar` | Konstitutsiya, O‘rganish, Kazuslar, Profil |
| `StatusIcon` | Konstitutsiya, O‘rganish, Kazuslar, Test |
| `AsosSearchField` | Bosh sahifa, Konstitutsiya, Lug‘at, Hamjamiyat |
| `StreakBadge` | Bosh sahifa, O‘rganish, Profil |

The endpoints each screen uses are in [`api/README.md`](api/README.md).
