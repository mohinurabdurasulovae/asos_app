/*
 * DictionaryUiState.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * Immutable state the screen renders. Everything is already formatted ("27 · 31-moddalar",
 * "ot · arabcha …"), so DictionaryScreen only lays it out.
 */
package uz.asos.feature.dictionary

enum class DictionaryFilter { ALL, SAVED }

data class DictionaryUiState(
    /** First load (skeleton). Later refreshes keep the old content on screen. */
    val isLoading: Boolean = true,
    /** Set when the first load failed and there is nothing to show. */
    val errorMessage: String? = null,
    val query: String = "",
    val filter: DictionaryFilter = DictionaryFilter.ALL,
    val isSignedIn: Boolean = false,
    /** Badge on the Saqlangan chip. null = unknown (guest or not loaded yet). */
    val savedCount: Int? = null,
    /** "Kun atamasi" card. Hidden while searching or on the Saqlangan filter. */
    val termOfTheDay: TermCardUi? = null,
    val list: TermListState = TermListState.Loading,
    /** A–Z rail. Letters without terms in the current list are disabled. */
    val letters: List<LetterUi> = emptyList(),
    /** Term opened from the list or from DictionaryRoute(termSlug): shown in a sheet. */
    val openedTerm: OpenedTermState? = null,
) {
    val isSearching: Boolean get() = query.isNotBlank()
    val showTermOfTheDay: Boolean get() = !isSearching && filter == DictionaryFilter.ALL && termOfTheDay != null
}

/** Full term: the "Kun atamasi" card and the opened-term sheet. */
data class TermCardUi(
    val id: Int,
    val slug: String,
    val term: String,
    /** "ot · arabcha “daxl” — tegish, aralashish"; null when there is neither. */
    val meta: String?,
    val definition: String,
    /** "Hayotda" box; null hides it. */
    val inLife: String?,
    val articles: List<ArticleChipUi>,
    val isSaved: Boolean,
)

data class ArticleChipUi(
    val number: Int,
    /** "27-modda" */
    val label: String,
)

data class TermRowUi(
    val id: Int,
    val slug: String,
    val term: String,
    val shortDefinition: String,
    /** "27 · 31-moddalar", "1-modda", or null. */
    val articlesLabel: String?,
    val isSaved: Boolean,
)

/** One letter header ("D") and the terms under it. */
data class TermGroupUi(
    val letter: String,
    val items: List<TermRowUi>,
)

data class LetterUi(
    val letter: String,
    val enabled: Boolean,
)

sealed interface TermListState {
    data object Loading : TermListState
    data class Loaded(val groups: List<TermGroupUi>) : TermListState {
        val isEmpty: Boolean get() = groups.isEmpty()
    }
    data class Failed(val message: String) : TermListState
    /** Saqlangan filter while signed out. */
    data object NeedsSignIn : TermListState
}

sealed interface OpenedTermState {
    data object Loading : OpenedTermState
    data class Loaded(val term: TermCardUi) : OpenedTermState
    data class Failed(val slug: String, val message: String) : OpenedTermState
}

/** One-off effects for DictionaryRoute. */
sealed interface DictionaryEvent {
    data object SignInRequired : DictionaryEvent
    /** An error message from the API, shown as-is. */
    data class Message(val text: String) : DictionaryEvent
}
