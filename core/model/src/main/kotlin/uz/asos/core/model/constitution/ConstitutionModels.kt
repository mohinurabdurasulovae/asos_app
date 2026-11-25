/*
 * ConstitutionModels.kt
 * Module: :core:model   ·   Work package: WP-02
 *
 * Domain models for the Constitution, shared by every layer. Field meanings follow
 * docs/api/openapi.yaml (schemas ConstitutionStats, Section, Chapter, ArticleBrief, Article,
 * Topic, ReadingOverview).
 */
package uz.asos.core.model.constitution

import uz.asos.core.model.dictionary.TermSummary

/** Edition 2023: 6 sections, 27 chapters, 155 articles (the preamble is not counted). */
data class ConstitutionInfo(
    val edition: String,
    val sections: Int,
    val chapters: Int,
    val articles: Int,
    val source: String?,
)

enum class ReadingStatus { NOT_STARTED, IN_PROGRESS, READ }

/** Bo‘lim. Section VI has no chapters: articles 154–155 belong to it directly. */
data class Section(
    val id: Int,
    val roman: String,
    val title: String,
    val articleFrom: Int,
    val articleTo: Int,
    val chapterCount: Int,
    val articleCount: Int,
    val readCount: Int,
    val percent: Int,
    val chapters: List<Chapter>,
)

/** Bob. */
data class Chapter(
    val id: Int,
    val roman: String,
    val title: String,
    val articleFrom: Int,
    val articleTo: Int,
    val articleCount: Int,
    val readCount: Int,
    val percent: Int,
    val status: ReadingStatus,
)

data class ConstitutionTree(
    val info: ConstitutionInfo,
    val sections: List<Section>,
)

/** Mavzu (topic). [tone] is a design-system colour name: brand, teal, orange, purple, pink, navy. */
data class Topic(
    val id: Int,
    val slug: String,
    val title: String,
    val icon: String?,
    val tone: String?,
    val articleCount: Int,
)

/** Short reference to an article (number + title), used in links. */
data class ArticleRef(
    val number: Int,
    val title: String,
    val slug: String,
)

data class ReadingProgress(
    val status: ReadingStatus,
    val seconds: Int,
    val lastReadAt: String?,
)

/**
 * Article in a list. When [titleAuto] is true the article has no editorial title and [title]
 * is just "130-modda": show [excerpt] instead.
 */
data class ArticleBrief(
    val id: Int,
    val number: Int,
    val title: String,
    val titleAuto: Boolean,
    val slug: String,
    val chapterRoman: String?,
    val chapterTitle: String?,
    val readingMinutes: Int,
    val isPreamble: Boolean,
    val excerpt: String?,
    val progress: ReadingProgress?,
)

/** One numbered part (band) of the official text. */
data class ArticlePart(
    val partNo: Int,
    val body: String,
    val highlights: List<String>,
    val isExtra: Boolean,
)

/** "Sodda qilib aytganda": editorial plain-language summary, not official text. */
data class ArticleSummary(
    val bullets: List<String>,
    val reviewedBy: String?,
    val reviewedAt: String?,
    val disclaimer: String?,
)

data class ArticleExample(
    val id: Int,
    val situation: String,
    val verdict: String,
    /** true = the situation is lawful (green), false = it breaks the article (red). */
    val isLawful: Boolean,
)

data class RelatedArticle(
    val number: Int,
    val title: String,
    val slug: String,
    val note: String?,
)

data class ArticleTopic(
    val slug: String,
    val title: String,
    val tone: String?,
)

data class ArticleDiscussion(
    val count: Int,
    val lastPostId: Int?,
    val lastTitle: String?,
)

data class QuizRef(
    val id: Int,
    val slug: String,
    val title: String,
)

data class SectionRef(val roman: String, val title: String)

data class ChapterRef(val id: Int, val roman: String, val title: String)

/** Everything the article reader (Modda) shows. */
data class Article(
    val id: Int,
    val number: Int,
    val title: String,
    val titleAuto: Boolean,
    val slug: String,
    val readingMinutes: Int,
    val effectiveFrom: String?,
    val isPreamble: Boolean,
    val section: SectionRef?,
    val chapter: ChapterRef?,
    val topics: List<ArticleTopic>,
    val parts: List<ArticlePart>,
    val summary: ArticleSummary?,
    val examples: List<ArticleExample>,
    val related: List<RelatedArticle>,
    val terms: List<TermSummary>,
    val discussion: ArticleDiscussion?,
    val quiz: QuizRef?,
    val previousNumber: Int?,
    val nextNumber: Int?,
    val progress: ReadingProgress?,
    val isSaved: Boolean,
    val source: String?,
)

/** "Davom ettiring": where the signed-in user stopped reading. */
data class ReadingOverview(
    val articlesTotal: Int,
    val articlesRead: Int,
    val percent: Int,
    val continueReading: ContinueReading?,
)

data class ContinueReading(
    val articleNumber: Int,
    val articleTitle: String,
    val articleSlug: String,
    val readingMinutes: Int,
    val minutesLeft: Int,
    val chapterRoman: String?,
    val chapterTitle: String?,
)

/** Result of POST /v1/articles/{key}/progress. */
data class ReadProgressResult(
    val pointsEarned: Int,
    val totalPoints: Int,
    val streakDays: Int,
)

/** Filters for GET /v1/articles. */
data class ArticleQuery(
    val topic: String? = null,
    val chapter: String? = null,
    val section: String? = null,
    val text: String? = null,
    val savedOnly: Boolean = false,
)
