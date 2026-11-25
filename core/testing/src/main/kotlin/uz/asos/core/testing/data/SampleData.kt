/*
 * SampleData.kt
 * Module: :core:testing   ·   Work package: WP-02
 *
 * Sample content from the design for tests: the Constitution tree (6 sections, 27 chapters,
 * 155 articles) and 50-modda. Chapter titles and article ranges are from the 2023 text.
 * Still to add: Kazus № 14, lawyers, posts.
 */
package uz.asos.core.testing.data

import uz.asos.core.model.constitution.Article
import uz.asos.core.model.constitution.ArticleBrief
import uz.asos.core.model.constitution.ArticleDiscussion
import uz.asos.core.model.constitution.ArticleExample
import uz.asos.core.model.constitution.ArticlePart
import uz.asos.core.model.constitution.ArticleSummary
import uz.asos.core.model.constitution.ArticleTopic
import uz.asos.core.model.constitution.Chapter
import uz.asos.core.model.constitution.ChapterRef
import uz.asos.core.model.constitution.ConstitutionInfo
import uz.asos.core.model.constitution.ConstitutionTree
import uz.asos.core.model.constitution.ContinueReading
import uz.asos.core.model.constitution.QuizRef
import uz.asos.core.model.constitution.ReadingOverview
import uz.asos.core.model.constitution.ReadingProgress
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.RelatedArticle
import uz.asos.core.model.constitution.Section
import uz.asos.core.model.constitution.SectionRef
import uz.asos.core.model.constitution.Topic
import uz.asos.core.model.dictionary.TermSummary

object SampleData {

    private fun chapter(
        id: Int,
        roman: String,
        title: String,
        from: Int,
        to: Int,
        read: Int = 0,
    ): Chapter {
        val count = to - from + 1
        val percent = read * 100 / count
        return Chapter(
            id = id,
            roman = roman,
            title = title,
            articleFrom = from,
            articleTo = to,
            articleCount = count,
            readCount = read,
            percent = percent,
            status = when {
                read == 0 -> ReadingStatus.NOT_STARTED
                read >= count -> ReadingStatus.READ
                else -> ReadingStatus.IN_PROGRESS
            },
        )
    }

    private fun section(id: Int, roman: String, title: String, from: Int, to: Int, chapters: List<Chapter>) = Section(
        id = id,
        roman = roman,
        title = title,
        articleFrom = from,
        articleTo = to,
        chapterCount = chapters.size,
        articleCount = to - from + 1,
        readCount = chapters.sumOf { it.readCount },
        percent = 0,
        chapters = chapters,
    )

    val constitutionTree = ConstitutionTree(
        info = ConstitutionInfo(edition = "2023", sections = 6, chapters = 27, articles = 155, source = "lex.uz"),
        sections = listOf(
            section(
                1, "I", "Asosiy prinsiplar", 1, 18,
                listOf(
                    chapter(1, "I", "Davlat suvereniteti", 1, 6),
                    chapter(2, "II", "Xalq hokimiyatchiligi", 7, 14),
                    chapter(3, "III", "Konstitutsiya va qonunning ustunligi", 15, 16),
                    chapter(4, "IV", "Tashqi siyosat", 17, 18),
                ),
            ),
            section(
                2, "II", "Inson va fuqaroning asosiy huquqlari, erkinliklari va burchlari", 19, 64,
                listOf(
                    chapter(5, "V", "Umumiy qoidalar", 19, 21, read = 3),
                    chapter(6, "VI", "Fuqarolik", 22, 24, read = 3),
                    chapter(7, "VII", "Shaxsiy huquq va erkinliklar", 25, 35, read = 11),
                    chapter(8, "VIII", "Siyosiy huquqlar", 36, 40),
                    chapter(9, "IX", "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar", 41, 53, read = 8),
                    chapter(10, "X", "Inson hamda fuqaroning huquq va erkinliklari kafolatlari", 54, 58),
                    chapter(11, "XI", "Fuqarolarning burchlari", 59, 64),
                ),
            ),
            section(
                3, "III", "Jamiyat va shaxs", 65, 82,
                listOf(
                    chapter(12, "XII", "Jamiyatning iqtisodiy negizlari", 65, 68),
                    chapter(13, "XIII", "Fuqarolik jamiyati institutlari", 69, 75),
                    chapter(14, "XIV", "Oila, bolalar va yoshlar", 76, 80),
                    chapter(15, "XV", "Ommaviy axborot vositalari", 81, 82),
                ),
            ),
            section(
                4, "IV", "Ma’muriy-hududiy va davlat tuzilishi", 83, 90,
                listOf(
                    chapter(16, "XVI", "O‘zbekiston Respublikasining ma’muriy-hududiy tuzilishi", 83, 84),
                    chapter(17, "XVII", "Qoraqalpog‘iston Respublikasi", 85, 90),
                ),
            ),
            section(
                5, "V", "Davlat hokimiyatining tashkil etilishi", 91, 153,
                listOf(
                    chapter(18, "XVIII", "O‘zbekiston Respublikasi Oliy Majlisi", 91, 104),
                    chapter(19, "XIX", "O‘zbekiston Respublikasining Prezidenti", 105, 113),
                    chapter(20, "XX", "O‘zbekiston Respublikasi Vazirlar Mahkamasi", 114, 119),
                    chapter(21, "XXI", "Mahalliy davlat hokimiyati asoslari. Fuqarolarning o‘zini o‘zi boshqarish organlari", 120, 127),
                    chapter(22, "XXII", "Saylov tizimi", 128, 129),
                    chapter(23, "XXIII", "Sud hokimiyati", 130, 140),
                    chapter(24, "XXIV", "Advokatura", 141, 142),
                    chapter(25, "XXV", "Prokuratura", 143, 146),
                    chapter(26, "XXVI", "Moliya, pul va bank tizimi", 147, 151),
                    chapter(27, "XXVII", "Mudofaa va xavfsizlik", 152, 153),
                ),
            ),
            section(6, "VI", "Konstitutsiyani o‘zgartirish tartibi", 154, 155, emptyList()),
        ),
    )

