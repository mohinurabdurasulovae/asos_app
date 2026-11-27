package uz.asos.feature.community.post

import uz.asos.core.model.community.VoteState

sealed interface PostDetailAction {
    data class OnPostVote(val vote: VoteState) : PostDetailAction

    data class OnCommentVote(val commentId: Int, val vote: VoteState) : PostDetailAction

    data object OnSaveToggle : PostDetailAction

    data object OnShareClick : PostDetailAction

    data object OnMembershipToggle : PostDetailAction

    data class OnReplyTo(val commentId: Int?, val authorName: String?) : PostDetailAction

    data class OnReplyChange(val body: String) : PostDetailAction

    data class OnReplyAnonymousChange(val isAnonymous: Boolean) : PostDetailAction

    data object OnReplySend : PostDetailAction

    data class OnArticleClick(val number: Int) : PostDetailAction

    data class OnWriteToLawyer(val lawyerId: Int) : PostDetailAction

    data object OnSignInClick : PostDetailAction

    data object OnNoticeDismiss : PostDetailAction

    data object OnBackClick : PostDetailAction

    data object OnRetry : PostDetailAction
}
