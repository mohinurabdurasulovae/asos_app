package uz.asos.feature.legalhelp.help

sealed interface LegalHelpAction {
    data class OnAssistantInputChange(val text: String) : LegalHelpAction
    data class OnSuggestionClick(val text: String) : LegalHelpAction
    data object OnAssistantSend : LegalHelpAction
    data class OnSpecialtySelect(val specialty: String?) : LegalHelpAction
    data object OnRetry : LegalHelpAction
    data object OnResume : LegalHelpAction

    data class OnWriteToLawyer(val lawyerId: Int) : LegalHelpAction

    data object OnCallbackRequest : LegalHelpAction
    data class OnCallbackPhoneChange(val phone: String) : LegalHelpAction
    data object OnCallbackConfirm : LegalHelpAction
    data object OnCallbackDismiss : LegalHelpAction

    data class OnBookLawyer(val lawyerId: Int) : LegalHelpAction
    data object OnBookingConfirm : LegalHelpAction
    data object OnBookingDismiss : LegalHelpAction

    data object OnBackClick : LegalHelpAction
    data object OnSignInClick : LegalHelpAction
    data class OnArticleClick(val articleNumber: Int) : LegalHelpAction
    data class OnHotlineCall(val dialNumber: String) : LegalHelpAction
    data object OnVoiceInputClick : LegalHelpAction
    data class OnCallLawyer(val lawyerId: Int) : LegalHelpAction
    data class OnVideoCallLawyer(val lawyerId: Int) : LegalHelpAction
}

sealed interface LegalHelpEvent {
    data class OpenChat(val conversationId: Int) : LegalHelpEvent
    data object NeedsSignIn : LegalHelpEvent
    data object CallbackSent : LegalHelpEvent
    data object BookingDone : LegalHelpEvent
    data object NoFreeSlot : LegalHelpEvent
    data class ShowError(val message: String) : LegalHelpEvent
}
