package uz.asos.core.testing.data

import uz.asos.core.model.community.ArticleRef
import uz.asos.core.model.community.Author
import uz.asos.core.model.community.BestAnswerPreview
import uz.asos.core.model.community.Comment
import uz.asos.core.model.community.CommunityTopic
import uz.asos.core.model.community.LawyerBadge
import uz.asos.core.model.community.Post
import uz.asos.core.model.community.PostDetail
import uz.asos.core.model.community.PostStatus
import uz.asos.core.model.community.PostsPage
import uz.asos.core.model.community.VoteState

object CommunitySampleData {

    val anonymous = Author(
        id = null,
        name = "Anonim",
        username = null,
        initials = "A",
        avatarUrl = null,
        isAnonymous = true,
        isLawyer = false,
        lawyerId = null,
        isVerified = false,
    )

    val sardor = Author(
        id = 6,
        name = "Sardor Rahimov",
        username = "sardor.advokat",
        initials = "SR",
        avatarUrl = null,
        isAnonymous = false,
        isLawyer = true,
        lawyerId = 1,
        isVerified = true,
    )

    val kamola = Author(
        id = 8,
        name = "kamola_88",
        username = "kamola_88",
        initials = "KA",
        avatarUrl = null,
        isAnonymous = false,
        isLawyer = false,
        lawyerId = null,
        isVerified = false,
    )

    val uyJoy = CommunityTopic(
        slug = "uy-joy",
        title = "Uy-joy",
        tone = "pink",
        memberCount = 2400,
        isMember = false,
    )

    val communities = listOf(
        uyJoy,
        CommunityTopic("talim", "Ta’lim", "teal", 3120, false),
        CommunityTopic("mehnat", "Mehnat", "orange", 1870, false),
        CommunityTopic("oila", "Oila", "purple", 1540, false),
        CommunityTopic("sud", "Sud", "navy", 980, false),
    )

    val article31 = ArticleRef(
        number = 31,
        title = "Shaxsiy hayot va uy-joy daxlsizligi",
        slug = "31-modda-shaxsiy-hayot-va-uy-joy-daxlsizligi",
    )

    val rentPost = Post(
        id = 1,
        title = "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?",
        excerpt = "Ijarada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men " +
            "yo‘qligimda kirib, narsalarimni ko‘rib ketyapti.",
        tags = listOf("ijara", "uy-joy daxlsizligi", "31-modda"),
        upvotes = 128,
        commentCount = 34,
        status = PostStatus.ANSWERED,
        createdAt = "2026-11-20 12:17:55",
        author = anonymous,
        community = uyJoy,
        article = article31,
        bestAnswer = BestAnswerPreview(
            id = 1,
            excerpt = "31-modda uy-joy daxlsizligini kafolatlaydi. Avval uy egasiga yozma " +
                "ogohlantirish yuboring…",
            isLawyer = true,
            authorName = "Sardor Rahimov",
        ),
        myVote = VoteState.UP,
        isSaved = false,
    )

