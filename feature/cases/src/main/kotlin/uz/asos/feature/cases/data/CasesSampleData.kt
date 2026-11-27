package uz.asos.feature.cases.data

import uz.asos.feature.cases.data.model.ArticleOption
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic
import uz.asos.feature.cases.data.model.LawyerComment

internal object CasesSampleData {
    private val article27 = ArticleOption(
        number = 27,
        title = "Erkinlik va shaxsiy daxlsizlik",
        officialText = "Har kim erkinlik va shaxsiy daxlsizlik huquqiga ega. Hech kim qonunga asoslanmagan holda hibsga olinishi, ushlab turilishi, qamoqqa olinishi, qamoqda saqlanishi yoki uning ozodligi boshqacha tarzda cheklanishi mumkin emas. Shaxsni sudning qarorisiz qirq sakkiz soatdan ortiq muddat ushlab turish mumkin emas.",
        simpleExplanation = "Sizni qonuniy asossiz ushlab turish mumkin emas. Sud qarorisiz ushlab turish muddati 48 soatdan oshmasligi shart."
    )

    private val article31 = ArticleOption(
        number = 31,
        title = "Shaxsiy hayot va yozishmalar siri",
        officialText = "Har bir inson shaxsiy hayotining daxlsizligi, shaxsiy va oilaviy sirga ega bo‘lish, o‘z sha’ni va qadr-qimmatini himoya qilish huquqiga ega. Har kim yozishmalari, telefon orqali so‘zlashuvlari, pochta, elektron va boshqa xabarlari sir saqlanishi huquqiga ega. Ushbu huquqning cheklanishiga faqat qonunga muvofiq va sudning qaroriga asosan yo‘l qo‘yiladi. Hech kim uy-joyga unda yashovchi shaxslarning xohishiga qarshi kirishi mumkin emas.",
        simpleExplanation = "Yozishmalaringiz, telefoningiz va uy-joyingiz qonun bilan himoyalangan. Ruxsatingizsiz (sud qarorisiz) ularga aralashish taqiqlanadi."
    )

    private val article33 = ArticleOption(
        number = 33,
        title = "Fikrlash va so‘z erkinligi",
        officialText = "Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega. Har kim istalgan axborotni izlash, olish va tarqatish huquqiga ega.",
        simpleExplanation = "O‘z fikringizni erkin ifoda etish va qonun bilan taqiqlanmagan axborotni olish huquqingiz bor."
    )

    private val article42 = ArticleOption(
        number = 42,
        title = "Mehnat va xavfsiz sharoitlar",
        officialText = "Har kim munosib mehnat qilish, kasb va faoliyat turini erkin tanlash, xavfsizlik va gigiyena talablariga javob beradigan qulay mehnat sharoitlarida ishlash, hech qanday kamsitishlarsiz va mehnatga haq to‘lashning belgilangan eng kam miqdoridan kam bo‘lmagan tarzda adolatli haq olish huquqiga ega.",
        simpleExplanation = "Ish beruvchi sizga xavfsiz, gigiyena talablariga javob beradigan qulay ish sharoitini yaratib berishi shart."
    )

    private val article50 = ArticleOption(
        number = 50,
        title = "Ta’lim olish huquqi",
        officialText = "Har kim ta’lim olish huquqiga ega. Davlat bepul umumiy o‘rta ta’lim va boshlang‘ich professional ta’lim olishni kafolatlaydi. Ta’lim tashkilotlarida alohida ta’lim ehtiyojlariga ega bo‘lgan bolalar uchun inklyuziv ta’lim va tarbiya ta’minlanadi.",
        simpleExplanation = "Umumiy o‘rta ta’lim bepul. Alohida ehtiyojli bolalar ham oddiy maktablarda tengdoshlari bilan birga (inklyuziv) o‘qish huquqiga ega."
    )

