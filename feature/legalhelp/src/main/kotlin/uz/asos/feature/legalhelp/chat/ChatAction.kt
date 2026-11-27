package uz.asos.feature.legalhelp.chat

sealed interface ChatAction {
    data class OnInputChange(val text: String) : ChatAction
    data object OnSend : ChatAction
    data class OnQuickReply(val text: String) : ChatAction
    data object OnRetry : ChatAction
    data object OnResume : ChatAction
    data object OnPause : ChatAction
    data object OnSendErrorShown : ChatAction

    data object OnBackClick : ChatAction
    data object OnSignInClick : ChatAction
    data class OnArticleClick(val articleNumber: Int) : ChatAction
    data class OnFileClick(val file: ChatFileUi) : ChatAction
    data object OnCallClick : ChatAction
    data object OnVideoCallClick : ChatAction
    data object OnAttachClick : ChatAction
    data object OnVoiceClick : ChatAction
}
