/*
 * CommunityFeedUiState.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Immutable state the screen renders: Loading / Success(data) / Error. Everything
 * CommunityFeedScreen needs, already formatted.
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.feed

data class CommunityTopicUi(
    val id: String,
    val label: String,
)

data class CommunityPostUi(
    val id: Int,
    val categoryLabel: String,
    val authorLabel: String,
    val timeAgoLabel: String,
    val title: String,
    val bodyPreview: String,
    val votes: Int,
    val isUpvoted: Boolean,
    val commentCount: Int,
    val isBookmarked: Boolean,
    val lawyerName: String? = null,
    val lawyerExcerpt: String? = null,
    val statusText: String? = null,
    val isLawyerAnswered: Boolean = false,
)

data class AskQuestionDraftUi(
    val selectedTopicId: String = "housing",
    val title: String = "",
    val body: String = "",
    val isAnonymous: Boolean = false,
    val isSubmitting: Boolean = false,
)

sealed interface CommunityFeedUiState {
    data object Loading : CommunityFeedUiState

    data class Error(
        val message: String,
    ) : CommunityFeedUiState

    data class Success(
        val topics: List<CommunityTopicUi>,
        val selectedTopicId: String,
        val sortOptions: List<String>,
        val selectedSortIndex: Int,
        val posts: List<CommunityPostUi>,
        val askSheetVisible: Boolean = false,
        val draft: AskQuestionDraftUi = AskQuestionDraftUi(),
    ) : CommunityFeedUiState
}

