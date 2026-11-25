/*
 * ConstitutionAction.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * User actions the screen sends. Navigation actions (OnArticleClick, OnContinueClick) are
 * handled by ConstitutionRoute; the rest go to ConstitutionViewModel.
 */
package uz.asos.feature.constitution.browse

sealed interface ConstitutionAction {
    data class OnQueryChange(val query: String) : ConstitutionAction
    data class OnTabSelect(val tab: ConstitutionTab) : ConstitutionAction
    /** null = "Barchasi". */
    data class OnTopicSelect(val slug: String?) : ConstitutionAction
    /** From the Mavzular tab: pick a topic and show its articles. */
    data class OnTopicOpen(val slug: String) : ConstitutionAction
    data class OnSectionToggle(val roman: String) : ConstitutionAction
    data class OnChapterToggle(val roman: String) : ConstitutionAction
    data class OnChapterRetry(val roman: String) : ConstitutionAction
    data object OnSavedClick : ConstitutionAction
    data object OnRetry : ConstitutionAction
    data object OnListRetry : ConstitutionAction
    /** Screen came back to the foreground: refresh reading progress quietly. */
    data object OnResume : ConstitutionAction

    // Navigation (handled in ConstitutionRoute)
    data class OnArticleClick(val key: String) : ConstitutionAction
    data object OnSignInClick : ConstitutionAction
}
