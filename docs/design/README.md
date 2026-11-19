# Asos — Konstitutsiya ilovasi

**Konstitutsiyani o‘qing, tushuning va hayotda qo‘llang.**

Asos is a mobile app design that makes the Constitution of the Republic of Uzbekistan (2023 edition) easy to read, understand and use in everyday life. It is aimed mainly at young people. The whole interface is in Uzbek (Latin script).

- **Design canvas:** https://claude.ai/artifact/QXopGLYXeSP1DJjP4exUvm
- **Canva copy (editable):** https://www.canva.com/d/6zdicDVCC9y-lj1

The name *Asos* comes from *Asosiy qonun* (the Basic Law). The logo shows three pillars under a roof.

## Screens

| # | Screen | What it does |
|---|--------|--------------|
| 1 | Kirish | Log in with a phone number, OneID or Google |
| 2 | Bosh sahifa | Greeting, search, article of the day, topics, continue learning, daily quiz, help shortcuts, community highlights |
| 3 | Konstitutsiya | Browse by section (bo‘lim) and chapter (bob), with reading progress |
| 4 | Modda (50-modda) | Article reader: official text, plain-language summary, real-life examples, related articles |
| 5 | Lug‘at | Legal dictionary: term of the day, A–Z index, saved terms |
| 6 | O‘rganish | Learning hub: overall progress, difficulty levels, tests by topic, achievements |
| 7 | Test | Quiz question with instant right/wrong feedback and an explanation |
| 8 | Kazuslar | List of real-life cases, sorted by topic and difficulty |
| 9 | Kazus | Solve a case: pick the article, write a solution, compare with a lawyer's explanation |
| 10 | Hamjamiyat | Reddit-style Q&A feed: topic communities, votes, anonymous posting |
| 11 | Muhokama | Discussion thread: verified lawyer answer, nested replies |
| 12 | Huquqiy yordam | AI assistant, hotline, lawyer profiles |
| 13 | Chat | Chat with a verified lawyer |
| 14 | Profil | Stats, weekly activity, achievements, saved items, settings |
| — | Komponentlar | Colors, fonts and reusable components |

**Navigation:** a five-item bottom bar (Bosh sahifa · Konstitutsiya · O‘rganish · Hamjamiyat · Profil). The dictionary and legal help open from the home screen and from related screens.

**Prototype:** press Play on the canvas to click between screens. The Test and Kazus screens are interactive.

## Design system

**Colors**

| Token | Hex | Use |
|-------|-----|-----|
| Brand 500 | `#2478DE` | Large surfaces, icons, progress bars |
| Brand 600 | `#1C64C0` | Buttons and links (passes contrast with white text) |
| Navy | `#172B4D` | Main text, dark surfaces |
| Text 2 | `#5E6A80` | Secondary text |
| Soft gray | `#8A94A6` | Decoration and icons only |
| Background | `#F5F8FC` | App background |
| Brand 50 | `#EAF2FD` | Selected states |

White text on `#2478DE` is 4.4:1, just below the 4.5:1 accessibility minimum for small text. That is why buttons use Brand 600.

**Fonts**

- *Source Serif 4* for official legal text, article numbers and quotes
- *Plus Jakarta Sans* for everything else in the interface

**Style:** rounded cards, soft shadows, line icons. Each feature has its own layout. For example, the reader is a clean text page, the quiz is full-screen, and the feed is a dense list.

## Content notes

- Article texts come from the official 2023 Constitution: 6 sections, 27 chapters, 155 articles.
- 50-modda shows only its first three parts. The rest is behind a "To‘liq matnni ochish" (open full text) button.
- **Placeholder:** the hotline number `+998 (71) 000-00-00` must be replaced with the real one.
- All lawyers, users and community posts are invented sample content.

## Sources

[1] O‘zbekiston Respublikasi Konstitutsiyasi — gov.uz: https://gov.uz/oz/mfa/sections/view/14475
[2] Konstitutsiya, 50-modda — UN Women Constitutions database: https://constitutions.unwomen.org/en/countries/asia/uzbekistan
[3] Konstitutsiya haqida 10 ta fakt — talimxabarlari.uz: https://talimxabarlari.uz/16895/
