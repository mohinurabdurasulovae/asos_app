package uz.asos.feature.constitution.article

import uz.asos.core.model.user.ReaderTextSize

sealed interface ArticleAction {
    data object OnBookmarkClick : ArticleAction
    data class OnTextSizeSelect(val size: ReaderTextSize) : ArticleAction
    data object OnToggleFullText : ArticleAction
    data object OnReachedEnd : ArticleAction
    data object OnRetry : ArticleAction

    data object OnBack : ArticleAction
    data object OnShareClick : ArticleAction
    data class OnArticleClick(val number: Int) : ArticleAction
    data class OnSiblingClick(val number: Int) : ArticleAction
    data class OnTermClick(val slug: String) : ArticleAction
    data object OnDiscussionClick : ArticleAction
    data object OnCasesClick : ArticleAction
    data class OnQuizClick(val quizId: String) : ArticleAction
    data object OnSignInClick : ArticleAction
}
