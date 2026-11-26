/*
 * PostDetailAction.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Muhokama (design artboard: Post.dc.html)
 *
 * Sealed interface of user actions the screen sends to PostDetailViewModel (clicks, text
 * input, retry, navigation requests).
 *
 * To include:
 *  - Navigates to: community, chat, article
 *
 * PLACEHOLDER: not implemented yet.
 */

package uz.asos.feature.community.post

sealed interface PostDetailAction {
    data object Back : PostDetailAction
    data object OpenCommunity : PostDetailAction
    data object ToggleJoinCommunity : PostDetailAction
    data object UpvotePost : PostDetailAction
    data object ToggleBookmarkPost : PostDetailAction
    data object SharePost : PostDetailAction
    data class UpvoteReply(val replyId: String) : PostDetailAction
    data class ReplyToAuthor(val authorName: String) : PostDetailAction
    data class OpenChat(val lawyerId: Int) : PostDetailAction
    data class OpenArticle(val articleKey: String) : PostDetailAction
    data class UpdateReplyText(val text: String) : PostDetailAction
    data class ToggleAnonymous(val anonymous: Boolean) : PostDetailAction
    data object SendReply : PostDetailAction
    data object Retry : PostDetailAction
}
