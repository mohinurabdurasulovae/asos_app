package uz.asos.feature.home.data

import uz.asos.core.model.constitution.Topic
import uz.asos.core.network.dto.ContinueDto
import uz.asos.core.network.dto.TopicDto
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
import uz.asos.feature.home.data.network.ArticleOfDayDto
import uz.asos.feature.home.data.network.DailyAnswerResponseDto
import uz.asos.feature.home.data.network.DailyQuestionDto
import uz.asos.feature.home.data.network.HomeCaseDto
import uz.asos.feature.home.data.network.HomePostDto
import uz.asos.feature.home.data.network.HomeResponseDto
import uz.asos.feature.home.data.network.HomeUserDto
internal fun HomeResponseDto.asModel() = HomeFeed(
    greeting = greeting,
    user = user?.asHomeUser(),
    unreadNotifications = unreadNotifications,
    articleOfDay = articleOfDay?.takeIf { it.number > 0 }?.asModel(),
    termOfDay = termOfDay?.takeIf { it.term.isNotBlank() }?.let { TermOfDay(term = it.term, slug = it.slug) },
    topics = topics.map { it.asModel() },
    continueLearning = continueOverview?.continueReading?.asContinueLearning(),
    caseOfWeek = caseOfWeek?.takeIf { it.number > 0 }?.asModel(),
    dailyQuestion = dailyQuestion?.takeIf { it.questionId > 0 }?.asModel(),
    lawyersOnline = help.lawyersOnline,
    posts = community.items.map { it.asModel() },
)

internal fun HomeUserDto.asHomeUser() = HomeUser(
    name = name,
    initials = initials,
    streakDays = streakDays,
)

internal fun ArticleOfDayDto.asModel() = ArticleOfDay(
    number = number,
    title = title,
    quote = quote,
    partNo = partNo,
    chapterRoman = chapter?.roman?.takeIf { it.isNotBlank() },
    chapterTitle = chapter?.title?.takeIf { it.isNotBlank() },
)

internal fun ContinueDto.asContinueLearning(): ContinueLearning? {
    val last = lastArticle?.takeIf { it.number > 0 } ?: return null
    return ContinueLearning(
        lastArticleNumber = last.number,
        chapterRoman = chapter?.roman?.takeIf { it.isNotBlank() },
        chapterTitle = chapter?.title?.takeIf { it.isNotBlank() },
        readCount = chapter?.readCount ?: 0,
        articleCount = chapter?.articleCount ?: 0,
        percent = chapter?.percent ?: 0,
        nextArticleNumber = nextArticle?.number?.takeIf { it > 0 },
        nextArticleTitle = nextArticle?.title?.takeIf { it.isNotBlank() },
    )
}

internal fun HomeCaseDto.asModel() = CaseOfWeek(
    number = number,
    title = title,
    status = when (myStatus) {
        "in_progress" -> CaseStatus.IN_PROGRESS
        "solved" -> CaseStatus.SOLVED
        else -> CaseStatus.NEW
    },
    step = myStep,
    percent = myPercent,
    estMinutes = estMinutes,
)

internal fun DailyQuestionDto.asModel() = DailyQuestion(
    questionId = questionId,
    body = body,
    points = points,
    difficulty = difficulty,
    topic = topic,
    options = options.map { DailyOption(id = it.id, label = it.label, body = it.body, isCorrect = it.isCorrect) },
    answeredOptionId = if (answered) myOptionId else null,
    isCorrect = isCorrect,
    explanation = explanation,
)

internal fun DailyAnswerResponseDto.asModel() = DailyAnswer(
    isCorrect = isCorrect,
    correctOptionId = correctOptionId,
    pointsEarned = pointsEarned,
    streakDays = streakDays,
    explanation = explanation,
)

internal fun HomePostDto.asModel() = PostHighlight(
    id = id,
    title = title,
    communityTitle = community?.title?.takeIf { it.isNotBlank() },
    communityTone = community?.tone,
    authorName = author.name,
    createdAt = createdAt,
    upvotes = upvotes,
    commentCount = commentCount,
    status = when (status) {
        "answered" -> PostStatus.ANSWERED
        "awaiting_lawyer" -> PostStatus.AWAITING_LAWYER
        else -> PostStatus.OPEN
    },
)

internal fun TopicDto.asModel() = Topic(
    id = id,
    slug = slug,
    title = title,
    icon = icon,
    tone = tone,
    articleCount = articleCount,
)
