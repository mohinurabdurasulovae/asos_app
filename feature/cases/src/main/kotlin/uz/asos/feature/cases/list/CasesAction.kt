package uz.asos.feature.cases.list

import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic

sealed interface CasesAction {
    data class OnTopicSelect(val topic: CaseTopic?) : CasesAction

    data class OnDifficultySelect(val difficulty: CaseDifficulty?) : CasesAction

    data class OnStatusFilterSelect(val status: CaseStatus?) : CasesAction

    data class OnOnlyBookmarkedChange(val onlyBookmarked: Boolean) : CasesAction

    data object OnFilterSheetOpen : CasesAction

    data object OnFilterSheetDismiss : CasesAction

    data object OnClearFilters : CasesAction

    data class OnCaseClick(val id: Int) : CasesAction

    data object OnBackClick : CasesAction

    data object OnRetry : CasesAction
}
