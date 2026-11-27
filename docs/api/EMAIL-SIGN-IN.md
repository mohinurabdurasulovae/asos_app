# Email orqali kirish — bajarildi

**Holat: TAYYOR** (27-noyabr 2026). Server ham, ilova ham email kodi bilan ishlaydi.
Quyida nima qilinganining yozuvi; kontrakt: [`openapi.yaml`](openapi.yaml).

Serverda o'zgargan joylar:

- **yangi** `lib/mail.php` — email tekshirish, xat yuborish, kod oqimi va
  `auth_codes` jadvalining avtomatik migratsiyasi
- `index.php` — bitta `require_once` qatori
- `routes/auth.php` — `need_phone` o'rniga `need_email`, `email_*` funksiyalari
- ikkala endpoint ham ixtiyoriy `purpose` (`login` | `register`) maydonini oladi:
  `login` da manzil ro'yxatdan o'tgan bo'lishi shart (aks holda `404 not_registered`),
  `register` da esa bo'lmasligi shart (`409 already_registered`). Ro'yxatdan
  o'tmagan manzilga kirish kodi umuman yuborilmaydi.
- baza: `auth_codes.phone varchar(20)` → `auth_codes.email varchar(254)` (birlamchi kalit),
  `users.email` ga unique indeks

Zaxira nusxa: `/www/asos.api.berdiklichev.uz.zip` (o'zgarishlardan oldin olingan).

---

## Quyidagisi o'sha paytdagi topshiriq matni (tarix uchun)

## Hozirgi holat (27-noyabr 2026, tekshirildi)

```
POST /v1/auth/request-code  {"email":"a@b.uz"}  →  422 {"error":"missing_field","field":"phone"}
POST /v1/auth/verify        {"email":"a@b.uz"}  →  422 {"error":"missing_field","field":"phone"}
POST /v1/auth/email/request-code                →  404
POST /v1/auth/verify-email                      →  404
```

Ya'ni email oqimi uchun hech narsa yo'q. Ilova `email` yuboradi va shu 422 ni oladi.

## Nima qilish kerak

Yangi endpoint **kerak emas**. Mavjud ikkitasi `phone` o'rniga `email` ni ham qabul
qilsa yetarli.

### 1. `POST /v1/auth/request-code`

Kiruvchi: `email` **yoki** `phone` — aynan bittasi.

| Holat | Javob |
|---|---|
| Ikkalasi ham yo'q | `422 {"error":"missing_field","message":"«email» maydoni kerak.","field":"email"}` |
| Ikkalasi ham bor | `422 {"error":"conflicting_fields","message":"Email yoki telefon — bittasini yuboring.","field":"email"}` |
| Email formati xato | `422 {"error":"bad_email","message":"Email manzil noto'g'ri.","field":"email"}` |
| 60 soniya o'tmagan | `429 {"error":"too_soon","retryAfter":42}` (hozirgi `phone` xatti-harakati bilan bir xil) |
| Xat ketmadi | `502 {"error":"mail_failed","message":"Xat yuborib bo'lmadi. Birozdan keyin urinib ko'ring."}` |
| Muvaffaqiyat | `200 {"ok":true,"email":"a@b.uz","expiresIn":300,"resendIn":60}` |

`dev_mode: true` bo'lganda javobga `devCode` ham qo'shiladi — hozirgi `phone` oqimidagidek.

Qolgan qoidalar `phone` bilan bir xil: kod 6 xonali, 5 daqiqa amal qiladi, qayta yuborish
orasida 60 soniya.

Email'ni saqlashdan oldin **normallashtiring**: `trim()` va `strtolower()`. Aks holda
`Aziza@Example.uz` va `aziza@example.uz` ikki xil akkaunt bo'lib ketadi.

### 2. `POST /v1/auth/verify`

Kiruvchi: `email` yoki `phone` (request-code da nima yuborilgan bo'lsa) + `code`.

Javoblar hozirgidek: `200 {token, user, isNew}`, `401` + `triesLeft`, `410` muddati tugagan,
`429` urinishlar tugadi.

Manzil birinchi marta ko'rilsa, akkaunt shu yerda yaratiladi (`isNew: true`) va
`users.email` to'ldiriladi.

### 3. Baza

Hozir OTP jadvali va `users` telefonga bog'langan bo'lsa:

- `users.phone` — `NOT NULL` bo'lsa, `NULL` ga ruxsat bering (email bilan kirganlarda telefon yo'q)
- `users.email` — unique index kerak
- OTP jadvalidagi `phone` ustunini umumiy `identifier` (+ `channel` enum: `sms` | `email`) ga
  aylantirish eng toza yo'l. Yoki shunchaki `email` ustuni qo'shib, ikkalasini ham tekshirish.
- Kamida bittasi (`phone` yoki `email`) to'ldirilgan bo'lishi shart.

### 4. Xatning o'zi

Jo'natuvchi: **noreply@asos.api.berdiklichev.uz** (ilovada shu manzil ko'rsatilgan — o'zgartirsangiz
`CodeRequest.CODE_SENDER` ni ham o'zgartiring).

Mavzu: `Asos — tasdiqlash kodi: 123456`

Kodni **mavzuda ham** yozish foydali: odam pochtani ochmasdan ham ko'radi.

Matn (oddiy va HTML, ikkalasi ham):

```
Asos ilovasiga kirish uchun tasdiqlash kodingiz:

    123456

Kod 5 daqiqa amal qiladi. Agar bu so'rovni siz yubormagan bo'lsangiz, bu xatni
e'tiborsiz qoldiring.

— Asos
```

### 5. Spam papkasiga tushmaslik uchun

Ilova foydalanuvchini «Spam» papkasini tekshirishga ogohlantiradi, lekin buni **kamaytirish
server tomonida**:

- **SPF** — `asos.api.berdiklichev.uz` DNS'ida jo'natuvchi serverga ruxsat
- **DKIM** — xatlar imzolansin
- **DMARC** — kamida `p=none`, monitoring bilan
- `From: Asos <noreply@asos.api.berdiklichev.uz>` va ishlaydigan `Reply-To`
- `List-Unsubscribe` **qo'ymang** — bu tranzaksion xat, marketing emas
- Imkon bo'lsa alohida subdomen (`mail.asos.api.berdiklichev.uz`) va tranzaksion provayder (Postmark,
  SendGrid, Amazon SES) — ularning yetkazib berish ko'rsatkichi o'z SMTP'dan yaxshiroq

