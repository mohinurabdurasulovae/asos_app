# Contributing

1. Find your work package in [`docs/TEAM_PLAN.md`](docs/TEAM_PLAN.md) and read your module's `README.md`.
2. Branch from `main`: `wp-<nn>/<short-topic>`, e.g. `wp-05/article-reader-summary`.
3. Stay inside your module. Changes to `core:*`, `AsosRoutes.kt`, `openapi.yaml` or `AsosIcons.kt` go in a small separate PR, reviewed by that area's owner.
4. Build against the fakes in `core:testing` until the real repository is ready.
5. Before opening a PR:
   - previews for every screen and component
   - ViewModel unit tests pass
   - no hard-coded colours, fonts or icons (use the theme and `AsosIcons`)
   - all user-facing text in your module's `strings.xml`, in Uzbek (Latin)
   - interactive elements have a content description in Uzbek and are at least 44 dp
6. Fill in the PR template and attach a screenshot next to the design artboard.
