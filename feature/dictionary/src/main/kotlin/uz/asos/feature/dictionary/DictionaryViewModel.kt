/*
 * DictionaryViewModel.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * Holds StateFlow<DictionaryUiState> and handles DictionaryAction. No Android UI types.
 *
 * API used (through repositories, see docs/api/openapi.yaml):
 *  - GET /v1/terms               list, search (q), Saqlangan (saved=1, token) + alphabet rail
 *  - GET /v1/terms/of-the-day    "Kun atamasi" card
 *  - GET /v1/terms/{slug}        opened term (from the list or DictionaryRoute(termSlug))
 *  - POST /v1/me/bookmarks, DELETE /v1/me/bookmarks/term/{id}   save / unsave (token)
 */
package uz.asos.feature.dictionary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.result.getOrNull
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.BookmarkRepository
import uz.asos.core.data.repository.DictionaryRepository
import uz.asos.core.model.dictionary.Term
import uz.asos.core.model.dictionary.TermList
import uz.asos.core.model.dictionary.TermQuery
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.core.model.user.BookmarkType
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dictionaryRepository: DictionaryRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    private val _events = Channel<DictionaryEvent>(Channel.BUFFERED)
    val events: Flow<DictionaryEvent> = _events.receiveAsFlow()

    /** Full alphabet from the API, kept so the rail doesn't jump while filtering. */
    private var alphabet: List<String> = emptyList()
    private var hasLoaded = false
    private var loadJob: Job? = null
    private var listJob: Job? = null
    private var searchJob: Job? = null
    private var termJob: Job? = null
    private var countJob: Job? = null

    init {
        // DictionaryRoute(termSlug) stores its argument under "termSlug" ("" = just the list).
        val initialSlug = savedStateHandle.get<String>("termSlug").orEmpty()
        observeSession()
        load(showSkeleton = true)
        if (initialSlug.isNotBlank()) openTerm(initialSlug)
    }

    fun onAction(action: DictionaryAction) {
        when (action) {
            is DictionaryAction.OnQueryChange -> onQueryChange(action.query)
            is DictionaryAction.OnFilterSelect -> {
                if (_uiState.value.filter == action.filter) return
                _uiState.update { it.copy(filter = action.filter) }
                refreshList()
            }
            is DictionaryAction.OnTermClick -> openTerm(action.slug)
            DictionaryAction.OnTermSheetDismiss -> {
                termJob?.cancel()
                _uiState.update { it.copy(openedTerm = null) }
            }
            DictionaryAction.OnTermSheetRetry -> {
                (_uiState.value.openedTerm as? OpenedTermState.Failed)?.let { openTerm(it.slug) }
            }
            is DictionaryAction.OnBookmarkClick -> toggleBookmark(action.id, action.slug, action.isSaved)
            DictionaryAction.OnRetry -> load(showSkeleton = true)
            DictionaryAction.OnListRetry -> refreshList()
            // Handled by DictionaryRoute / the screen.
            is DictionaryAction.OnLetterClick,
            DictionaryAction.OnBack,
            is DictionaryAction.OnArticleClick,
            is DictionaryAction.OnListenClick,
            DictionaryAction.OnSignInClick,
            -> Unit
        }
    }

    // ------------------------------------------------------------------ loading

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                val changed = _uiState.value.isSignedIn != signedIn
                _uiState.update { it.copy(isSignedIn = signedIn, savedCount = if (signedIn) it.savedCount else null) }
                // Saved state of every term depends on the user: reload after sign-in / sign-out.
                if (changed && hasLoaded) load(showSkeleton = false)
            }
        }
    }

    private fun load(showSkeleton: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (showSkeleton) _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val termOfTheDay = async { dictionaryRepository.getTermOfTheDay() }
            val terms = async { dictionaryRepository.getTerms(currentQuery() ?: TermQuery()) }

            when (val result = terms.await()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    val day = termOfTheDay.await().getOrNull()
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            errorMessage = null,
                            termOfTheDay = day?.toCard() ?: state.termOfTheDay,
                        )
                    }
                    // The first call ignores the Saqlangan filter for guests; refreshList handles it.
                    if (currentQuery() == TermQuery()) applyList(result.data) else refreshList()
                    refreshSavedCount()
                }
                is AsosResult.Error -> _uiState.update {
                    val nothingShown = it.list !is TermListState.Loaded
                    it.copy(
                        isLoading = false,
                        errorMessage = if (nothingShown) result.message else null,
                    )
                }
            }
        }
    }

    /** The query for the current search / filter, or null when the user must sign in first. */
    private fun currentQuery(): TermQuery? {
        val state = _uiState.value
        val saved = state.filter == DictionaryFilter.SAVED
        if (saved && !state.isSignedIn) return null
        return TermQuery(text = state.query.trim().takeIf { it.isNotEmpty() }, savedOnly = saved)
    }

    private fun refreshList() {
        listJob?.cancel()
        val query = currentQuery()
        if (query == null) {
            _uiState.update { it.copy(list = TermListState.NeedsSignIn, letters = letters(emptySet())) }
            return
        }
        _uiState.update { it.copy(list = TermListState.Loading) }
        listJob = viewModelScope.launch {
            when (val result = dictionaryRepository.getTerms(query)) {
                is AsosResult.Success -> applyList(result.data)
                is AsosResult.Error -> _uiState.update { it.copy(list = TermListState.Failed(result.message)) }
            }
        }
    }

    private fun applyList(list: TermList) {
        if (list.letters.isNotEmpty()) alphabet = list.letters.map { it.letter }
        val groups = groupByLetter(list.items)
        _uiState.update { state ->
            state.copy(
                list = TermListState.Loaded(groups),
                letters = letters(groups.map { it.letter }.toSet()),
                // A saved-only list tells us the exact count for the chip badge.
                savedCount = if (state.filter == DictionaryFilter.SAVED && !state.isSearching) list.total else state.savedCount,
            )
        }
    }

    private fun refreshSavedCount() {
        countJob?.cancel()
        if (!_uiState.value.isSignedIn) return
        countJob = viewModelScope.launch {
            dictionaryRepository.getTerms(TermQuery(savedOnly = true)).getOrNull()?.let { saved ->
                _uiState.update { it.copy(savedCount = saved.total) }
            }
        }
    }

    private fun groupByLetter(items: List<TermSummary>): List<TermGroupUi> {
        val order = alphabet.withIndex().associate { (index, letter) -> letter to index }
        return items
            .groupBy { it.letter?.takeIf(String::isNotBlank) ?: it.term.take(1).uppercase() }
            .entries
            .sortedBy { order[it.key] ?: Int.MAX_VALUE }
            .map { (letter, terms) -> TermGroupUi(UzbekFormatters.display(letter), terms.map { it.toRow() }) }
    }

    private fun letters(withTerms: Set<String>): List<LetterUi> =
        alphabet.map { UzbekFormatters.display(it) }.map { LetterUi(it, enabled = it in withTerms) }

    // ------------------------------------------------------------------ search

    private fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (query.isNotBlank()) delay(SEARCH_DEBOUNCE_MS)
            refreshList()
        }
    }

    // ------------------------------------------------------------------ opened term

    private fun openTerm(slug: String) {
        termJob?.cancel()
        _uiState.update { it.copy(openedTerm = OpenedTermState.Loading) }
        termJob = viewModelScope.launch {
            val opened = when (val result = dictionaryRepository.getTerm(slug)) {
                is AsosResult.Success -> OpenedTermState.Loaded(result.data.toCard())
                is AsosResult.Error -> OpenedTermState.Failed(slug, result.message)
            }
            _uiState.update { it.copy(openedTerm = opened) }
        }
    }

    // ------------------------------------------------------------------ bookmarks

    private fun toggleBookmark(id: Int, slug: String, isSaved: Boolean) {
        if (!_uiState.value.isSignedIn) {
            _events.trySend(DictionaryEvent.SignInRequired)
            return
        }
        val saving = !isSaved
        setSaved(slug, saving)
        viewModelScope.launch {
            val result = if (saving) {
                bookmarkRepository.saveTerm(slug)
            } else {
                bookmarkRepository.remove(BookmarkType.TERM, id)
            }
            when (result) {
                is AsosResult.Success -> if (_uiState.value.filter == DictionaryFilter.SAVED) refreshList()
                is AsosResult.Error -> {
                    setSaved(slug, isSaved)
                    _events.send(DictionaryEvent.Message(result.message))
                }
            }
        }
    }

    /** Updates the saved flag everywhere the term is shown, and the chip badge. */
    private fun setSaved(slug: String, saved: Boolean) {
        _uiState.update { state ->
            val wasSaved = findSaved(state, slug)
            val list = (state.list as? TermListState.Loaded)?.let { loaded ->
                loaded.copy(
                    groups = loaded.groups.map { group ->
                        group.copy(items = group.items.map { if (it.slug == slug) it.copy(isSaved = saved) else it })
                    },
                )
            } ?: state.list
            val opened = (state.openedTerm as? OpenedTermState.Loaded)?.let {
                if (it.term.slug == slug) it.copy(term = it.term.copy(isSaved = saved)) else it
            } ?: state.openedTerm
            val delta = when {
                wasSaved == saved -> 0
                saved -> 1
                else -> -1
            }
            state.copy(
                list = list,
                openedTerm = opened,
                termOfTheDay = state.termOfTheDay?.let { if (it.slug == slug) it.copy(isSaved = saved) else it },
                savedCount = state.savedCount?.let { (it + delta).coerceAtLeast(0) },
            )
        }
    }

    private fun findSaved(state: DictionaryUiState, slug: String): Boolean? =
        (state.list as? TermListState.Loaded)?.groups?.flatMap { it.items }?.firstOrNull { it.slug == slug }?.isSaved
            ?: state.termOfTheDay?.takeIf { it.slug == slug }?.isSaved
            ?: (state.openedTerm as? OpenedTermState.Loaded)?.term?.takeIf { it.slug == slug }?.isSaved

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}

// ---------------------------------------------------------------------- mapping to UI models

internal fun Term.toCard() = TermCardUi(
    id = id,
    slug = slug,
    term = UzbekFormatters.display(term),
    meta = listOfNotNull(partOfSpeech, etymology?.let(UzbekFormatters::display))
        .joinToString(" · ")
        .ifBlank { null },
    definition = UzbekFormatters.display(definition.ifBlank { shortDefinition.orEmpty() }),
    inLife = inLife?.let(UzbekFormatters::display),
    articles = articles.map { ArticleChipUi(it.number, UzbekFormatters.article(it.number)) },
    isSaved = isSaved,
)

internal fun TermSummary.toRow() = TermRowUi(
    id = id,
    slug = slug,
    term = UzbekFormatters.display(term),
    shortDefinition = UzbekFormatters.display(shortDefinition.orEmpty()),
    articlesLabel = articlesLabel(articleNumbers),
    isSaved = isSaved,
)

/** [] → null, [1] → "1-modda", [27, 31] → "27 · 31-moddalar". */
internal fun articlesLabel(numbers: List<Int>): String? = when (numbers.size) {
    0 -> null
    1 -> UzbekFormatters.article(numbers.single())
    else -> numbers.joinToString(" · ") + "-moddalar"
}
