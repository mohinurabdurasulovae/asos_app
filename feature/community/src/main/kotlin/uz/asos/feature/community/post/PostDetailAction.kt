/*
 * PostDetailAction.kt
 * Module: :feature:community   ·   Work package: WP-09
 *
 * Everything the user can do on the Muhokama screen.
 */
package uz.asos.feature.community.post

import uz.asos.core.model.community.VoteState

sealed interface PostDetailAction {
    data class OnPostVote(val vote: VoteState) : PostDetailAction

    data class OnCommentVote(val commentId: Int, val vote: VoteState) : PostDetailAction

    data object OnSaveToggle : PostDetailAction

    data object OnShareClick : PostDetailAction

    data object OnMembershipToggle : PostDetailAction

    /** "Javob berish" under a comment; null answers the post itself. */
    data class OnReplyTo(val commentId: Int?, val authorName: String?) : PostDetailAction

    data class OnReplyChange(val body: String) : PostDetailAction

    data class OnReplyAnonymousChange(val isAnonymous: Boolean) : PostDetailAction

    data object OnReplySend : PostDetailAction

    /** "31-modda" link: opens the Modda reader. */
    data class OnArticleClick(val number: Int) : PostDetailAction

    /** "Advokatga yozish" on the lawyer's answer. */
    data class OnWriteToLawyer(val lawyerId: Int) : PostDetailAction

    data object OnSignInClick : PostDetailAction

    data object OnNoticeDismiss : PostDetailAction

    data object OnBackClick : PostDetailAction

    data object OnRetry : PostDetailAction
}
