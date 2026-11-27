package uz.asos.feature.cases.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.cases.data.CasesRepository
import uz.asos.feature.cases.data.model.CaseItem
import uz.asos.feature.cases.data.model.CaseProgress
import uz.asos.feature.cases.data.model.CaseStatus
import javax.inject.Inject

@HiltViewModel
class CaseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val casesRepository: CasesRepository,
) : ViewModel() {

    private val caseId: Int? = checkNotNull(savedStateHandle.get<String>(KEY_ARG)) {
        "CaseDetailViewModel needs the \"$KEY_ARG\" navigation argument"
    }.toIntOrNull()

    private val _uiState = MutableStateFlow(CaseDetailUiState())
    val uiState: StateFlow<CaseDetailUiState> = _uiState.asStateFlow()

    private var case: CaseItem? = null
    private var progress = CaseProgress()

    init {
        load()
    }

    fun onAction(action: CaseDetailAction) {
        when (action) {
            is CaseDetailAction.OnArticleToggle -> toggleArticle(action.number)
            is CaseDetailAction.OnSolutionChange -> changeSolution(action.text)
            CaseDetailAction.OnSubmit -> submit()
            CaseDetailAction.OnRetry -> reset()
            CaseDetailAction.OnBookmarkToggle -> toggleBookmark()
            is CaseDetailAction.OnArticlePreviewOpen ->
                _uiState.update { it.copy(previewArticle = action.article) }
            CaseDetailAction.OnArticlePreviewDismiss ->
                _uiState.update { it.copy(previewArticle = null) }
            CaseDetailAction.OnReload -> load()
            is CaseDetailAction.OnOpenArticleReader,
            CaseDetailAction.OnNextCaseClick,
            CaseDetailAction.OnBackClick,
            -> Unit
        }
    }

    private fun load() {
        val id = caseId
        if (id == null) {
            _uiState.update { it.copy(isLoading = false, errorMessage = CASE_NOT_FOUND) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = casesRepository.getCase(id)) {
                is AsosResult.Success -> {
                    case = result.data
                    progress = currentProgress(id, result.data)
                    render()
                    findNextCase(id)
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    private suspend fun currentProgress(id: Int, item: CaseItem): CaseProgress {
        val stored = casesRepository.observeProgress().first()[id]
        return stored ?: CaseProgress(status = item.initialStatus)
    }

    private suspend fun findNextCase(id: Int) {
        val all = (casesRepository.getCases() as? AsosResult.Success)?.data ?: return
        val stored = casesRepository.observeProgress().first()
        val next = all.firstOrNull { item ->
            item.id != id && (stored[item.id]?.status ?: item.initialStatus) != CaseStatus.YECHILGAN
        }
        _uiState.update { it.copy(nextCaseId = next?.id) }
    }

    private fun toggleArticle(number: Int) {
        val id = caseId ?: return
        if (progress.isSubmitted) return

        val picked = if (number in progress.selectedArticles) {
            progress.selectedArticles - number
        } else {
            progress.selectedArticles + number
        }
        progress = progress.copy(selectedArticles = picked)
        render()
        viewModelScope.launch { casesRepository.saveDraft(id, picked, progress.userSolution) }
    }

    private fun changeSolution(text: String) {
        val id = caseId ?: return
        if (progress.isSubmitted) return

        progress = progress.copy(userSolution = text)
        render()
        viewModelScope.launch { casesRepository.saveDraft(id, progress.selectedArticles, text) }
    }

    private fun submit() {
        val id = caseId ?: return
        if (!_uiState.value.canSubmit) return

        viewModelScope.launch {
            casesRepository.saveDraft(id, progress.selectedArticles, progress.userSolution)
            when (val result = casesRepository.checkSolution(id)) {
                is AsosResult.Success -> {
                    progress = result.data
                    render()
                    findNextCase(id)
                }
                is AsosResult.Error -> _uiState.update { it.copy(errorMessage = result.message) }
            }
        }
    }

    private fun reset() {
        val id = caseId ?: return
        viewModelScope.launch {
            casesRepository.resetSolution(id)
            progress = progress.copy(
                selectedArticles = emptySet(),
                userSolution = "",
                isSubmitted = false,
                isCorrect = false,
                status = CaseStatus.JARAYONDA,
            )
            render()
        }
    }

    private fun toggleBookmark() {
        val id = caseId ?: return
        viewModelScope.launch {
            casesRepository.toggleBookmark(id)
            progress = progress.copy(isBookmarked = !progress.isBookmarked)
            render()
        }
    }

    private fun render() {
        val item = case ?: return
        _uiState.update { state ->
            state.copy(
                isLoading = false,
                errorMessage = null,
                titleLabel = "Kazus № ${item.number}",
                title = item.title,
                topic = item.topic,
                difficulty = item.difficulty,
                persona = PersonaUi(
                    initials = item.personaInitials,
                    name = item.personaName,
                    subtitle = item.personaSubtitle,
                ),
                storyText = item.storyText,
                questionText = item.questionText,
                options = item.options.map { option ->
                    ArticleChoiceUi(
                        option = option,
                        isSelected = option.number in progress.selectedArticles,
                        isCorrectOption = option.number in item.correctArticleNumbers,
                    )
                },
                userSolution = progress.userSolution,
                isSubmitted = progress.isSubmitted,
                isCorrect = progress.isCorrect,
                isBookmarked = progress.isBookmarked,
                rewardPoints = item.rewardPoints,
                lawyerComment = item.lawyerComment,
                correctArticles = item.options.filter { it.number in item.correctArticleNumbers },
            )
        }
    }

    private companion object {
        const val KEY_ARG = "caseId"
        const val CASE_NOT_FOUND = "Kazus topilmadi"
    }
}