Tekshirish: [mail-tester.com](https://www.mail-tester.com) ga sinov xati yuboring, 10 dan
kamida 8 ball bo'lsin.

### 6. Rate limit va xavfsizlik

- Bitta manzilga: 60 soniyada 1 marta, soatiga 5 marta
- Bitta IP'dan: soatiga 20 marta (aks holda server pochta spam'iga aylanadi)
- Kodni tekshirishda 5 ta noto'g'ri urinishdan keyin kodni bekor qiling (`429`)
- Kodni bazada **hash** qilib saqlang (hozirgi telefon oqimi qanday saqlasa, shunday)

## Qabul qilish mezoni

Shu to'rt buyruq ishlasa, tayyor:

```powershell
curl -X POST https://asos.api.berdiklichev.uz/v1/auth/request-code -H "Content-Type: application/json" -d '{\"email\":\"test@example.uz\"}'
```

1. Yuqoridagi buyruq `200` va `expiresIn`/`resendIn` qaytarsin
2. `test@example.uz` ga 6 xonali kodli xat kelsin (jo'natuvchi `noreply@asos.api.berdiklichev.uz`)
3. `POST /v1/auth/verify {"email":"test@example.uz","code":"<kod>"}` → `200` + `token`
4. Noto'g'ri kod → `401` + `triesLeft`

Shundan keyin ilovada hech narsa o'zgartirish shart emas — u allaqachon shu shaklda
yuboradi.

## Ilova tomoni qayerda

| Nima | Fayl |
|---|---|
| Endpoint'lar | `feature/auth/.../data/network/SignInApi.kt` |
| So'rov/javob shakllari | `feature/auth/.../data/network/SignInDtos.kt` |
| Jo'natuvchi manzil (UI'da) | `feature/auth/.../data/model/AuthModels.kt` → `CodeRequest.CODE_SENDER` |
| Spam ogohlantirishi | `feature/auth/.../common/components/CodeStep.kt` |

Backend boshqa yo'l yoki maydon nomi tanlasa, birinchi ikki fayl o'zgaradi — boshqa hech
narsa.
