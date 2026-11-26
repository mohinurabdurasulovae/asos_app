/*
 * LegalHelpAction.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * User actions the screen sends. Navigation and system actions (dialer, snackbars) are
 * handled by LegalHelpRoute; the rest go to LegalHelpViewModel.
 */
package uz.asos.feature.legalhelp.help

import uz.asos.feature.legalhelp.data.temp.LawyerCategory

sealed interface LegalHelpAction {
    data class OnAssistantInputChange(val text: String) : LegalHelpAction
    /** Suggestion chip: fills the input with its label. */
    data class OnSuggestionClick(val text: String) : LegalHelpAction
    data object OnAssistantSend : LegalHelpAction
    /** null = "Barchasi". */
    data class OnCategorySelect(val category: LawyerCategory?) : LegalHelpAction
    data object OnRetry : LegalHelpAction

    // Handled in LegalHelpRoute
    data object OnBackClick : LegalHelpAction
    data class OnArticleClick(val articleNumber: Int) : LegalHelpAction
    data class OnHotlineCall(val dialNumber: String) : LegalHelpAction
    data object OnCallbackRequest : LegalHelpAction
    data object OnVoiceInputClick : LegalHelpAction
    data class OnWriteToLawyer(val lawyerId: Int) : LegalHelpAction
    data class OnCallLawyer(val lawyerId: Int) : LegalHelpAction
    data class OnVideoCallLawyer(val lawyerId: Int) : LegalHelpAction
    data class OnBookLawyer(val lawyerId: Int) : LegalHelpAction
}
