package uz.asos.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConstitutionResponseDto(
    val ok: Boolean = true,
    val stats: ConstitutionStatsDto = ConstitutionStatsDto(),
    val sections: List<SectionDto> = emptyList(),
)

@Serializable
data class ConstitutionStatsDto(
    val edition: String = "2023",
    val sections: Int = 0,
    val chapters: Int = 0,
    val articles: Int = 0,
    val source: String? = null,
)

@Serializable
data class SectionDto(
    val id: Int = 0,
    val roman: String = "",
    val title: String = "",
    val articleFrom: Int = 0,
    val articleTo: Int = 0,
    val chapterCount: Int = 0,
    val articleCount: Int = 0,
    val readCount: Int = 0,
    val percent: Int = 0,
    val chapters: List<ChapterDto> = emptyList(),
)

@Serializable
data class ChapterDto(
    val id: Int = 0,
    val roman: String = "",
    val title: String = "",
    val articleFrom: Int = 0,
    val articleTo: Int = 0,
    val articleCount: Int = 0,
    val readCount: Int = 0,
    val percent: Int = 0,
    val status: String = "not_started",
)

@Serializable
data class TopicsResponseDto(
    val ok: Boolean = true,
    val items: List<TopicDto> = emptyList(),
)

@Serializable
data class TopicDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
    val icon: String? = null,
    val tone: String? = null,
    val articleCount: Int = 0,
)

@Serializable
data class ChapterResponseDto(
    val ok: Boolean = true,
    val chapter: ChapterDto = ChapterDto(),
    val section: SectionRefDto? = null,
    val items: List<ArticleBriefDto> = emptyList(),
)

@Serializable
data class SectionRefDto(
    val roman: String = "",
    val title: String = "",
)

@Serializable
data class ArticleListResponseDto(
    val ok: Boolean = true,
    val items: List<ArticleBriefDto> = emptyList(),
    val meta: ListMetaDto? = null,
)

@Serializable
data class ArticleBriefDto(
    val id: Int = 0,
    val number: Int = 0,
    val title: String = "",
    val titleAuto: Boolean = false,
    val slug: String = "",
    val chapterId: Int? = null,
    val chapterRoman: String? = null,
    val chapterTitle: String? = null,
    val readingMinutes: Int = 1,
    val isPreamble: Boolean = false,
    val excerpt: String? = null,
    val progress: ReadingProgressDto? = null,
)

@Serializable
data class ReadingProgressDto(
    val status: String = "reading",
    val seconds: Int = 0,
    val lastReadAt: String? = null,
)

@Serializable
data class ArticleResponseDto(
    val ok: Boolean = true,
    val article: ArticleDto = ArticleDto(),
)

@Serializable
data class ArticleDto(
    val id: Int = 0,
    val number: Int = 0,
    val title: String = "",
    val titleAuto: Boolean = false,
    val slug: String = "",
    val readingMinutes: Int = 1,
    val effectiveFrom: String? = null,
    val isPreamble: Boolean = false,
    val section: SectionRefDto? = null,
    val chapter: ArticleChapterDto? = null,
    val topics: List<ArticleTopicDto> = emptyList(),
    val parts: List<ArticlePartDto> = emptyList(),
    val text: String? = null,
    val plain: PlainSummaryDto? = null,
    val examples: List<ArticleExampleDto> = emptyList(),
    val related: List<RelatedArticleDto> = emptyList(),
    val terms: List<TermBriefDto> = emptyList(),
    val discussion: DiscussionDto? = null,
    val quiz: QuizRefDto? = null,
    val prev: Int? = null,
    val next: Int? = null,
    val progress: ReadingProgressDto? = null,
    val isSaved: Boolean = false,
    val source: String? = null,
)

@Serializable
data class ArticleChapterDto(
    val id: Int = 0,
    val roman: String = "",
    val title: String = "",
)

@Serializable
data class ArticleTopicDto(
    val slug: String = "",
    val title: String = "",
    val tone: String? = null,
)

@Serializable
data class ArticlePartDto(
    val partNo: Int = 0,
    val body: String = "",
    val highlight: List<String> = emptyList(),
    val isExtra: Boolean = false,
)

@Serializable
data class PlainSummaryDto(
    val bullets: List<String> = emptyList(),
    val reviewedBy: String? = null,
    val reviewedAt: String? = null,
    val disclaimer: String? = null,
)

@Serializable
data class ArticleExampleDto(
    val id: Int = 0,
    val situation: String = "",
    val verdict: String = "",
    val verdictKind: String = "ok",
)

@Serializable
data class RelatedArticleDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
    val note: String? = null,
)

@Serializable
data class TermBriefDto(
    val id: Int = 0,
    val term: String = "",
    val slug: String = "",
    val letter: String? = null,
    val shortDef: String? = null,
    val articles: List<Int> = emptyList(),
    val isSaved: Boolean = false,
)

@Serializable
data class DiscussionDto(
    val count: Int = 0,
    val lastId: Int? = null,
    val lastTitle: String? = null,
)

@Serializable
data class QuizRefDto(
    val id: Int = 0,
    val slug: String = "",
    val title: String = "",
)

@Serializable
data class ArticleProgressRequestDto(
    val status: String,
    val seconds: Int? = null,
)

@Serializable
data class ArticleProgressResponseDto(
    val ok: Boolean = true,
    val status: String? = null,
    val pointsEarned: Int = 0,
    val points: Int = 0,
    val streakDays: Int = 0,
    val overview: ReadingOverviewDto? = null,
)

@Serializable
data class ReadingOverviewDto(
    val ok: Boolean = true,
    val articlesTotal: Int = 0,
    val articlesRead: Int = 0,
    val percent: Int = 0,
    @SerialName("continue")
    val continueReading: ContinueDto? = null,
)

@Serializable
data class ContinueDto(
    val lastArticle: LastArticleDto? = null,
    val chapter: ContinueChapterDto? = null,
    val nextArticle: ArticleNumberTitleDto? = null,
)

@Serializable
data class LastArticleDto(
    val number: Int = 0,
    val title: String = "",
    val slug: String = "",
    val status: String? = null,
    val readingMinutes: Int = 0,
    val minutesLeft: Int = 0,
)

@Serializable
data class ContinueChapterDto(
    val id: Int = 0,
    val roman: String = "",
    val title: String = "",
    val articleCount: Int = 0,
    val readCount: Int = 0,
    val percent: Int = 0,
)

@Serializable
data class ArticleNumberTitleDto(
    val number: Int = 0,
    val title: String = "",
)
