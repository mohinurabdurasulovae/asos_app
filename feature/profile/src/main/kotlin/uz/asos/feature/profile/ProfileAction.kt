package uz.asos.feature.profile

import uz.asos.core.model.user.BookmarkType
import uz.asos.core.model.user.ReaderTextSize

sealed interface ProfileAction {
    data object OnSettingsClick : ProfileAction

    data object OnAchievementsClick : ProfileAction

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

    data object OnResume : ProfileAction
}