    private val article55 = ArticleOption(
        number = 55,
        title = "Huquqlarni sud orqali himoya qilish",
        officialText = "Har kim o‘z huquq va erkinliklarini qonunda taqiqlanmagan barcha usullar bilan himoya qilishga haqli. Har kimga o‘z huquq va erkinliklarini sud orqali himoya qilish huquqi kafolatlanadi.",
        simpleExplanation = "Huquqingiz buzilsa, sudga va vakolatli organlarga murojaat qilib himoya talab qilishingiz mumkin."
    )

    val allCases: List<CaseItem> = listOf(
        CaseItem(
            id = 14,
            number = 14,
            title = "Direktor va telefon",
            shortDescription = "Maktab direktori darsda olib qo‘yilgan telefondagi o‘quvchi yozishmalarini o‘qib chiqdi. U haqmi?",
            topic = CaseTopic.SHAXSIY_HAYOT,
            difficulty = CaseDifficulty.ORTA,
            estimatedMinutes = 6,
            isFeaturedOfWeek = true,
            initialStatus = CaseStatus.JARAYONDA,
            initialProgressFraction = 0.66f,
            personaInitials = "T",
            personaName = "Temur, 16 yosh",
            personaSubtitle = "10-sinf o‘quvchisi",
            storyText = "Dars paytida Temurning telefoni jiringlab qoldi. O‘qituvchi telefonni olib, direktorga topshirdi. Tanaffusda direktor telefonni ochib, Temurning do‘stlari bilan yozishmalarini o‘qib chiqdi va ba’zilarini ota-onasiga ko‘rsatdi.",
            questionText = "Direktor o‘z vakolati doirasida harakat qildimi?",
            options = listOf(article27, article31, article33, article50),
            correctArticleNumbers = setOf(31),
            rewardPoints = 40,
            sampleSolution = "Direktor telefonni vaqtincha olib qo‘yishi mumkin, lekin yozishmalarni o‘qishga haqqi yo‘q. Bu shaxsiy hayot daxlsizligini buzadi.",
            lawyerComment = LawyerComment(
                initials = "MY",
                fullName = "Malika Yusupova",
                role = "advokat",
                paragraphs = listOf(
                    "31-modda har kimga shaxsiy hayot daxlsizligini, yozishmalar, telefon so‘zlashuvlari va boshqa xabarlar sirini kafolatlaydi. Bu huquqni cheklash faqat qonunga muvofiq va sud qaroriga asosan mumkin.",
                    "Maktab ichki tartibi darsda telefonni vaqtincha olib qo‘yishga ruxsat berishi mumkin. Ammo yozishmalarni o‘qish va ularni boshqalarga ko‘rsatish — butunlay boshqa masala."
                ),
                practicalSteps = listOf(
                    "Voqeani ota-onangizga xotirjam aytib bering.",
                    "Maktab ma’muriyatidan ichki tartib qoidalarini ko‘rsatishni va shaxsiy yozishmalarga kirmaslikni talab qiling.",
                    "Huquqbuzarlik takrorlansa, tuman maktabgacha va maktab ta’limi bo‘limiga yoki Bolalar ombudsmaniga murojaat qiling."
                )
            )
        ),
        CaseItem(
            id = 13,
            number = 13,
            title = "Inklyuziv sinf",
            shortDescription = "Maktab eshitishida muammosi bor qizni qabul qilishdan bosh tortdi.",
            topic = CaseTopic.TALIM,
            difficulty = CaseDifficulty.OSON,
            estimatedMinutes = 4,
            initialStatus = CaseStatus.YECHILGAN,
            initialProgressFraction = 1f,
            personaInitials = "M",
            personaName = "Malika, 8 yosh",
            personaSubtitle = "2-sinfga kiruvchi o‘quvchi",
            storyText = "Malikaning eshitishida qisman muammo bor, lekin u eshitish moslamasi bilan bemalol muloqot qiladi. Mahalladagi umumta’lim maktabi direktori «bizda maxsus sinf yo‘q» deb uni qabul qilishdan bosh tortdi.",
            questionText = "Maktabning rad javobi Konstitutsiyaga muvofiqmi?",
            options = listOf(article50, article33, article42, article55),
            correctArticleNumbers = setOf(50),
            rewardPoints = 30,
            sampleSolution = "Yo‘q, 50-moddaga ko‘ra alohida ta’lim ehtiyojlariga ega bolalar uchun inklyuziv ta’lim kafolatlangan. Maktab sharoit yaratishi shart.",
            lawyerComment = LawyerComment(
                initials = "MY",
                fullName = "Malika Yusupova",
                role = "advokat",
                paragraphs = listOf(
                    "Konstitutsiyaning 50-moddasiga ko‘ra, ta’lim tashkilotlarida alohida ta’lim ehtiyojlariga ega bo‘lgan bolalar uchun inklyuziv ta’lim va tarbiya ta’minlanadi.",
                    "Maktab rahbariyati bolaning nogironligi yoki eshitish qobiliyati tufayli uni qabul qilishdan asossiz bosh tortishga haqli emas."
                ),
                practicalSteps = listOf(
                    "Maktab direktoridan qabul qilishni rad etish sababini yozma ravishda so‘rang.",
                    "Yozma rad javobi bilan tuman ta’lim bo‘limiga va Maktabgacha va maktab ta’limi vazirligi ishonch telefoniga murojaat qiling.",
                    "Zarur bo‘lsa, tibbiy-pedagogik komissiya xulosasi asosida inklyuziv sharoit yaratilishini talab eting."
                )
            )
        ),
        CaseItem(
            id = 12,
            number = 12,
            title = "Qirq sakkiz soat",
            shortDescription = "Akasi ikki kundan beri sud qarorisiz ushlab turilibdi. Oila nima qila oladi?",
            topic = CaseTopic.SUD,
            difficulty = CaseDifficulty.MURAKKAB,
            estimatedMinutes = 8,
            initialStatus = CaseStatus.YANGI,
            initialProgressFraction = 0f,
            personaInitials = "J",
            personaName = "Jasur, 24 yosh",
            personaSubtitle = "Ushlab turilgan shaxsning ukasi",
            storyText = "Jasurning akasini ichki ishlar bo‘limiga olib ketishganiga 52 soat bo‘ldi. Oila a’zolariga hali ham sud qarori ko‘rsatilmagan, advokat bilan uchrashishga esa «tergov tugagach» deb ruxsat berilmayapti.",
            questionText = "Bu holatda qaysi konstitutsiyaviy qoida buzilmoqda?",
            options = listOf(article27, article31, article42, article50),
            correctArticleNumbers = setOf(27),
            rewardPoints = 50,
            lawyerComment = LawyerComment(
                initials = "SR",
                fullName = "Sardor Rahimov",
                role = "advokat",
                paragraphs = listOf(
                    "27-moddaga ko‘ra, shaxsni sud qarorisiz 48 soatdan ortiq muddat ushlab turish qat’iyan taqiqlanadi. Agar bu muddat ichida sud qamoqqa olish to‘g‘risida qaror chiqarmasa, shaxs darhol ozod qilinishi shart.",
                    "Bundan tashqari, shaxs ushlangan paytdan boshlab o‘z huquqlari va malakali yuridik yordam (advokat) olish huquqiga ega."
                ),
                practicalSteps = listOf(
                    "Darhol advokat yollab, ushlab turish bayonnomasi va aniq vaqtini talab qiling.",
                    "48 soat o‘tgani sababli shaxsni zudlik bilan ozod qilish to‘g‘risida prokuraturaga ariza topshiring.",
                    "Noqonuniy ushlab turish holati yuzasidan sudga shikoyat qiling."
                )
            )
        ),
        CaseItem(
            id = 11,
            number = 11,
            title = "Himoya vositasisiz sex",
            shortDescription = "Sex rahbari tikuvchilardan himoya vositalarisiz, shamollatilmagan xonada ishlashni talab qilmoqda.",
            topic = CaseTopic.MEHNAT,
            difficulty = CaseDifficulty.ORTA,
            estimatedMinutes = 5,
            initialStatus = CaseStatus.YANGI,
            initialProgressFraction = 0f,
            personaInitials = "N",
            personaName = "Nilufar, 27 yosh",
            personaSubtitle = "Tikuvchilik sexi xodimasi",
            storyText = "Tikuvchilik sexida havo almashtirish tizimi ishlamaydi, chang va issiqlik kuchli. Xodimlar niqob va shamollatish tizimini so‘raganda, rahbariyat «yoqmasa ishdan ketaveringlar» deb javob berdi.",
            questionText = "Ish beruvchining talabi qonuniymi?",
            options = listOf(article42, article27, article33, article50),
            correctArticleNumbers = setOf(42),
            rewardPoints = 40,
            lawyerComment = LawyerComment(
                initials = "BK",
                fullName = "Bekzod Karimov",
                role = "advokat",
                paragraphs = listOf(
                    "Konstitutsiyaning 42-moddasi har kimga xavfsizlik va gigiyena talablariga javob beradigan qulay mehnat sharoitlarida ishlash huquqini kafolatlaydi.",
                    "Ish beruvchi xodimlarni zarur shaxsiy himoya vositalari bilan ta’minlashi va mehnat muhofazasi qoidalariga rioya qilishi majburiydir."
                ),
                practicalSteps = listOf(
                    "Xodimlar nomidan rahbariyatga ish sharoitlarini yaxshilash bo‘yicha yozma murojaat topshiring.",
                    "Muammo hal etilmasa, Davlat mehnat inspeksiyasiga (ishonch telefoni yoki onlayn) ariza yuboring.",
                    "Mehnat shartnomasi va ish joyidagi holatni tasdiqlovchi dalillarni saqlab qo‘ying."
                )
            )
        ),
        CaseItem(
            id = 10,
            number = 10,
            title = "Kalit nusxasi",
            shortDescription = "Uy egasi ijarachi yo‘qligida kvartiraga kirib, narsalarini ko‘zdan kechirib ketyapti.",
            topic = CaseTopic.UY_JOY,
            difficulty = CaseDifficulty.ORTA,
            estimatedMinutes = 5,
            initialStatus = CaseStatus.YECHILGAN,
            initialProgressFraction = 1f,
            personaInitials = "A",
            personaName = "Aziza, 22 yosh",
            personaSubtitle = "Ijarada yashovchi talaba",
            storyText = "Aziza yozma shartnoma asosida kvartirada ijarada yashaydi. Uy egasi kalit nusxasini o‘zida saqlab qolgan va Aziza o‘qishda bo‘lgan paytda ruxsatsiz uyga kirib, shaxsiy narsalarini ko‘zdan kechirib ketayotgani ma’lum bo‘ldi.",
            questionText = "Uy egasining bu harakati qaysi moddaga zid?",
            options = listOf(article31, article55, article42, article27),
            correctArticleNumbers = setOf(31),
            rewardPoints = 40,
            sampleSolution = "31-modda uy-joy daxlsizligini kafolatlaydi. Ijara davomida kvartiradan qonuniy foydalanuvchi ijarachi hisoblanadi, ruxsatsiz kirish mumkin emas.",
            lawyerComment = LawyerComment(
                initials = "SR",
                fullName = "Sardor Rahimov",
                role = "advokat",
                paragraphs = listOf(
                    "Konstitutsiyaning 31-moddasi uy-joy daxlsizligini kafolatlaydi: qonunda belgilangan holatlardan tashqari, unda yashovchilarning roziligisiz uyga kirish mumkin emas.",
                    "Ijara davomida kvartiradan qonuniy foydalanuvchi — siz. Mulkdor ham oldindan kelishmasdan uyga kirishga haqli emas."
                ),
                practicalSteps = listOf(
                    "Uy egasiga yozma ogohlantirish yuboring va tashrif tartibini shartnomaga qo‘shishni taklif qiling.",
                    "Qulfni almashtirishdan oldin shartnoma bandlarini tekshiring.",
                    "Ruxsatsiz kirish takrorlansa, ichki ishlar organlariga yoki sudga (55-modda) murojaat qiling."
                )
            )
        )
    )
}
