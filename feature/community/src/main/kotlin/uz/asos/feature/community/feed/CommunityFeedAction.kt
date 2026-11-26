/*
 * CommunityFeedAction.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Sealed interface of user actions the screen sends to CommunityFeedViewModel (clicks, text
 * input, retry, navigation requests).
 *
 * To include:
 *  - Navigates to: post, bookmarks
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.feed

sealed interface CommunityFeedAction {
    data class SelectTopic(val topicId: String) : CommunityFeedAction
    data class SelectSort(val sortIndex: Int) : CommunityFeedAction
    data class UpvotePost(val postId: Int) : CommunityFeedAction
    data class ToggleBookmark(val postId: Int) : CommunityFeedAction
    data class OpenPost(val postId: Int) : CommunityFeedAction
    data object OpenBookmarks : CommunityFeedAction
    data object OpenSearch : CommunityFeedAction
    data object OpenNotifications : CommunityFeedAction
    data object OpenAskSheet : CommunityFeedAction
    data object DismissAskSheet : CommunityFeedAction
    data class UpdateDraftTitle(val value: String) : CommunityFeedAction
    data class UpdateDraftBody(val value: String) : CommunityFeedAction
    data class UpdateDraftTopic(val topicId: String) : CommunityFeedAction
    data class ToggleDraftAnonymous(val anonymous: Boolean) : CommunityFeedAction
    data object SubmitQuestion : CommunityFeedAction
    data object Retry : CommunityFeedAction
}
