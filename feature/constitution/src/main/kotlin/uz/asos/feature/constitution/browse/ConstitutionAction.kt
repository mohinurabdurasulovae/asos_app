package uz.asos.feature.constitution.browse

sealed interface ConstitutionAction {
    data class OnQueryChange(val query: String) : ConstitutionAction
    data class OnTabSelect(val tab: ConstitutionTab) : ConstitutionAction
    data class OnTopicSelect(val slug: String?) : ConstitutionAction
    data class OnTopicOpen(val slug: String) : ConstitutionAction
    data class OnSectionToggle(val roman: String) : ConstitutionAction
    data class OnChapterToggle(val roman: String) : ConstitutionAction
    data class OnChapterRetry(val roman: String) : ConstitutionAction
    data object OnSavedClick : ConstitutionAction
    data object OnRetry : ConstitutionAction
    data object OnListRetry : ConstitutionAction
    data object OnResume : ConstitutionAction

    data class OnArticleClick(val key: String) : ConstitutionAction
    data object OnSignInClick : ConstitutionAction
}
