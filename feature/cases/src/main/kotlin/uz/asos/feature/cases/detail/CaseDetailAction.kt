package uz.asos.feature.cases.detail

import uz.asos.feature.cases.data.model.ArticleOption

sealed interface CaseDetailAction {
    data class OnArticleToggle(val number: Int) : CaseDetailAction

    data class OnSolutionChange(val text: String) : CaseDetailAction

    data object OnSubmit : CaseDetailAction

    data object OnRetry : CaseDetailAction

    data object OnBookmarkToggle : CaseDetailAction

    data class OnArticlePreviewOpen(val article: ArticleOption) : CaseDetailAction

    data object OnArticlePreviewDismiss : CaseDetailAction

    data class OnOpenArticleReader(val number: Int) : CaseDetailAction

    data object OnNextCaseClick : CaseDetailAction

    data object OnBackClick : CaseDetailAction

    data object OnReload : CaseDetailAction
}
