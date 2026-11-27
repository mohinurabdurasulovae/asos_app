# :feature:profile · Profil

**Work package:** WP-11. Owner: _assign a name here_

| Screen | Design artboard | Entry file | Route |
|---|---|---|---|
| Profil | `Profil.dc.html` | `ProfileScreen.kt` | `profile` |

Profil is a tab, so it has no back button. Guests see the sign-in card instead of a failed
request, because `GET /v1/me/profile` needs a token.

## Folder layout

```
profile/
├── navigation/   public entry point: the only file the app module uses
├── (root)/   ProfileScreen · ProfileViewModel · ProfileUiState · ProfileAction
├── components/   ProfileHeader · StatsRow · WeeklyActivityChart · AchievementsGrid ·
│                 SavedItemsSection · SettingsSection · ChoiceSheet
```

## API endpoints used (through `core:data`'s UserRepository)

- `GET /v1/me/profile` — the whole screen in one call: user, stats, week, daily goal,
  achievements and the saved-item counts
- `PATCH /v1/me` — the settings rows (language, text size, daily reminders)

See [`docs/api/openapi.yaml`](../../docs/api/openapi.yaml).

The reader text size is a device setting first (`core:datastore`), mirrored to the server, so
it applies even when the call fails or the user is a guest.

## Not built

- "Shaxsiy ma'lumotlar" and "Maxfiylik va xavfsizlik" rows: no screens exist yet, so they
  show a notice.
- "Chiqish" navigates to Kirish; clearing the token belongs to `:feature:auth` (WP-03).
- "Yutuqlar · Barchasi" has no full achievements screen yet.

## Done when

- [x] Screen matches the design artboard (spacing, colours, fonts, states)
- [x] UiState covers loading, guest and error states
- [x] Settings changes roll back when the server refuses them
- [x] ViewModel unit tests pass
- [x] Previews exist for the screen
- [x] Only depends on `core:*` modules
