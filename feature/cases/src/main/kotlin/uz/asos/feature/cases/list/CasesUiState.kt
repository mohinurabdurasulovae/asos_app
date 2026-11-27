/*
 * CasesUiState.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * Everything CasesScreen draws, already filtered. No Android UI types, so the ViewModel
 * stays testable.
 */
package uz.asos.feature.cases.list

import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic

data class CasesUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    /** null = "Barchasi". */
    val selectedTopic: CaseTopic? = null,
    /** null = "Barchasi". */
    val selectedDifficulty: CaseDifficulty? = null,
    /** Sheet filter; null = every status. */
    val statusFilter: CaseStatus? = null,
    val onlyBookmarked: Boolean = false,
    val isFilterSheetOpen: Boolean = false,
    /** "Haftaning kazusi", null when the filters hide it. */
    val featured: FeaturedCaseUi? = null,
    /** Every other case that passes the filters. */
    val cases: List<CaseRowUi> = emptyList(),
) {
    /** The sort button is highlighted while a sheet filter is on. */
    val hasSheetFilter: Boolean get() = statusFilter != null || onlyBookmarked

    val isEmpty: Boolean get() = featured == null && cases.isEmpty()
}

data class CaseRowUi(
    val id: Int,
    /** "№ 14" */
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
    /** "≈ 6 daqiqa" */
    val durationLabel: String,
    /** 0f..1f */
    val progressFraction: Float,
    /** true once solved: the button reads "Ko‘rish" instead of "Davom etish". */
    val isDone: Boolean,
)
