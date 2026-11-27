/*
 * LegalHelpAction.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * User actions the screen sends, and one-time events the ViewModel sends back.
 * Pure navigation and system actions (back, article, dialer, "coming soon") are handled by
 * LegalHelpRoute; the rest go to LegalHelpViewModel.
 */
package uz.asos.feature.legalhelp.help

sealed interface LegalHelpAction {
    data class OnAssistantInputChange(val text: String) : LegalHelpAction
    /** Suggestion chip: fills the input with its label. */
    data class OnSuggestionClick(val text: String) : LegalHelpAction
    data object OnAssistantSend : LegalHelpAction
    /** null = "Barchasi". */
    data class OnSpecialtySelect(val specialty: String?) : LegalHelpAction
    data object OnRetry : LegalHelpAction
    data object OnResume : LegalHelpAction

    /** "Yozish": opens (or reuses) the chat with this lawyer. */
    data class OnWriteToLawyer(val lawyerId: Int) : LegalHelpAction

    /** "Menga qo‘ng‘iroq": signed in → sends at once; otherwise asks for the phone number. */
    data object OnCallbackRequest : LegalHelpAction
    data class OnCallbackPhoneChange(val phone: String) : LegalHelpAction
    data object OnCallbackConfirm : LegalHelpAction
    data object OnCallbackDismiss : LegalHelpAction

    /** "Vaqt band qilish": asks to confirm the lawyer's next free slot. */
    data class OnBookLawyer(val lawyerId: Int) : LegalHelpAction
    data object OnBookingConfirm : LegalHelpAction
    data object OnBookingDismiss : LegalHelpAction

    // Handled in LegalHelpRoute
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
    /** Server text, shown as-is. */
    data class ShowError(val message: String) : LegalHelpEvent
}
