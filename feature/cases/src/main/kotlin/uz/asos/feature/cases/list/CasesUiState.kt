package uz.asos.feature.cases.list

import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic

data class CasesUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val selectedTopic: CaseTopic? = null,
    val selectedDifficulty: CaseDifficulty? = null,
    val statusFilter: CaseStatus? = null,
    val onlyBookmarked: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    val featured: FeaturedCaseUi? = null,
    val cases: List<CaseRowUi> = emptyList(),
) {
    val hasSheetFilter: Boolean get() = statusFilter != null || onlyBookmarked

    val isEmpty: Boolean get() = featured == null && cases.isEmpty()
}

data class CaseRowUi(
    val id: Int,
    val numberLabel: String,
    val title: String,
    val shortDescription: String,
    val topic: CaseTopic,
    val difficulty: CaseDifficulty,
    val status: CaseStatus,
)

data class FeaturedCaseUi(
    val id: Int,
    val number: Int,
    val numberLabel: String,
    val title: String,
    val shortDescription: String,
    val topic: CaseTopic,
    val difficulty: CaseDifficulty,
    val durationLabel: String,
    val progressFraction: Float,
    val isDone: Boolean,
)
