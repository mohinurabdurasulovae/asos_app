/*
 * DictionaryAction.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 *
 * Everything the user can do on the Konstitutsiyaviy lug'at screen.
 */
package uz.asos.feature.dictionary

sealed interface DictionaryAction {
    data class OnQueryChange(val query: String) : DictionaryAction

    data object OnQueryClear : DictionaryAction

    /** "Barcha atamalar" / "Saqlangan". */
    data class OnSavedOnlyChange(val savedOnly: Boolean) : DictionaryAction

    /** Alphabet rail; the same letter again clears the filter. */
    data class OnLetterSelect(val letter: String?) : DictionaryAction

    /** Opens the term sheet. */
    data class OnTermClick(val slug: String) : DictionaryAction

    data object OnTermDismiss : DictionaryAction

    data class OnSaveToggle(val termId: Int, val isSaved: Boolean) : DictionaryAction

    /** "27-modda" chip inside a term: opens the Modda reader. */
    data class OnArticleClick(val number: Int) : DictionaryAction

    data object OnBackClick : DictionaryAction

    data object OnRetry : DictionaryAction
}
