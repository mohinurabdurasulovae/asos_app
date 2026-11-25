/*
 * ArticleUiState.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * Immutable state the reader renders: Loading / Error / Success. Everything is already
 * formatted. The official text (parts) is kept exactly as the API sends it.
 */
package uz.asos.feature.constitution.article

import uz.asos.core.model.user.ReaderTextSize

sealed interface ArticleUiState {
    data object Loading : ArticleUiState

    data class Error(val message: String) : ArticleUiState

    data class Success(
        val article: ArticleUi,
        val isSaved: Boolean,
        val isBookmarkBusy: Boolean = false,
        val textSize: ReaderTextSize = ReaderTextSize.MEDIUM,
        /** false = only the first [ArticleUi.collapsedPartCount] parts are shown. */
        val showFullText: Boolean = false,
    ) : ArticleUiState
}

data class ArticleUi(
    /** Route key: "50" or "muqaddima". */
    val key: String,
    /** Internal id, used for bookmarks. */
    val id: Int,
    val number: Int,
    val isPreamble: Boolean,
    /** "II" → shown as "II bo‘lim". null for the preamble. */
    val sectionRoman: String?,
    /** "IX" → shown as "IX bob". */
    val chapterRoman: String?,
    /** Blue line above the number: the chapter's title. */
    val chapterTitle: String?,
    /** "50-modda", or "Muqaddima". */
    val heading: String,
    /** Editorial title ("Ta’lim olish huquqi"); null when the article has none. */
    val title: String?,
    val topics: List<TagUi>,
    /** "4 daqiqa" */
    val readingTime: String,
    /** "01.05.2023"; the screen adds "dan kuchda". */
    val effectiveFrom: String?,
    val parts: List<PartUi>,
    val sourceLabel: String?,
    val summary: SummaryUi?,
    val examples: List<ExampleUi>,
    val related: List<RelatedUi>,
    val terms: List<TermChipUi>,
    val discussion: DiscussionUi?,
    /** Quiz id or slug for "Bilimni sinash"; null = no quiz yet. */
    val quizId: String?,
    val previousNumber: Int?,
    val nextNumber: Int?,
    /** Plain text for the share sheet. */
    val shareText: String,
) {
    /** The design shows three parts, then "To‘liq matnni ochish". */
    val collapsedPartCount: Int get() = COLLAPSED_PARTS
    val hasMoreParts: Boolean get() = parts.size > COLLAPSED_PARTS

    companion object {
        const val COLLAPSED_PARTS = 3
    }
}

data class TagUi(val text: String, val tone: String?)

data class PartUi(
    /** Part number shown in the margin. */
    val number: Int,
    /** Official text, verbatim. */
    val body: String,
    /** Phrases to highlight inside [body]. */
    val highlights: List<String>,
)

data class SummaryUi(
    val bullets: List<String>,
    val reviewedBy: String?,
    val disclaimer: String?,
)

data class ExampleUi(
    val situation: String,
    val verdict: String,
    val isLawful: Boolean,
)

data class RelatedUi(
    val number: Int,
    val title: String,
)

data class TermChipUi(
    val slug: String,
    val term: String,
)

data class DiscussionUi(
    val count: Int,
    val lastTitle: String?,
)

/** One-off events: snackbars. */
sealed interface ArticleEvent {
    /** First time the article was marked read: "+10 ball". */
    data class PointsEarned(val points: Int) : ArticleEvent
    /** Bookmark tapped while signed out. */
    data object SignInRequired : ArticleEvent
    /** An error message from the API, shown as-is. */
    data class Message(val text: String) : ArticleEvent
}
