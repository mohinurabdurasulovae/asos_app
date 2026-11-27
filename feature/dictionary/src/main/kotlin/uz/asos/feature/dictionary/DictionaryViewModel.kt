/*
 * DictionaryViewModel.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug'at (design artboard: Lugat.dc.html)
 *
 * Loads the terms, debounces the search box, groups the results under their letter and keeps
 * the saved-terms count in step.
 */
package uz.asos.feature.dictionary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.data.repository.DictionaryRepository
import uz.asos.core.model.dictionary.TermSummary
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dictionaryRepository: DictionaryRepository,
) : ViewModel() {

    /** DictionaryRoute(termSlug): "" = just the list. */
    private val openSlug: String = savedStateHandle.get<String>(KEY_ARG).orEmpty()

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        load()
        loadTermOfTheDay()
        if (openSlug.isNotBlank()) openTerm(openSlug)
    }

    fun onAction(action: DictionaryAction) {
        when (action) {
            is DictionaryAction.OnQueryChange -> {
                _uiState.update { it.copy(query = action.query) }
                scheduleSearch()
            }
            DictionaryAction.OnQueryClear -> {
                _uiState.update { it.copy(query = "") }
                scheduleSearch(immediate = true)
            }
            is DictionaryAction.OnSavedOnlyChange -> {
                _uiState.update { it.copy(savedOnly = action.savedOnly) }
                load()
            }
            is DictionaryAction.OnLetterSelect -> {
                val next = action.letter.takeIf { it != _uiState.value.selectedLetter }
                _uiState.update { it.copy(selectedLetter = next) }
                load()
            }
            is DictionaryAction.OnTermClick -> openTerm(action.slug)
            DictionaryAction.OnTermDismiss -> _uiState.update { it.copy(openTerm = null) }
            is DictionaryAction.OnSaveToggle -> toggleSaved(action.termId, action.isSaved)
            DictionaryAction.OnRetry -> {
                load()
                loadTermOfTheDay()
            }
            // Navigation is handled by DictionaryRoute.
            is DictionaryAction.OnArticleClick,
            DictionaryAction.OnBackClick,
            -> Unit
        }
    }

    /** The search box reloads a short moment after the user stops typing. */
    private fun scheduleSearch(immediate: Boolean = false) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (!immediate) delay(SEARCH_DEBOUNCE_MS)
            load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = dictionaryRepository.getTerms(
                letter = state.selectedLetter,
                query = state.query.takeIf { it.isNotBlank() },
                savedOnly = state.savedOnly,
            )
            when (result) {
                is AsosResult.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        letters = result.data.letters,
                        groups = result.data.terms.groupIntoLetters(),
                        savedCount = if (state.savedOnly) {
                            result.data.terms.size
                        } else {
                            maxOf(it.savedCount, result.data.terms.count { term -> term.isSaved })
                        },
                    )
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.groups.isEmpty()) result.message else null,
                    )
                }
            }
        }
    }

    private fun loadTermOfTheDay() {
        viewModelScope.launch {
            val result = dictionaryRepository.getTermOfTheDay()
            // The card is decoration: a failure must not take the list down with it.
            if (result is AsosResult.Success) {
                _uiState.update { it.copy(termOfTheDay = result.data) }
            }
        }
    }

    private fun openTerm(slug: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTermLoading = true) }
            when (val result = dictionaryRepository.getTerm(slug)) {
                is AsosResult.Success ->
                    _uiState.update { it.copy(isTermLoading = false, openTerm = result.data) }
                is AsosResult.Error ->
                    _uiState.update { it.copy(isTermLoading = false, errorMessage = result.message) }
            }
        }
    }

    private fun toggleSaved(termId: Int, isSaved: Boolean) {
        viewModelScope.launch {
            when (val result = dictionaryRepository.toggleSaved(termId, isSaved)) {
                is AsosResult.Success -> applySaved(termId, result.data)
                is AsosResult.Error -> _uiState.update { it.copy(errorMessage = result.message) }
            }
        }
    }

    private fun applySaved(termId: Int, nowSaved: Boolean) {
        _uiState.update { state ->
            val groups = state.groups.map { group ->
                group.copy(
                    terms = group.terms.map { term ->
                        if (term.id == termId) term.copy(isSaved = nowSaved) else term
                    },
                )
            }
            state.copy(
                // In "Saqlangan" an unsaved term leaves the list at once.
                groups = if (state.savedOnly && !nowSaved) {
                    groups.map { it.copy(terms = it.terms.filter { term -> term.id != termId }) }
                        .filter { it.terms.isNotEmpty() }
                } else {
                    groups
                },
                savedCount = (state.savedCount + if (nowSaved) 1 else -1).coerceAtLeast(0),
                openTerm = state.openTerm?.let { term ->
                    if (term.id == termId) term.copy(isSaved = nowSaved) else term
                },
                termOfTheDay = state.termOfTheDay?.let { term ->
                    if (term.id == termId) term.copy(isSaved = nowSaved) else term
                },
            )
        }
    }

    private companion object {
        const val KEY_ARG = "termSlug"
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}

/** Keeps the API's order and starts a new group whenever the letter changes. */
private fun List<TermSummary>.groupIntoLetters(): List<TermGroupUi> {
    val groups = mutableListOf<TermGroupUi>()
    forEach { term ->
        val letter = term.letter?.takeIf { it.isNotBlank() } ?: term.term.take(1).uppercase()
        val last = groups.lastOrNull()
        if (last != null && last.letter == letter) {
            groups[groups.lastIndex] = last.copy(terms = last.terms + term)
        } else {
            groups += TermGroupUi(letter = letter, terms = listOf(term))
        }
    }
    return groups
}
