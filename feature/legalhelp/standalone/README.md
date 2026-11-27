# Asos · Huquqiy yordam

«Asos» prototipidagi «Huquqiy yordam» sahifasi. Kotlin, Jetpack Compose va Material 3 asosida tayyorlangan. Plus Jakarta Sans va Source Serif 4 shriftlari loyiha tarkibida.

## Android Studio’da ochish

1. ZIP faylni arxivdan chiqaring. Android Studio’da **File → Open** orqali `settings.gradle.kts` turgan `Asos_Huquqiy_Yordam` papkasini tanlang. Android Studio Quail 2 yoki AGP 9.3 ni qo‘llaydigan yangi versiyadan foydalaning.
2. **SDK Manager** ichida **Android SDK Platform 37.0** va **Android SDK Build-Tools 36.0.0** ni o‘rnating.
3. Gradle Sync’ni ishga tushiring. Loyiha Gradle uchun **JDK 25** ni tanlaydi. Zarur bo‘lsa, [Temurin 25](https://adoptium.net/temurin/releases/?version=25) ni o‘rnating va **Settings → Build Tools → Gradle → Gradle JDK** orqali tanlang.
4. Qurilma yoki emulyatorni tanlang va **Run ▶** ni bosing.

Loyiha Gradle 9.5.1, Android Gradle Plugin 9.3.2 va Kotlin Compose 2.3.21 dan foydalanadi. Java va Kotlin baytkod maqsadi JVM 17. Android SDK qiymatlari `compileSdk = 37`, `targetSdk = 36`, `minSdk = 26`. Ilova Android 8.0 va undan yangi tizimlarda ishlashga mo‘ljallangan.

Windows’da loyiha papkasidan debug APK yig‘ish buyrug‘i.

```powershell
.\gradlew.bat :app:assembleDebug
```

Debug APK `app/build/outputs/apk/debug/app-debug.apk` yo‘lida hosil bo‘ladi.

Compose Preview uchun `LegalHelpScreen.kt` oxiridagi `LegalHelpPreview` funksiyasini oching. 390 dp telefon ko‘rinishi va butun sahifa uchun alohida preview mavjud.

## Asos API bilan ishlash

Debug va release ilovasi `https://asos.api.berdiklichev.uz` manziliga ulanadi. Android Studio’da loyiha papkasini oching, **Sync Now** tugmasini bosing, qurilma yoki emulyatorni tanlang va **Run ▶** ni bosing.

- **Huquqiy yordam** sahifasi `GET /v1/help` orqali moddani, AI tavsiyalarini va ishonch telefonini oladi.
- Advokatlar ro‘yxati, yo‘nalishlari, tajribasi, reytingi va onlayn holati `GET /v1/lawyers` orqali olinadi.
- AI savolni bevosita Gemini’ga yuboradi. Huquqiy mavzular bo‘yicha javob matnida qisqa izoh, qonun yoki kodeks nomi hamda modda raqami ko‘rinadi. Moddalar amaldagi matn bilan avtomatik solishtirilmaydi. Mavzudan tashqari savollarga tayyor rad javobi chiqadi.
- Gemini uchun ishlab chiquvchi sozlamasi `gemini.local.properties` faylida. AI foydalanuvchidan kalit, telefon raqami yoki SMS kodi so‘ramaydi.
- «Advokatga yozish» chat ekranini darhol ochadi. Xabar yuborish mavjud sessiyaga bog‘liq. Vaqt band qilishda Asos API’ning SMS orqali kirish tizimi ishlatiladi.
- Ishonch telefoni raqami avval `GET /v1/help`, so‘ng `GET /v1/hotline` orqali olinadi. Raqam kelmasa, prototipdagi raqam ko‘rsatiladi. Namuna raqamida qo‘ng‘iroq tugmasi nofaol.

Kalit bilan yig‘ilgan APK sinov uchun. Tarqatiladigan versiyada Gemini so‘rovlarini kalit serverda turadigan proksi orqali yuboring.

Brauzerdagi hujjat kirish kodi ilovaga kerak emas. Ilova Asos va Gemini serverlariga HTTPS orqali ulanadi. Server manzilini ixtiyoriy `ASOS_API_BASE_URL` Gradle qiymati bilan boshqa HTTPS serverga yo‘naltirish mumkin. Avval sozlangan mahalliy HTTP qiymati bo‘lsa, uni `gradle.properties` faylidan olib tashlang.

## Huquqiy yordam ekranini boshqa Asos moduliga qo‘shish

Tayyor ilovaning ulanishi `MainActivity.kt`, `AssistantClient.kt`, `AssistantViewModel.kt` va `HelpDialogs.kt` fayllarida. Dizayn uchun shu paketdagi `LegalHelpScreen.kt`, `LegalHelpAction.kt`, `LegalHelpStyle.kt`, `LegalHelpIcon.kt` va `AssistantConversation.kt` fayllari, shuningdek `app/src/main/res/font/` ichidagi shriftlar ishlatiladi.
