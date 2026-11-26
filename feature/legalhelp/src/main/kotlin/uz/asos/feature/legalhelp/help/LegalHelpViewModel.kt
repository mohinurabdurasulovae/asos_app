/*
 * LegalHelpViewModel.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * Holds StateFlow<LegalHelpUiState> and handles LegalHelpAction. No Android UI types.
 *
 * API used (through the repository, see docs/api/openapi.yaml):
 *  - GET  /lawyers
 *  - GET  /legal-help/hotline
 *  - POST /assistant/messages
 */
package uz.asos.feature.legalhelp.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.feature.legalhelp.data.temp.Hotline
import uz.asos.feature.legalhelp.data.temp.Lawyer
import uz.asos.feature.legalhelp.data.temp.TempLegalHelpRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LegalHelpViewModel @Inject constructor(
    // TODO(WP-02): switch to uz.asos.core.data.repository.LegalHelpRepository.
    private val repository: TempLegalHelpRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LegalHelpUiState())
    val uiState: StateFlow<LegalHelpUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var assistantJob: Job? = null

    init {
        load()
    }

    fun onAction(action: LegalHelpAction) {
        when (action) {
            is LegalHelpAction.OnAssistantInputChange -> _uiState.update {
                it.copy(assistant = it.assistant.copy(input = action.text, errorMessage = null))
            }
            is LegalHelpAction.OnSuggestionClick -> _uiState.update {
                it.copy(assistant = it.assistant.copy(input = action.text, errorMessage = null))
            }
            LegalHelpAction.OnAssistantSend -> askAssistant()
            is LegalHelpAction.OnCategorySelect -> _uiState.update { it.copy(selectedCategory = action.category) }
            LegalHelpAction.OnRetry -> load()
            // Navigation and system actions are handled by LegalHelpRoute.
            LegalHelpAction.OnBackClick,
            is LegalHelpAction.OnArticleClick,
            is LegalHelpAction.OnHotlineCall,
            LegalHelpAction.OnCallbackRequest,
            LegalHelpAction.OnVoiceInputClick,
            is LegalHelpAction.OnWriteToLawyer,
            is LegalHelpAction.OnCallLawyer,
            is LegalHelpAction.OnVideoCallLawyer,
            is LegalHelpAction.OnBookLawyer,
            -> Unit
        }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val lawyers = async { repository.getLawyers() }
            val hotline = async { repository.getHotline() }

            val lawyersResult = lawyers.await()
            // The hotline card is optional: hide it if only that call fails.
            val hotlineUi = (hotline.await() as? AsosResult.Success)?.data?.toUi()

            when (val result = lawyersResult) {
                is AsosResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        lawyers = result.data.map { lawyer -> lawyer.toUi() },
                        hotline = hotlineUi,
                    )
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }

    private fun askAssistant() {
        val question = _uiState.value.assistant.input.trim()
        if (question.isEmpty() || _uiState.value.assistant.isSending) return

        assistantJob?.cancel()
        assistantJob = viewModelScope.launch {
            _uiState.update { it.copy(assistant = it.assistant.copy(isSending = true, errorMessage = null)) }
            when (val result = repository.askAssistant(question)) {
                is AsosResult.Success -> _uiState.update {
                    it.copy(
                        assistant = it.assistant.copy(
                            isSending = false,
                            answer = AssistantAnswerUi(
                                text = result.data.text,
                                articleNumber = result.data.articleNumber,
                                articleTitle = result.data.articleTitle,
                            ),
                        ),
                    )
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(assistant = it.assistant.copy(isSending = false, errorMessage = result.message))
                }
            }
        }
    }
}

private fun Lawyer.toUi() = LawyerUi(
    id = id,
    name = fullName,
    initials = initials,
    specialty = specialty,
    experienceYears = experienceYears,
    // Uzbek uses a decimal comma: 4,9
    ratingLabel = rating?.let { String.format(Locale.ROOT, "%.1f", it).replace('.', ',') },
    reviewCount = reviewCount,
    isOnline = isOnline,
    isVerified = isVerified,
    nextFreeTime = if (isOnline) null else nextFreeTime,
    categories = categories,
    avatarTone = avatarTone,
)

private fun Hotline.toUi() = HotlineUi(
    displayNumber = displayNumber,
    dialNumber = dialNumber,
    opensAt = opensAt,
    closesAt = closesAt,
    isFree = isFree,
    isOpenNow = isOpenNow,
)
