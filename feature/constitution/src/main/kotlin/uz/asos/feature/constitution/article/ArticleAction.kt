/*
 * ArticleAction.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * User actions the reader sends. Navigation and sharing are handled by ArticleRoute;
 * the rest go to ArticleViewModel.
 *
 * Navigates to: another article, dictionary, community, cases, quiz, login.
 */
package uz.asos.feature.constitution.article

import uz.asos.core.model.user.ReaderTextSize

sealed interface ArticleAction {
    data object OnBookmarkClick : ArticleAction
    data class OnTextSizeSelect(val size: ReaderTextSize) : ArticleAction
    data object OnToggleFullText : ArticleAction
    /** The user scrolled to the end: the article counts as read. */
    data object OnReachedEnd : ArticleAction
    data object OnRetry : ArticleAction

    // Handled by ArticleRoute
    data object OnBack : ArticleAction
    data object OnShareClick : ArticleAction
    data class OnArticleClick(val number: Int) : ArticleAction
    /** Previous / next article: replaces the current reader. */
    data class OnSiblingClick(val number: Int) : ArticleAction
    data class OnTermClick(val slug: String) : ArticleAction
    data object OnDiscussionClick : ArticleAction
    data object OnCasesClick : ArticleAction
    data class OnQuizClick(val quizId: String) : ArticleAction
    data object OnSignInClick : ArticleAction
}
