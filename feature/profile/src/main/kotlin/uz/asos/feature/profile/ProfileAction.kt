/*
 * ProfileAction.kt
 * Module: :feature:profile   ·   Work package: WP-11
 *
 * Everything the user can do on the Profil screen.
 */
package uz.asos.feature.profile

import uz.asos.core.model.user.BookmarkType
import uz.asos.core.model.user.ReaderTextSize

sealed interface ProfileAction {
    data object OnSettingsClick : ProfileAction

    data object OnAchievementsClick : ProfileAction

    /** A "Saqlanganlar" row: opens that list. */
    data class OnSavedClick(val type: BookmarkType) : ProfileAction

    data object OnPersonalDataClick : ProfileAction

    data class OnNotifyDailyChange(val enabled: Boolean) : ProfileAction

    data object OnTextSizeOpen : ProfileAction

    data object OnTextSizeDismiss : ProfileAction

    data class OnTextSizeSelect(val size: ReaderTextSize) : ProfileAction

    data object OnLanguageOpen : ProfileAction

    data object OnLanguageDismiss : ProfileAction

    data class OnLanguageSelect(val locale: String) : ProfileAction

    data object OnPrivacyClick : ProfileAction

    data object OnLogoutClick : ProfileAction

    data object OnSignInClick : ProfileAction

    data object OnNoticeDismiss : ProfileAction

    data object OnRetry : ProfileAction

    /** Screen is visible again: refresh points and streak quietly. */
    data object OnResume : ProfileAction
}
