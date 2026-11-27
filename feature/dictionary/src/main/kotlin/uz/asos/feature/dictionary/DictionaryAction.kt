package uz.asos.feature.dictionary

sealed interface DictionaryAction {
    data class OnQueryChange(val query: String) : DictionaryAction

    data object OnQueryClear : DictionaryAction

    data class OnSavedOnlyChange(val savedOnly: Boolean) : DictionaryAction

    data class OnLetterSelect(val letter: String?) : DictionaryAction

    data class OnTermClick(val slug: String) : DictionaryAction

    data object OnTermDismiss : DictionaryAction

    data class OnSaveToggle(val termId: Int, val isSaved: Boolean) : DictionaryAction

    data class OnArticleClick(val number: Int) : DictionaryAction

    data object OnBackClick : DictionaryAction

    data object OnRetry : DictionaryAction
}
