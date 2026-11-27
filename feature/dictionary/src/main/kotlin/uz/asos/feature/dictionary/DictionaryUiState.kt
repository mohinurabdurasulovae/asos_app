/*
 * DictionaryUiState.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug'at (design artboard: Lugat.dc.html)
 *
 * Everything DictionaryScreen draws, already grouped by letter. No Android UI types.
 */
package uz.asos.feature.dictionary

import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermSummary

data class DictionaryUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val query: String = "",
    /** "Barcha atamalar" or "Saqlangan". */
    val savedOnly: Boolean = false,
    /** Count on the "Saqlangan" chip. */
    val savedCount: Int = 0,
    /** The «Kun atamasi» card; hidden while searching or filtering. */
    val termOfTheDay: TermDetail? = null,
    /** Letter the alphabet rail highlights; null = none picked. */
    val selectedLetter: String? = null,
    val letters: List<AlphabetLetter> = emptyList(),
    /** Terms grouped under their letter heading, in alphabet order. */
    val groups: List<TermGroupUi> = emptyList(),
    /** Open term sheet (DictionaryRoute(termSlug) or a tap on a row). */
    val openTerm: TermDetail? = null,
    val isTermLoading: Boolean = false,
) {
    val isEmpty: Boolean get() = groups.isEmpty()

    /** The card only makes sense on the unfiltered A–Z list. */
    val showTermOfTheDay: Boolean
        get() = termOfTheDay != null && query.isBlank() && !savedOnly && selectedLetter == null
}

/** One letter heading plus the terms under it. */
data class TermGroupUi(
    val letter: String,
    val terms: List<TermSummary>,
)