    val posts = listOf(
        rentPost,
        Post(
            id = 2,
            title = "Kontrakt to‘lovi kechiksa, talabani darsga kiritmaslik mumkinmi?",
            excerpt = "To‘lovning ikkinchi qismini ikki haftaga kechiktirdim. Dekanat " +
                "imtihonlarga kiritmasligini aytdi.",
            tags = listOf("talim", "kontrakt"),
            upvotes = 76,
            commentCount = 19,
            status = PostStatus.AWAITING_LAWYER,
            createdAt = "2026-11-20 09:00:00",
            author = Author(9, "Jasur T.", "jasur_t", "JT", null, false, false, null, false),
            community = communities[1],
            article = null,
            bestAnswer = null,
            myVote = VoteState.NONE,
            isSaved = false,
        ),
        Post(
            id = 3,
            title = "16 yoshda yozgi ishga rasmiy kirish mumkinmi?",
            excerpt = "Yozda kafeda ishlamoqchiman. Ish beruvchi “shartnoma shart emas” deyapti.",
            tags = listOf("mehnat", "yoshlar"),
            upvotes = 54,
            commentCount = 11,
            status = PostStatus.ANSWERED,
            createdAt = "2026-11-19 18:30:00",
            author = Author(10, "Nilufar K.", "nilufar_k", "NK", null, false, false, null, false),
            community = communities[2],
            article = null,
            bestAnswer = null,
            myVote = VoteState.NONE,
            isSaved = true,
        ),
        Post(
            id = 4,
            title = "19 yoshdaman, ota-onam pasportimni berishmayapti",
            excerpt = "Boshqa shaharda o‘qishga kirmoqchiman, lekin hujjatlarimni " +
                "“xavfsizlik uchun” olib qo‘yishgan.",
            tags = listOf("oila", "hujjat"),
            upvotes = 41,
            commentCount = 23,
            status = PostStatus.OPEN,
            createdAt = "2026-11-18 10:05:00",
            author = anonymous,
            community = communities[3],
            article = null,
            bestAnswer = null,
            myVote = VoteState.NONE,
            isSaved = false,
        ),
    )

    val lawyerAnswer = Comment(
        id = 1,
        parentId = null,
        body = "Konstitutsiyaning 31-moddasi uy-joy daxlsizligini kafolatlaydi: qonunda " +
            "belgilangan holatlardan tashqari, unda yashovchilarning roziligisiz uyga kirish " +
            "mumkin emas. Ijara davomida kvartiradan qonuniy foydalanuvchi — siz.",
        isDeleted = false,
        upvotes = 96,
        isBest = true,
        isLawyer = true,
        lawyer = LawyerBadge(specialties = listOf("uy-joy", "fuqarolik"), years = 9),
        replyCount = 0,
        createdAt = "2026-11-20 13:17:55",
        author = sardor,
        article = article31,
        isMine = false,
        replies = emptyList(),
    )

    val threadedComment = Comment(
        id = 2,
        parentId = null,
        body = "Menda ham shunday bo‘lgan. Qulfni almashtirib, uy egasiga xabar berdim — " +
            "muammo hal bo‘ldi.",
        isDeleted = false,
        upvotes = 24,
        isBest = false,
        isLawyer = false,
        lawyer = null,
        replyCount = 1,
        createdAt = "2026-11-20 14:00:00",
        author = kamola,
        article = null,
        isMine = false,
        replies = listOf(
            Comment(
                id = 3,
                parentId = 2,
                body = "Qulfni almashtirishdan oldin shartnomani tekshiring — ba’zi " +
                    "shartnomalarda bu uy egasining roziligi bilan qilinadi.",
                isDeleted = false,
                upvotes = 31,
                isBest = false,
                isLawyer = true,
                lawyer = LawyerBadge(specialties = listOf("uy-joy"), years = 9),
                replyCount = 0,
                createdAt = "2026-11-20 14:40:00",
                author = sardor,
                article = null,
                isMine = false,
                replies = emptyList(),
            ),
        ),
    )

    val rentPostDetail = PostDetail(
        post = rentPost,
        body = "Ijaradagi kvartirada yashayman. Uy egasi kalit nusxasini saqlab qolgan va men " +
            "yo‘qligimda kirib, narsalarimni ko‘rib ketyapti. Shartnomada bu haqda hech narsa " +
            "yozilmagan. Nima qilsam bo‘ladi?",
        isMine = false,
        disclaimer = "Bu umumiy ma’lumot. Aniq vaziyatingiz bo‘yicha shaxsiy maslahat oling.",
        comments = listOf(lawyerAnswer, threadedComment),
    )

    val feed = PostsPage(
        posts = posts,
        communities = communities,
        page = 1,
        total = posts.size,
        hasMore = false,
    )
}
