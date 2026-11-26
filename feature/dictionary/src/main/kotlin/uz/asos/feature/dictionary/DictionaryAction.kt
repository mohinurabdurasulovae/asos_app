/*
 * DictionaryAction.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * User actions the screen sends. Navigation and device actions (OnBack, OnArticleClick,
 * OnListenClick, OnSignInClick, OnLetterClick) are handled by DictionaryRoute / the screen;
 * the rest go to DictionaryViewModel.
 */
package uz.asos.feature.dictionary

sealed interface DictionaryAction {
    data class OnQueryChange(val query: String) : DictionaryAction
    data class OnFilterSelect(val filter: DictionaryFilter) : DictionaryAction
    data class OnTermClick(val slug: String) : DictionaryAction
    data object OnTermSheetDismiss : DictionaryAction
    data object OnTermSheetRetry : DictionaryAction
    /** Save / unsave. [id] is the term's internal id (needed to remove the bookmark). */
    data class OnBookmarkClick(val id: Int, val slug: String, val isSaved: Boolean) : DictionaryAction
    data object OnRetry : DictionaryAction
    data object OnListRetry : DictionaryAction

    // Handled by the screen (scrolls the list)
    data class OnLetterClick(val letter: String) : DictionaryAction

    // Handled by DictionaryRoute
    data object OnBack : DictionaryAction
    data class OnArticleClick(val number: Int) : DictionaryAction
    /** Read the term aloud (text-to-speech). */
    data class OnListenClick(val text: String) : DictionaryAction
    data object OnSignInClick : DictionaryAction
}
