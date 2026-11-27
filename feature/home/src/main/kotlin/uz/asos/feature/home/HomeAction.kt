package uz.asos.feature.home

sealed interface HomeAction {
    data object OnRetry : HomeAction
    data object OnResume : HomeAction
    data object OnSaveArticleClick : HomeAction
    data class OnQuizOptionClick(val optionId: Int) : HomeAction
    data object OnNotificationsClick : HomeAction

    data object OnProfileClick : HomeAction
    data object OnSearchClick : HomeAction
    data class OnArticleClick(val key: String) : HomeAction
    data object OnAllTopicsClick : HomeAction
    data class OnTopicClick(val slug: String) : HomeAction
    data object OnContinueAllClick : HomeAction
    data class OnCaseClick(val caseId: String) : HomeAction
    data object OnLegalHelpClick : HomeAction
    data object OnDictionaryClick : HomeAction
    data object OnAssistantClick : HomeAction
    data object OnAllPostsClick : HomeAction
    data class OnPostClick(val postId: Int) : HomeAction
}
