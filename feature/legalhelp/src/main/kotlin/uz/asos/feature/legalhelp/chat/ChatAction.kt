/*
 * ChatAction.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * User actions the chat screen sends. Navigation and snackbars are handled by ChatRoute;
 * the rest go to ChatViewModel.
 */
package uz.asos.feature.legalhelp.chat

sealed interface ChatAction {
    data class OnInputChange(val text: String) : ChatAction
    data object OnSend : ChatAction
    /** Quick reply chip: sends its text right away. */
    data class OnQuickReply(val text: String) : ChatAction
    data object OnRetry : ChatAction
    /** Screen visible: start refreshing for the lawyer's replies. */
    data object OnResume : ChatAction
    /** Screen hidden: stop refreshing. */
    data object OnPause : ChatAction
    data object OnSendErrorShown : ChatAction

    // Handled in ChatRoute
    data object OnBackClick : ChatAction
    data object OnSignInClick : ChatAction
    data class OnArticleClick(val articleNumber: Int) : ChatAction
    data class OnFileClick(val file: ChatFileUi) : ChatAction
    data object OnCallClick : ChatAction
    data object OnVideoCallClick : ChatAction
    data object OnAttachClick : ChatAction
    data object OnVoiceClick : ChatAction
}
