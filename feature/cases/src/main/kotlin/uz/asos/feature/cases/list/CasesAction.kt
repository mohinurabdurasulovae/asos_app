/*
 * CasesAction.kt
 * Module: :feature:cases   ·   Work package: WP-08
 *
 * Everything the user can do on the Kazuslar screen.
 */
package uz.asos.feature.cases.list

import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic

sealed interface CasesAction {
    /** null = "Barchasi". */
    data class OnTopicSelect(val topic: CaseTopic?) : CasesAction

    /** null = "Barchasi". */
    data class OnDifficultySelect(val difficulty: CaseDifficulty?) : CasesAction

    data class OnStatusFilterSelect(val status: CaseStatus?) : CasesAction

    data class OnOnlyBookmarkedChange(val onlyBookmarked: Boolean) : CasesAction

    data object OnFilterSheetOpen : CasesAction

    data object OnFilterSheetDismiss : CasesAction

    data object OnClearFilters : CasesAction

    /** Opens Kazus — yechim. */
    data class OnCaseClick(val id: Int) : CasesAction

    data object OnBackClick : CasesAction

    data object OnRetry : CasesAction
}
