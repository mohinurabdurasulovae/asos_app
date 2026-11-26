/*
 * ConstitutionMappers.kt
 * Module: :core:data   ·   Work package: WP-02
 *
 * DTO → core:model mappers for the Constitution endpoints.
 */
package uz.asos.core.data.mapper

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
import uz.asos.core.model.constitution.ReadProgressResult
import uz.asos.core.model.constitution.ReadingOverview
import uz.asos.core.model.constitution.ReadingProgress
import uz.asos.core.model.constitution.ReadingStatus
import uz.asos.core.model.constitution.RelatedArticle
import uz.asos.core.model.constitution.Section
import uz.asos.core.model.constitution.SectionRef
import uz.asos.core.model.constitution.Topic
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.core.network.dto.ArticleBriefDto
import uz.asos.core.network.dto.ArticleDto
import uz.asos.core.network.dto.ArticleProgressResponseDto
import uz.asos.core.network.dto.ChapterDto
import uz.asos.core.network.dto.ConstitutionResponseDto
import uz.asos.core.network.dto.ReadingOverviewDto
import uz.asos.core.network.dto.ReadingProgressDto
import uz.asos.core.network.dto.SectionDto
import uz.asos.core.network.dto.TermBriefDto
import uz.asos.core.network.dto.TopicDto

/** Chapter.status: not_started | in_progress | done. */
internal fun chapterStatus(value: String): ReadingStatus = when (value) {
    "done" -> ReadingStatus.READ
    "in_progress" -> ReadingStatus.IN_PROGRESS
    else -> ReadingStatus.NOT_STARTED
}

/** ReadingProgress.status: reading | read. */
internal fun ReadingProgressDto.asModel() = ReadingProgress(
    status = if (status == "read") ReadingStatus.READ else ReadingStatus.IN_PROGRESS,
    seconds = seconds,
    lastReadAt = lastReadAt,
)

internal fun ConstitutionResponseDto.asModel() = ConstitutionTree(
    info = ConstitutionInfo(
        edition = stats.edition,
        sections = stats.sections,
        chapters = stats.chapters,
        articles = stats.articles,
        source = stats.source,
    ),
    sections = sections.map { it.asModel() },
)

internal fun SectionDto.asModel() = Section(
    id = id,
    roman = roman,
    title = title,
    articleFrom = articleFrom,
    articleTo = articleTo,
    chapterCount = chapterCount,
    articleCount = articleCount,
    readCount = readCount,
    percent = percent,
    chapters = chapters.map { it.asModel() },
)

internal fun ChapterDto.asModel() = Chapter(
    id = id,
    roman = roman,
    title = title,
    articleFrom = articleFrom,
    articleTo = articleTo,
    articleCount = articleCount,
    readCount = readCount,
    percent = percent,
    status = chapterStatus(status),
)

internal fun TopicDto.asModel() = Topic(
    id = id,
    slug = slug,
    title = title,
    icon = icon,
    tone = tone,
    articleCount = articleCount,
)

internal fun ArticleBriefDto.asModel() = ArticleBrief(
    id = id,
    number = number,
    title = title,
    titleAuto = titleAuto,
    slug = slug,
    chapterRoman = chapterRoman,
    chapterTitle = chapterTitle,
    readingMinutes = readingMinutes,
    isPreamble = isPreamble,
    excerpt = excerpt,
    progress = progress?.asModel(),
)

internal fun TermBriefDto.asModel() = TermSummary(
    id = id,
    term = term,
    slug = slug,
    letter = letter,
    shortDefinition = shortDef,
    articleNumbers = articles,
    isSaved = isSaved,
)

internal fun ArticleDto.asModel() = Article(
    id = id,
    number = number,
    title = title,
    titleAuto = titleAuto,
    slug = slug,
    readingMinutes = readingMinutes,
    effectiveFrom = effectiveFrom,
    isPreamble = isPreamble,
    section = section?.let { SectionRef(roman = it.roman, title = it.title) },
    chapter = chapter?.let { ChapterRef(id = it.id, roman = it.roman, title = it.title) },
    topics = topics.map { ArticleTopic(slug = it.slug, title = it.title, tone = it.tone) },
    parts = parts.sortedBy { it.partNo }.map {
        ArticlePart(partNo = it.partNo, body = it.body, highlights = it.highlight, isExtra = it.isExtra)
    }.ifEmpty {
        // No `parts` (e.g. the preamble): show `text` as a single block.
        text?.takeIf { it.isNotBlank() }
            ?.let { listOf(ArticlePart(partNo = 1, body = it.trim(), highlights = emptyList(), isExtra = false)) }
            .orEmpty()
    },
    summary = plain?.takeIf { it.bullets.isNotEmpty() }?.let {
        ArticleSummary(
            bullets = it.bullets,
            reviewedBy = it.reviewedBy,
            reviewedAt = it.reviewedAt,
            disclaimer = it.disclaimer,
        )
    },
    examples = examples.map {
        ArticleExample(
            id = it.id,
            situation = it.situation,
            verdict = it.verdict,
            isLawful = it.verdictKind != "bad",
        )
    },
    related = related.map { RelatedArticle(number = it.number, title = it.title, slug = it.slug, note = it.note) },
    terms = terms.map { it.asModel() },
    discussion = discussion?.let { ArticleDiscussion(count = it.count, lastPostId = it.lastId, lastTitle = it.lastTitle) },
    quiz = quiz?.takeIf { it.slug.isNotBlank() || it.id != 0 }?.let { QuizRef(id = it.id, slug = it.slug, title = it.title) },
    previousNumber = prev,
    nextNumber = next,
    progress = progress?.asModel(),
    isSaved = isSaved,
    source = source,
)

internal fun ReadingOverviewDto.asModel(): ReadingOverview {
    val chapter = continueReading?.chapter
    val last = continueReading?.lastArticle?.takeIf { it.number > 0 }
    return ReadingOverview(
        articlesTotal = articlesTotal,
        articlesRead = articlesRead,
        percent = percent,
        continueReading = last?.let {
            ContinueReading(
                articleNumber = it.number,
                articleTitle = it.title,
                articleSlug = it.slug,
                readingMinutes = it.readingMinutes,
                minutesLeft = it.minutesLeft,
                chapterRoman = chapter?.roman,
                chapterTitle = chapter?.title,
            )
        },
    )
}

internal fun ArticleProgressResponseDto.asModel() = ReadProgressResult(
    pointsEarned = pointsEarned,
    totalPoints = points,
    streakDays = streakDays,
)
