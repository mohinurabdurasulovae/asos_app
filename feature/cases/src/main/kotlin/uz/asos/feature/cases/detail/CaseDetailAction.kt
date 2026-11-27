/*
 * CaseDetailAction.kt
 * Module: :feature:cases   ·   Work package: WP-08
 *
 * Everything the user can do while solving a case.
 */
package uz.asos.feature.cases.detail

import uz.asos.feature.cases.data.model.ArticleOption

sealed interface CaseDetailAction {
    /** Tap on an article card: adds or removes it from the picked set. */
    data class OnArticleToggle(val number: Int) : CaseDetailAction

    data class OnSolutionChange(val text: String) : CaseDetailAction

    /** "Yechimni tekshirish". */
    data object OnSubmit : CaseDetailAction

    /** "Qaytadan". */
    data object OnRetry : CaseDetailAction

    data object OnBookmarkToggle : CaseDetailAction

    /** "Moddani o'qish" chip: opens the article sheet. */
    data class OnArticlePreviewOpen(val article: ArticleOption) : CaseDetailAction

    data object OnArticlePreviewDismiss : CaseDetailAction

    /** Opens the Modda reader of another feature. */
    data class OnOpenArticleReader(val number: Int) : CaseDetailAction

    data object OnNextCaseClick : CaseDetailAction

    data object OnBackClick : CaseDetailAction

    data object OnReload : CaseDetailAction
}
