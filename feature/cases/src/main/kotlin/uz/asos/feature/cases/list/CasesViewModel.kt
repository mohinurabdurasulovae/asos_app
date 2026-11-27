/*
 * CasesViewModel.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * Loads the cases, applies the topic / difficulty / status / bookmark filters and keeps the
 * list in step with the progress the user makes on the detail screen.
 *
 * The prototype filtered inside the composable; the filtering lives here so the rules can be
 * unit-tested and the screen stays stateless.
 */
package uz.asos.feature.cases.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.cases.data.CasesRepository
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseProgress
import uz.asos.feature.cases.data.model.CaseStatus
import javax.inject.Inject

@HiltViewModel
class CasesViewModel @Inject constructor(
    private val casesRepository: CasesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CasesUiState())
    val uiState: StateFlow<CasesUiState> = _uiState.asStateFlow()

    private var cases: List<CaseItem> = emptyList()
    private var progress: Map<Int, CaseProgress> = emptyMap()

    init {
        load()
        observeProgress()
    }

    fun onAction(action: CasesAction) {
        when (action) {
            is CasesAction.OnTopicSelect -> patch { it.copy(selectedTopic = action.topic) }
            is CasesAction.OnDifficultySelect -> patch { it.copy(selectedDifficulty = action.difficulty) }
            is CasesAction.OnStatusFilterSelect -> patch { it.copy(statusFilter = action.status) }
            is CasesAction.OnOnlyBookmarkedChange -> patch { it.copy(onlyBookmarked = action.onlyBookmarked) }
            CasesAction.OnFilterSheetOpen -> _uiState.update { it.copy(isFilterSheetOpen = true) }
            CasesAction.OnFilterSheetDismiss -> _uiState.update { it.copy(isFilterSheetOpen = false) }
            CasesAction.OnClearFilters -> patch {
                it.copy(
                    selectedTopic = null,
                    selectedDifficulty = null,
                    statusFilter = null,
                    onlyBookmarked = false,
                )
            }
            CasesAction.OnRetry -> load()
            // Navigation is handled by CasesRoute.
            is CasesAction.OnCaseClick,
            CasesAction.OnBackClick,
            -> Unit
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = casesRepository.getCases()) {
                is AsosResult.Success -> {
                    cases = result.data
                    _uiState.update { it.copy(isLoading = false, errorMessage = null) }
                    reduce()
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (cases.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            casesRepository.observeProgress().collect { latest ->
                progress = latest
                if (cases.isNotEmpty()) reduce()
            }
        }
    }

    /** Re-applies the filters after a state or progress change. */
    private fun patch(transform: (CasesUiState) -> CasesUiState) {
        _uiState.update(transform)
        reduce()
    }

    private fun reduce() {
        val state = _uiState.value
        val featuredCase = cases.firstOrNull { it.isFeaturedOfWeek }

        _uiState.update {
            it.copy(
                featured = featuredCase
                    ?.takeIf { case -> case.matchesFilters(state) }
                    ?.toFeaturedUi(),
                cases = cases
                    .filterNot { case -> case.isFeaturedOfWeek }
                    .filter { case -> case.matchesFilters(state) }
                    .map { case -> case.toRowUi() },
            )
        }
    }

    private fun CaseItem.matchesFilters(state: CasesUiState): Boolean {
        val current = progressOf(this)
        return (state.selectedTopic == null || topic == state.selectedTopic) &&
            (state.selectedDifficulty == null || difficulty == state.selectedDifficulty) &&
            (state.statusFilter == null || current.status == state.statusFilter) &&
            (!state.onlyBookmarked || current.isBookmarked)
    }

    private fun progressOf(case: CaseItem): CaseProgress =
        progress[case.id] ?: CaseProgress(status = case.initialStatus)

    private fun CaseItem.toRowUi() = CaseRowUi(
        id = id,
        numberLabel = "№ $number",
        title = title,
        shortDescription = shortDescription,
        topic = topic,
        difficulty = difficulty,
        status = progressOf(this).status,
    )

    private fun CaseItem.toFeaturedUi(): FeaturedCaseUi {
        val current = progressOf(this)
        return FeaturedCaseUi(
            id = id,
            number = number,
            numberLabel = "№ $number",
            title = title,
            shortDescription = shortDescription,
            topic = topic,
            difficulty = difficulty,
            durationLabel = "≈ $estimatedMinutes daqiqa",
            progressFraction = when {
                current.isSubmitted && current.isCorrect -> 1f
                current.selectedArticles.isNotEmpty() -> PARTIAL_PROGRESS
                else -> initialProgressFraction
            },
            isDone = current.status == CaseStatus.YECHILGAN,
        )
    }

    private companion object {
        /** Articles picked but not checked yet, as on the prototype's featured card. */
        const val PARTIAL_PROGRESS = 0.66f
    }
}