    val topics = listOf(
        Topic(id = 1, slug = "huquqlar", title = "Huquqlar", icon = null, tone = "brand", articleCount = 24),
        Topic(id = 2, slug = "erkinliklar", title = "Erkinliklar", icon = null, tone = "purple", articleCount = 11),
        Topic(id = 3, slug = "burchlar", title = "Burchlar", icon = null, tone = "orange", articleCount = 6),
        Topic(id = 4, slug = "talim", title = "Ta’lim", icon = null, tone = "teal", articleCount = 3),
    )

    val continueReading = ContinueReading(
        articleNumber = 50,
        articleTitle = "Ta’lim olish huquqi",
        articleSlug = "50-modda",
        readingMinutes = 4,
        minutesLeft = 3,
        chapterRoman = "IX",
        chapterTitle = "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar",
    )

    val readingOverview = ReadingOverview(
        articlesTotal = 155,
        articlesRead = 59,
        percent = 38,
        continueReading = continueReading,
    )

    fun articleBrief(number: Int, title: String = "$number-modda", titleAuto: Boolean = true, excerpt: String? = null) =
        ArticleBrief(
            id = number,
            number = number,
            title = title,
            titleAuto = titleAuto,
            slug = "$number-modda",
            chapterRoman = "IX",
            chapterTitle = "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar",
            readingMinutes = 2,
            isPreamble = false,
            excerpt = excerpt,
            progress = null,
        )

    val chapterIXArticles: List<ArticleBrief> = (41..53).map { number ->
        if (number == 50) {
            articleBrief(50, title = "Ta’lim olish huquqi", titleAuto = false, excerpt = "Har kim ta’lim olish huquqiga ega.")
        } else {
            articleBrief(number)
        }
    }

    /** 50-modda. Parts 1–3 are the official text; the rest of the article is cut for tests. */
    val article50 = Article(
        id = 50,
        number = 50,
        title = "Ta’lim olish huquqi",
        titleAuto = false,
        slug = "50-modda",
        readingMinutes = 4,
        effectiveFrom = "2023-05-01",
        isPreamble = false,
        section = SectionRef(roman = "II", title = "Inson va fuqaroning asosiy huquqlari, erkinliklari va burchlari"),
        chapter = ChapterRef(id = 9, roman = "IX", title = "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar"),
        topics = listOf(ArticleTopic(slug = "talim", title = "Ta’lim", tone = "teal")),
        parts = listOf(
            ArticlePart(1, "Har kim ta’lim olish huquqiga ega.", emptyList(), isExtra = false),
            ArticlePart(
                2,
                "Davlat uzluksiz ta’lim tizimi, uning har xil turlari va shakllari, davlat va nodavlat ta’lim tashkilotlari rivojlanishini ta’minlaydi.",
                listOf("uzluksiz ta’lim tizimi"),
                isExtra = false,
            ),
            ArticlePart(3, "Davlat maktabgacha ta’lim va tarbiyani rivojlantirish uchun shart-sharoitlar yaratadi.", emptyList(), isExtra = false),
        ),
        summary = ArticleSummary(
            bullets = listOf(
                "O‘qish — har bir insonning huquqi.",
                "Umumiy o‘rta ta’lim bepul — maktabda o‘qish uchun pul talab qilinmaydi.",
            ),
            reviewedBy = null,
            reviewedAt = null,
            disclaimer = "Izohni huquqshunos tekshirgan · rasmiy matn o‘rnini bosmaydi",
        ),
        examples = listOf(
            ArticleExample(
                id = 1,
                situation = "Nodirning ota-onasi maktabga “homiylik puli” bermagani uchun uni darsdan chetlatishmoqchi.",
                verdict = "Umumiy o‘rta ta’lim bepul. To‘lov qilmagani uchun o‘quvchini darsdan chetlatish 50-moddaga zid.",
                isLawful = false,
            ),
        ),
        related = listOf(
            RelatedArticle(number = 19, title = "Qonun oldida tenglik", slug = "19-modda", note = null),
            RelatedArticle(number = 42, title = "Mehnat va kasb tanlash", slug = "42-modda", note = null),
            RelatedArticle(number = 55, title = "Huquqlarni himoya qilish", slug = "55-modda", note = null),
        ),
        terms = listOf(
            TermSummary(id = 1, term = "Inklyuziv ta’lim", slug = "inklyuziv-talim", letter = "I", shortDefinition = null, articleNumbers = listOf(50), isSaved = false),
        ),
        discussion = ArticleDiscussion(count = 24, lastPostId = 2, lastTitle = "Kontrakt to‘lovi kechiksa, darsga kiritmaslik mumkinmi?"),
        quiz = QuizRef(id = 2, slug = "ijtimoiy-huquqlar", title = "Ijtimoiy huquqlar"),
        previousNumber = 49,
        nextNumber = 51,
        progress = ReadingProgress(status = ReadingStatus.IN_PROGRESS, seconds = 40, lastReadAt = null),
        isSaved = false,
        source = null,
    )
}
