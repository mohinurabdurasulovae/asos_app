# Handover — 27-noyabr 2026

Kechasi bajarilgan ish. Hammasi build bo'ladi va testlar o'tadi; hech narsa commit qilinmagan.

```
./gradlew :app:assembleDebug test
```
→ BUILD SUCCESSFUL · 179 test · 0 yiqilish · `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Nima qo'shildi

Uchta oxirgi placeholder bo'lim yozildi — dizayn artbordlari va jonli API bo'yicha.

| Bo'lim | Ekranlar | Artbord | Testlar |
|---|---|---|---|
| `:feature:dictionary` | Konstitutsiyaviy lug'at | `Lugat.dc.html` | 11 |
| `:feature:community` | Hamjamiyat + Muhokama | `Hamjamiyat.dc.html`, `Post.dc.html` | 24 |
| `:feature:profile` | Profil | `Profil.dc.html` | 12 |

Ular uchun `core:*` qatlami ham noldan yozildi (ilgari bo'sh placeholder edi):

- `core:model` — `dictionary`, `community`, `user` (profil modellari)
- `core:network` — `DictionaryDto/Api`, `CommunityDto/Api`, `UserApi` ga `GET /v1/me/profile` va `PATCH /v1/me`
- `core:data` — `DictionaryRepository`, `CommunityRepository` + implementatsiya va mapperlar; `UserRepository` kengaytirildi
- `core:testing` — `FakeDictionaryRepository`, `FakeCommunityRepository`, `FakeUserRepository` + namunaviy ma'lumotlar

`AsosNavHost` da endi faqat ikkita `comingSoon` qoldi: `SearchRoute` va `BookmarksRoute` —
ikkalasining ham `AsosRoutes.kt` da "screen TBD" deb belgilangan, dizayn artbordi yo'q.

## 2. Ertalab nimani sinash kerak

APK jonli API'ga qarab turibdi (`asos.api.berdiklichev.uz`), demak Konstitutsiya, Lug'at,
O'rganish, Kazuslar, Hamjamiyat va Huquqiy yordam tizimga kirmasdan ham ishlashi kerak.

1. **Lug'at** — qidiruv (yozishni to'xtatgandan 300 ms keyin yuklanadi), o'ng chetdagi alifbo
   tasmasi, "Kun atamasi" kartasi, atama ustiga bosganda ochiladigan sheet.
2. **Hamjamiyat** — mavzu chiplari, Ommabop/Yangi/Javobsiz tablari, ovoz berish (strelka va
   son darhol o'zgaradi, xato bo'lsa orqaga qaytadi), muhokamaga kirish.
3. **Muhokama** — "Eng yaxshi javob" advokat kartasi, ichma-ich javoblar, pastdagi javob
   yozish paneli.
4. **Profil** — tizimga kirmasdan "Kirish" kartasi ko'rinadi (401 so'rov yuborilmaydi).
   Kirgandan keyin: haftalik ustunchalar, daraja bari, yutuqlar, sozlamalar.

Tizimga kirish talab qiladigan narsalarni men sinay olmadim — API'da akkaunt yaratmadim.

## 3. Bilib qo'yish kerak bo'lgan narsalar

**Modda matni `parts` emas, `text` bilan keladi.** Jonli API tekshirildi (1, 27, 31, 50,
155-moddalar): hammasida `parts: []` va butun matn bitta `text` satrida. `ConstitutionMappers`
buni allaqachon qamrab olgan (`parts` bo'sh bo'lsa `text` bitta blok bo'ladi), shuning uchun
o'qish ekrani ishlaydi — lekin dizayndagi "1-qism / 2-qism" ajratmasi hech qachon
ko'rinmaydi. Backend `parts` yubora boshlasa, `core:network` dagi `ApiContractTest` birinchi
bo'lib aytadi.

**`/v1/terms` ro'yxati va detali bir xil emas.** Ro'yxatda `articles` — oddiy raqamlar,
detalda — to'liq obyektlar. Spetsifikatsiyada ikkalasi ham obyekt deb yozilgan. Kod jonli
API bo'yicha yozilgan (`TermSummary.articleNumbers` va `TermDetail.articles`).

**`/docs` va `/admin/` 401 qaytaradi** — login kerak, shuning uchun to'liq `openapi.yaml` ni
serverdan ololmadim. Repodagi `docs/api/openapi.yaml` (3522 qator) asos qilib olindi va
ommaviy endpoint'lar bilan solishtirib tekshirildi.

**Yangi: `core:network` kontrakt testi.** `ApiContractTest` jonli serverdan olingan haqiqiy
javoblarni (`core/network/src/test/resources/api/`) DTO'larga parse qiladi. Maydon nomi yoki
turi o'zgarsa, ilovada emas, shu testda ko'rinadi. Fixture'larni yangilash — o'sha GET'ni
qayta chaqirib, faylni ustiga yozish.

## 4. Ulanmagan qolgan joylar

Bular ataylab qoldirilgan — endpoint yo'q yoki boshqa WP ga tegishli:

- Muhokamani saqlash va "Ulashish" — endpoint ulanmagan, bosilganda xabar chiqadi
- Profil: "Shaxsiy ma'lumotlar", "Maxfiylik va xavfsizlik" — ekran yo'q
- Profil: "Chiqish" Kirish ekraniga olib boradi, lekin tokenni tozalash `:feature:auth` (WP-03) ishi
- "Yutuqlar · Barchasi" — to'liq yutuqlar ekrani yo'q
- Lug'atdagi "Tinglash" tugmasi — loyihada TTS yo'q, shuning uchun qo'yilmadi
- `:core:database` va `:core:domain` hamon placeholder, `settings.gradle.kts` ga qo'shilmagan
- `core:designsystem` dagi `DifficultyBars`, `AsosTopBar`, `AsosTextField`, `AsosSwitch`,
  `VoteControl` hamon placeholder — shuning uchun bo'limlar o'z variantini chizadi
  (`feature/cases/ui/CasesVisuals.kt`, `feature/community/feed/components/VoteColumn.kt`).
  Core'ga ko'chirish keyingi tozalash ishi.

## 5. Commit qilinmagan

229 fayl o'zgargan/yangi, hammasi ishchi katalogda. Oldingi sessiyalardagi ish (learn,
legalhelp, auth, home, cases) ham hamon commit qilinmagan — ya'ni bitta katta o'zgarish
to'plami. Bo'lib commit qilishni xohlasangiz, mantiqiy chegara: `core:*` → har bir
`feature:*` → `app` ulanishi.

---

## 6. Kirish oqimi o'zgartirildi (keyinroq qo'shildi)

OneID va telefon raqam olib tashlandi. Endi ikki yo'l bor: **Google** yoki **email manzilga
yuboriladigan 6 xonali kod**. Kod `noreply@asos.api.berdiklichev.uz` dan keladi va kod ekranida «Spam» /
«Promotions» papkasini tekshirish haqida ogohlantirish bor.

O'zgargan joylar: `feature/auth` (`EmailAuthRepository`, `OtpState.email`, `EmailField`,
`CodeStep` dagi ogohlantirish, `GoogleSignInButton`), matnlar va testlar.

**Server tomoni tayyor (27-noyabr 2026).** `lib/mail.php` yozildi va yuklandi,
`routes/auth.php` almashtirildi, `auth_codes` email bo'yicha kalitlandi. Telefon raqam
oqimi serverdan ham olib tashlandi, shuning uchun eski telefonli akkauntlar kira olmaydi —
ular qaytadan ro'yxatdan o'tadi.

**Kirish ≠ ro'yxatdan o'tish.** Ikkala endpoint ham `purpose` (`login` | `register`)
maydonini oladi. `login` da manzil bazada bo'lishi shart, aks holda kod umuman
yuborilmaydi va `404 not_registered` qaytadi; `register` da esa band manzil
`409 already_registered` beradi. Ilovada bu `AuthPurpose` va `OtpState.needsRegistration` /
`needsSignIn` orqali ko'rinadi: xato xabari email maydoni ostida chiqadi va pastdagi
havola to'g'ri ekranga yo'naltiradi. Ro'yxatdan o'tishda ism va familiya so'raladi, u
kod tasdiqlangach `PATCH /v1/me` bilan saqlanadi.

**Baza tozalandi (27-noyabr 2026).** Barcha foydalanuvchilar va ular qilgan ishlar
o'chirildi: `users`, `tokens`, `auth_codes`, `user_identities`, `devices`,
`login_attempts`, `bookmarks`, `reading_progress`, `notifications`,
`user_achievements`, `points_ledger`, `daily_activity`, `quiz_attempts`, `quiz_answers`,
`case_attempts`, `community_members`, `posts`, `comments`, `votes`, `reports`,
`lawyers`, `bookings`, `callback_requests`, `conversations`, `messages`, `ai_threads`,
`ai_messages`, `audit_log` — hammasi 0 qator. Kontent (moddalar, atamalar, testlar,
keyslar, jamoalar, darajalar, yutuqlar, sozlamalar) tegilmadi. Demak **Jamoa** feed'i va
**Yuristlar** ro'yxati sinaganda bo'sh ko'rinadi — bu kutilgan holat, xatolik emas.

**Google uchun SDK va client id yo'q.** Tugma turibdi, lekin bosilganda «hali sozlanmagan»
   deb yozadi. `signInWithProvider(GOOGLE, subject, …)` tayyor — Google Sign-In
   dependency'sini qo'shib, id token'dan `subject` ni uzatish qoldi.
