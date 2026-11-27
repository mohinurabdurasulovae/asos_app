package uz.asos.feature.dictionary

import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermSummary

data class DictionaryUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val query: String = "",
    val savedOnly: Boolean = false,
    val savedCount: Int = 0,
    val termOfTheDay: TermDetail? = null,
    val selectedLetter: String? = null,
    val letters: List<AlphabetLetter> = emptyList(),
    val groups: List<TermGroupUi> = emptyList(),
    val openTerm: TermDetail? = null,
    val isTermLoading: Boolean = false,
) {
    val isEmpty: Boolean get() = groups.isEmpty()

    val showTermOfTheDay: Boolean
        get() = termOfTheDay != null && query.isBlank() && !savedOnly && selectedLetter == null
}

data class TermGroupUi(
    val letter: String,
    val terms: List<TermSummary>,
)
