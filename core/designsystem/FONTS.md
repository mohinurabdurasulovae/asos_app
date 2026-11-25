# Fonts

Bundled in `src/main/res/font/` (both families are under the SIL Open Font License 1.1, free to bundle in apps):

| File | Family | Weight |
|---|---|---|
| `plus_jakarta_sans_regular/medium/semibold/bold/extrabold.ttf` | Plus Jakarta Sans | 400, 500, 600, 700, 800 |
| `source_serif_4_regular/semibold/bold.ttf`, `source_serif_4_italic.ttf` | Source Serif 4 | 400, 600, 700, 400 italic |

- **Source Serif 4**: official legal text, article numbers, quotes.
- **Plus Jakarta Sans**: everything else.

Plus Jakarta Sans has no `ʻ` (U+02BB), which official Uzbek text often uses in oʻ / gʻ.
Interface text therefore goes through `UzbekFormatters.display()`, which shows it as `‘`
(as in the design). The official article text keeps its original characters and is set in
Source Serif 4, which has the glyph.

Do not put any other file (README, .md) inside `res/font/`: Android fails the build on it.
