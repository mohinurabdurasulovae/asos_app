# Team plan

The work is split into **12 work packages (WP)**. Each WP is one or a few folders, so people rarely edit the same files. Put a name next to each WP and add the GitHub usernames to `.github/CODEOWNERS`.

## Work packages

| WP | Owner | Module(s) | Screens / scope | Size | Depends on |
|---|---|---|---|---|---|
| **WP-00** Foundation | _name_ | `app`, `build-logic`, `gradle`, `core:common`, `core:navigation` | Gradle setup, app shell, NavHost, bottom bar, `AsosRoutes` | M | — |
| **WP-01** Design system | _name_ | `core:designsystem`, `core:ui` | Theme, fonts, 21 shared components, states. Icons already done | L | — |
| **WP-02** Data & API | _name_ | `core:model`, `core:network`, `core:database`, `core:datastore`, `core:data`, `core:domain`, `core:testing`, `docs/api` | Models, API client, DB, repositories, fakes, use cases | XL | — |
| **WP-03** Auth | _name_ | `feature:auth` | Kirish | S | 00, 01 |
| **WP-04** Home | _name_ | `feature:home` | Bosh sahifa | L | 00, 01, fakes from 02 |
| **WP-05** Constitution | _name_ | `feature:constitution` | Konstitutsiya, Modda | L | 00, 01, fakes |
| **WP-06** Dictionary | _name_ | `feature:dictionary` | Lug‘at | S | 00, 01, fakes |
| **WP-07** Learn | _name_ | `feature:learn` | O‘rganish, Test | M | 00, 01, fakes |
| **WP-08** Cases | _name_ | `feature:cases` | Kazuslar, Kazus | M | 00, 01, fakes |
| **WP-09** Community | _name_ | `feature:community` | Hamjamiyat, Muhokama | L | 00, 01, fakes |
| **WP-10** Legal help | _name_ | `feature:legalhelp` | Huquqiy yordam, Chat | M | 00, 01, fakes |
| **WP-11** Profile | _name_ | `feature:profile` | Profil | M | 00, 01, fakes |

Sizes: S ≈ 2–3 days, M ≈ 1 week, L ≈ 1.5 weeks, XL ≈ 2+ weeks (one person).

**Suggested pairing for a small team:** WP-03 + WP-06 + WP-11 (lighter screens) can go to one person, and WP-07 + WP-08 (similar quiz/case logic) to another.

## Phases

### Phase 0: Contracts (first 2–3 days, before anyone builds screens)
Owners of WP-00, 01 and 02 agree on and merge:
1. `AsosRoutes.kt`: every route and its arguments.
2. `core:model`: the domain classes, with fields.
3. `*Repository.kt` interfaces and the `Fake*Repository` versions with sample data from the design.
4. `Color.kt`, `Type.kt`, `AsosTheme.kt` and the signatures of shared components (implementation can follow).
5. `openapi.yaml` reviewed with the backend team.

➡ After this, **all feature WPs can start in parallel.**

### Phase 1: Parallel build
- Feature owners build screens against the fakes, with previews and ViewModel tests.
- WP-01 implements the shared components. Feature owners use them as they land.
- WP-02 implements the network, database and real repositories.
- The backend team implements the API (shared endpoints first; see `docs/api/README.md`).

### Phase 2: Integration
1. WP-00 registers every feature in `AsosNavHost` (most of this happens as features merge).
2. WP-02 switches the Hilt bindings from fakes to real repositories.
3. Test the main flows end to end:
   - Kirish → Bosh sahifa → Modda → Test
   - Kazuslar → Kazus → Huquqiy yordam → Chat
   - Hamjamiyat → Muhokama → Chat
   - Profil → Saqlanganlar → Modda
4. Design QA against the canvas, then accessibility (touch targets ≥ 44 dp, contrast, TalkBack labels in Uzbek).

## Cross-team rules

- **Need a change in `core:*`?** Ask its owner, or open a small separate PR and tag them.
- **Need a new endpoint?** Change `docs/api/openapi.yaml` first (WP-02 reviews).
- **Need a new route?** Add it to `AsosRoutes.kt` (WP-00 reviews).
- **Need a new icon?** Add it to `AsosIcons.kt` in the same style (WP-01 reviews).
- Branch names: `wp-04/home-daily-quiz`. One PR per screen or component when possible.

## Open items (not in the design yet)

- OTP entry screen after the phone number (Kirish shows only the phone field)
- Global search results screen and the saved-items (Saqlanganlar) screen. Routes are reserved: `search`, `bookmarks`
- Quiz results screen after the last question
- Notifications list (bell on Bosh sahifa)
- Real hotline number (placeholder `+998 (71) 000-00-00`)
