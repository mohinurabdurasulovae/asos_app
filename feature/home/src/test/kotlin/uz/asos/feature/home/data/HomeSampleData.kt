package uz.asos.feature.home.data

import uz.asos.core.model.constitution.Topic
import uz.asos.feature.home.data.model.ArticleOfDay
import uz.asos.feature.home.data.model.CaseOfWeek
import uz.asos.feature.home.data.model.CaseStatus
import uz.asos.feature.home.data.model.ContinueLearning
import uz.asos.feature.home.data.model.DailyAnswer
import uz.asos.feature.home.data.model.DailyOption
import uz.asos.feature.home.data.model.DailyQuestion
import uz.asos.feature.home.data.model.HomeFeed
import uz.asos.feature.home.data.model.HomeUser
import uz.asos.feature.home.data.model.PostHighlight
import uz.asos.feature.home.data.model.PostStatus
import uz.asos.feature.home.data.model.TermOfDay

object HomeSampleData {

    val user = HomeUser(name = "Aziza Karimova", initials = "AK", streakDays = 7)

    val articleOfDay = ArticleOfDay(
        number = 33,
        title = "Fikrlash va so‘z erkinligi",
        quote = "Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega.",
        partNo = 1,
        chapterRoman = "VII",
        chapterTitle = "Shaxsiy huquq va erkinliklar",
    )

    val topics = listOf(
        Topic(id = 1, slug = "shaxsiy-huquqlar", title = "Shaxsiy huquqlar", icon = "shield", tone = "brand", articleCount = 11),
        Topic(id = 2, slug = "siyosiy-huquqlar", title = "Siyosiy huquqlar", icon = "vote", tone = "purple", articleCount = 5),
        Topic(id = 3, slug = "ijtimoiy-huquqlar", title = "Ijtimoiy huquqlar", icon = "heart", tone = "teal", articleCount = 13),
        Topic(id = 4, slug = "burchlar", title = "Burchlar", icon = "clipboard", tone = "orange", articleCount = 6),
        Topic(id = 5, slug = "oila-va-yoshlar", title = "Oila va yoshlar", icon = "family", tone = "pink", articleCount = 5),
        Topic(id = 6, slug = "sud-hokimiyati", title = "Sud hokimiyati", icon = "scale", tone = "navy", articleCount = 11),
        Topic(id = 7, slug = "talim", title = "Ta’lim", icon = "cap", tone = "teal", articleCount = 1),
    )

    val continueLearning = ContinueLearning(
        lastArticleNumber = 49,
        chapterRoman = "IX",
        chapterTitle = "Ijtimoiy huquqlar",
        readCount = 8,
        articleCount = 13,
        percent = 62,
        nextArticleNumber = 50,
        nextArticleTitle = "Ta’lim",
    )

    val caseOfWeek = CaseOfWeek(
        number = 14,
        title = "Direktor va telefon: shaxsiy hayot daxlsizligi",
        status = CaseStatus.IN_PROGRESS,
        step = 2,
        percent = 66,
        estMinutes = 6,
    )

    val dailyQuestion = DailyQuestion(
        questionId = 66,
        body = "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?",
        points = 20,
        difficulty = "orta",
        topic = "Shaxsiy huquqlar",
        options = listOf(
            DailyOption(id = 261, label = "A", body = "24 soat", isCorrect = null),
            DailyOption(id = 262, label = "B", body = "48 soat", isCorrect = null),
            DailyOption(id = 263, label = "C", body = "72 soat", isCorrect = null),
            DailyOption(id = 264, label = "D", body = "12 soat", isCorrect = null),
        ),
        answeredOptionId = null,
        isCorrect = null,
        explanation = null,
    )

    val correctAnswer = DailyAnswer(
        isCorrect = true,
        correctOptionId = 262,
        pointsEarned = 20,
        streakDays = 8,
        explanation = "27-moddaga ko‘ra, sud qarorisiz 48 soatdan ortiq ushlab turish mumkin emas.",
    )

    val posts = listOf(
        PostHighlight(
            id = 1,
            title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
            communityTitle = "Uy-joy",
            communityTone = "pink",
            authorName = "Anonim",
            createdAt = "2026-11-20 12:17:55",
            upvotes = 128,
            commentCount = 34,
            status = PostStatus.ANSWERED,
        ),
        PostHighlight(
            id = 2,
            title = "Kontrakt to‘lovi kechiksa, talabani darsga kiritmaslik mumkinmi?",
            communityTitle = "Ta’lim",
            communityTone = "teal",
            authorName = "Jasur Tursunov",
            createdAt = "2026-11-20 09:17:55",
            upvotes = 76,
            commentCount = 19,
            status = PostStatus.AWAITING_LAWYER,
        ),
    )

    val feed = HomeFeed(
        greeting = "Xayrli kech",
        user = user,
        unreadNotifications = 2,
        articleOfDay = articleOfDay,
        termOfDay = TermOfDay(term = "Daxlsizlik", slug = "daxlsizlik"),
        topics = topics,
        continueLearning = continueLearning,
        caseOfWeek = caseOfWeek,
        dailyQuestion = dailyQuestion,
        lawyersOnline = 3,
        posts = posts,
    )

    val guestFeed = feed.copy(user = null, unreadNotifications = 0, continueLearning = null)
}
