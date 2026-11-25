# Asos: Konstitutsiya ilovasi

**Konstitutsiyani o‘qing, tushuning va hayotda qo‘llang.**

Android app (Kotlin + Jetpack Compose, MVVM) that makes the Constitution of the Republic of Uzbekistan (2023 edition) easy to read, understand and use. UI language: Uzbek (Latin).

> **Status:** the Gradle setup, app shell, design system basics, data layer for the Constitution, and the **Konstitutsiya** and **Modda** screens are implemented and connected to the live API. Every other file is still a placeholder with a header comment that says what goes in it.

## Run it

1. Open the `asos_app` folder in Android Studio (Ladybug or newer) and let Gradle sync. It downloads Gradle 8.11.1, AGP 8.7.3 and Kotlin 2.1.0.
2. Run the `app` configuration on an emulator or phone (Android 8.0+). The app opens on Konstitutsiya.
3. Unit tests: `./gradlew :feature:constitution:testDebugUnitTest`.

The backend URL is the Gradle property `asos.baseUrl` in `gradle.properties`. For a local server (`php -S 127.0.0.1:8080 index.php`), set `asos.baseUrl=http://10.0.2.2:8080/` in `~/.gradle/gradle.properties` (local HTTP also needs a network security config for cleartext).

## Start here

| If you are… | Read |
|---|---|
| Everyone | [`docs/TEAM_PLAN.md`](docs/TEAM_PLAN.md): work packages, order, who does what |
| Building a screen | [`docs/SCREENS.md`](docs/SCREENS.md), then your module's `README.md` in `feature/<name>/` |
| Working on architecture or data | [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) |
| Working on the backend or API | [`docs/api/README.md`](docs/api/README.md) and [`docs/api/openapi.yaml`](docs/api/openapi.yaml) |
| Working on UI or design | [`docs/design/README.md`](docs/design/README.md), [`docs/design/ICONS.md`](docs/design/ICONS.md), [`docs/design/icons-preview.html`](docs/design/icons-preview.html) |

Design canvas: https://claude.ai/artifact/QXopGLYXeSP1DJjP4exUvm

## Repository map

```
asos_app/
├── app/                      App shell: MainActivity, NavHost, bottom bar. Wires all features together.
├── build-logic/convention/   Gradle convention plugins (shared build config)
├── gradle/libs.versions.toml Version catalog (all library versions)
│
├── core/
│   ├── common/               Result type, dispatchers, Uzbek formatters
│   ├── model/                Domain models (Article, Term, Quiz, LegalCase, Post, Lawyer, User…)
│   ├── network/              Retrofit APIs + DTOs (matches docs/api/openapi.yaml) + FakeAsosApi
│   ├── database/             Room: offline Constitution, bookmarks, progress
│   ├── datastore/            Preferences (text size, reminders, language) + tokens
│   ├── data/                 Repositories (interfaces + implementations)
│   ├── domain/               Use cases shared by several screens
│   ├── navigation/           AsosRoutes: every screen's route, the integration contract
│   ├── designsystem/         Theme (colours, fonts, shapes), AsosIcons, reusable components
│   ├── ui/                   Loading / error / empty states, preview helpers
│   └── testing/              Fake repositories, sample data, test rules
│
├── feature/                  One module per feature area; each has screens in MVVM form
│   ├── auth/                 Kirish
│   ├── home/                 Bosh sahifa
│   ├── constitution/         Konstitutsiya · Modda (article reader)
│   ├── dictionary/           Lug‘at
│   ├── learn/                O‘rganish · Test
│   ├── cases/                Kazuslar · Kazus
│   ├── community/            Hamjamiyat · Muhokama
│   ├── legalhelp/            Huquqiy yordam · Chat
│   └── profile/              Profil
│
└── docs/                     Plan, architecture, screen map, API contract, design notes
```

Every screen folder has the same five parts:

```
<ScreenName>Screen.kt      Composable UI (stateless) + Route that connects the ViewModel
<ScreenName>ViewModel.kt   State holder, business calls
<ScreenName>UiState.kt     What the screen renders
<ScreenName>Action.kt      What the user can do
components/                Pieces used only by this screen
```

## Tech stack (planned)

Kotlin · Jetpack Compose + Material 3 · Navigation Compose (type-safe routes) · Hilt · Coroutines/Flow · Retrofit + OkHttp + kotlinx.serialization · Room · DataStore · Coil · JUnit + Turbine.
