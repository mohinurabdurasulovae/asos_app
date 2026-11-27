/*
 * ProfileViewModel.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * Loads the whole Profil screen in one call, works out how far the next level is, and saves
 * the settings rows. Guests get the sign-in card instead of a failed request.
 */
package uz.asos.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.asos.core.common.result.AsosResult
import uz.asos.core.common.text.UzbekFormatters
import uz.asos.core.data.repository.AuthRepository
import uz.asos.core.data.repository.UserRepository
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.model.user.UserProfile
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var hasLoaded = false

    init {
        observeSession()
        observeTextSize()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnNotifyDailyChange -> setNotifyDaily(action.enabled)
            ProfileAction.OnTextSizeOpen -> _uiState.update { it.copy(isTextSizeSheetOpen = true) }
            ProfileAction.OnTextSizeDismiss ->
                _uiState.update { it.copy(isTextSizeSheetOpen = false) }
            is ProfileAction.OnTextSizeSelect -> setTextSize(action.size)
            ProfileAction.OnLanguageOpen -> _uiState.update { it.copy(isLanguageSheetOpen = true) }
            ProfileAction.OnLanguageDismiss ->
                _uiState.update { it.copy(isLanguageSheetOpen = false) }
            is ProfileAction.OnLanguageSelect -> setLocale(action.locale)
            ProfileAction.OnNoticeDismiss -> _uiState.update { it.copy(notice = null) }
            ProfileAction.OnRetry -> load()
            ProfileAction.OnResume -> if (hasLoaded) load()
            // Not wired to an endpoint yet.
            ProfileAction.OnPersonalDataClick,
            ProfileAction.OnPrivacyClick,
            ProfileAction.OnSettingsClick,
            -> _uiState.update { it.copy(notice = NOT_READY) }
            // Navigation and sign-out are handled by ProfileRoute.
            ProfileAction.OnAchievementsClick,
            is ProfileAction.OnSavedClick,
            ProfileAction.OnLogoutClick,
            ProfileAction.OnSignInClick,
            -> Unit
        }
    }

    private fun observeSession() {
        viewModelScope.launch {
            authRepository.isSignedIn.collect { signedIn ->
                _uiState.update { it.copy(isSignedIn = signedIn) }
                if (signedIn) {
                    load()
                } else {
                    // Nothing to show a guest, and /v1/me/profile would only answer 401.
                    hasLoaded = false
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = null, user = null)
                    }
                }
            }
        }
    }

    private fun observeTextSize() {
        viewModelScope.launch {
            userRepository.readerTextSize.collect { size ->
                _uiState.update { it.copy(textSize = size) }
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = userRepository.getProfile()) {
                is AsosResult.Success -> {
                    hasLoaded = true
                    _uiState.update { it.withProfile(result.data) }
                }
                is AsosResult.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = if (it.user == null) result.message else null,
                        notice = if (it.user != null) result.message else null,
                    )
                }
            }
        }
    }

    private fun setNotifyDaily(enabled: Boolean) {
        val previous = _uiState.value.notifyDaily
        _uiState.update { it.copy(notifyDaily = enabled) }

        viewModelScope.launch {
            val result = userRepository.updateSettings(notifyDaily = enabled)
            if (result is AsosResult.Error) {
                _uiState.update { it.copy(notifyDaily = previous, notice = result.message) }
            }
        }
    }

    private fun setTextSize(size: ReaderTextSize) {
        _uiState.update { it.copy(textSize = size, isTextSizeSheetOpen = false) }
        viewModelScope.launch {
            // The device setting is applied by the repository even if the call fails.
            val result = userRepository.updateSettings(textSize = size)
            if (result is AsosResult.Error && _uiState.value.isSignedIn) {
                _uiState.update { it.copy(notice = result.message) }
            }
        }
    }

    private fun setLocale(locale: String) {
        val previous = _uiState.value.user?.locale
        _uiState.update { state ->
            state.copy(
                isLanguageSheetOpen = false,
                user = state.user?.copy(locale = locale),
            )
        }
        viewModelScope.launch {
            val result = userRepository.updateSettings(locale = locale)
            if (result is AsosResult.Error) {
                _uiState.update { state ->
                    state.copy(
                        user = state.user?.copy(locale = previous),
                        notice = result.message,
                    )
                }
            }
        }
    }

    private companion object {
        const val NOT_READY = "Bu bo‘lim hali tayyor emas."
    }
}

/** Fills the screen from one GET /v1/me/profile response. */
private fun ProfileUiState.withProfile(profile: UserProfile): ProfileUiState = copy(
    isLoading = false,
    errorMessage = null,
    user = profile.user,
    pointsLabel = UzbekFormatters.groupThousands(profile.stats.points),
    quizzesDone = profile.stats.quizzesDone,
    casesSolved = profile.stats.casesSolved,
    week = profile.week,
    levelProgress = profile.levelProgress(),
    achievementsEarned = profile.achievementsEarned,
    achievementsTotal = profile.achievementsTotal,
    achievements = profile.achievements,
    bookmarks = profile.bookmarks,
    notifyDaily = profile.user.notifyDaily,
)

/** null on the last level, where there is nothing left to climb to. */
private fun UserProfile.levelProgress(): LevelProgressUi? {
    val nextAt = user.level.nextAt ?: return null
    val floor = user.level.minPoints
    val span = (nextAt - floor).coerceAtLeast(1)
    val done = (stats.points - floor).coerceIn(0, span)

    return LevelProgressUi(
        nextLevel = user.level.number + 1,
        pointsLabel = UzbekFormatters.groupThousands(stats.points),
        targetLabel = UzbekFormatters.groupThousands(nextAt),
        fraction = done.toFloat() / span.toFloat(),
    )
}
