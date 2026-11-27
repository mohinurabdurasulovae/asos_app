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
        val showFullText: Boolean = false,
    ) : ArticleUiState
}

data class ArticleUi(
    val key: String,
    val id: Int,
    val number: Int,
    val isPreamble: Boolean,
    val sectionRoman: String?,
    val chapterRoman: String?,
    val chapterTitle: String?,
    val heading: String,
    val title: String?,
    val topics: List<TagUi>,
    val readingTime: String,
    val effectiveFrom: String?,
    val parts: List<PartUi>,
    val sourceLabel: String?,
    val summary: SummaryUi?,
    val examples: List<ExampleUi>,
    val related: List<RelatedUi>,
    val terms: List<TermChipUi>,
    val discussion: DiscussionUi?,
    val quizId: String?,
    val previousNumber: Int?,
    val nextNumber: Int?,
    val shareText: String,
) {
    val collapsedPartCount: Int get() = COLLAPSED_PARTS
    val hasMoreParts: Boolean get() = parts.size > COLLAPSED_PARTS

    companion object {
        const val COLLAPSED_PARTS = 3
    }
}

data class TagUi(val text: String, val tone: String?)

data class PartUi(
    val number: Int,
    val body: String,
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

sealed interface ArticleEvent {
    data class PointsEarned(val points: Int) : ArticleEvent
    data object SignInRequired : ArticleEvent
    data class Message(val text: String) : ArticleEvent
}
