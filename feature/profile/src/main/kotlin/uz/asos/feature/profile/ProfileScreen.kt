/*
 * ProfileScreen.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * Layout (top to bottom):
 *   ProfileHeader (Navy hero: name, level, StatsRow)
 *   WeeklyActivityChart ("Bu hafta" + the next-level bar)
 *   AchievementsGrid
 *   SavedItemsSection
 *   SettingsSection
 *
 * Profil is a tab, so there is no back button. Guests see the sign-in card instead.
 * ProfileRoute collects the ViewModel; ProfileScreen(uiState, onAction) is stateless.
 */
package uz.asos.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.user.BookmarkType
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.profile.components.AchievementsGrid
import uz.asos.feature.profile.components.ChoiceSheet
import uz.asos.feature.profile.components.ProfileHeader
import uz.asos.feature.profile.components.SavedItemsSection
import uz.asos.feature.profile.components.SettingsSection
import uz.asos.feature.profile.components.SheetChoice
import uz.asos.feature.profile.components.label

@Composable
internal fun ProfileRoute(
    onOpenSaved: (BookmarkType) -> Unit,
    onSignIn: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Back from a quiz or a case: points and the streak may have moved.
    LifecycleResumeEffect(Unit) {
        viewModel.onAction(ProfileAction.OnResume)
        onPauseOrDispose { }
    }

    ProfileScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is ProfileAction.OnSavedClick -> onOpenSaved(action.type)
                ProfileAction.OnSignInClick -> onSignIn()
                ProfileAction.OnLogoutClick -> onLogout()
                ProfileAction.OnAchievementsClick -> Unit
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun ProfileScreen(
    uiState: ProfileUiState,
    onAction: (ProfileAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        val user = uiState.user
        when {
            !uiState.isSignedIn -> GuestCard(onSignIn = { onAction(ProfileAction.OnSignInClick) })

            uiState.isLoading && user == null ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(140.dp, 170.dp, 110.dp, 160.dp, 220.dp),
                )

            uiState.errorMessage != null && user == null ->
                ErrorState(
                    title = stringResource(R.string.profile_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(ProfileAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            user != null -> ProfileContent(uiState = uiState, onAction = onAction)
        }

        uiState.notice?.let { notice ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(AsosSpacing.l),
                containerColor = AsosColors.Navy,
            ) {
                Text(text = notice, style = AsosTextStyles.BodySmall, color = AsosColors.Surface)
            }
        }
    }

    if (uiState.isTextSizeSheetOpen) {
        ChoiceSheet(
            title = stringResource(R.string.profile_text_size),
            choices = ReaderTextSize.entries.map {
                SheetChoice(value = it, label = stringResource(it.label()))
            },
            selectedValue = uiState.textSize,
            onSelect = { onAction(ProfileAction.OnTextSizeSelect(it)) },
            onDismiss = { onAction(ProfileAction.OnTextSizeDismiss) },
        )
    }

    if (uiState.isLanguageSheetOpen) {
        ChoiceSheet(
            title = stringResource(R.string.profile_language),
            choices = listOf(
                SheetChoice("uz-Latn", stringResource(R.string.profile_language_uz_latn)),
                SheetChoice("uz-Cyrl", stringResource(R.string.profile_language_uz_cyrl)),
                SheetChoice("ru", stringResource(R.string.profile_language_ru)),
                SheetChoice("en", stringResource(R.string.profile_language_en)),
            ),
            selectedValue = uiState.user?.locale ?: "uz-Latn",
            onSelect = { onAction(ProfileAction.OnLanguageSelect(it)) },
            onDismiss = { onAction(ProfileAction.OnLanguageDismiss) },
        )
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    onAction: (ProfileAction) -> Unit,
) {
    val user = uiState.user ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = AsosSpacing.xxxl),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.xl),
    ) {
        item {
            ProfileHeader(
                user = user,
                pointsLabel = uiState.pointsLabel,
                quizzesDone = uiState.quizzesDone,
                casesSolved = uiState.casesSolved,
                onSettingsClick = { onAction(ProfileAction.OnSettingsClick) },
            )
        }

        uiState.week?.let { week ->
            item {
                WeeklyActivityCard(
                    week = week,
                    levelProgress = uiState.levelProgress,
                )
            }
        }

        if (uiState.achievements.isNotEmpty()) {
            item {
                AchievementsGrid(
                    achievements = uiState.achievements,
                    earned = uiState.achievementsEarned,
                    total = uiState.achievementsTotal,
                    onSeeAll = { onAction(ProfileAction.OnAchievementsClick) },
                    modifier = Modifier.padding(horizontal = AsosSpacing.xl),
                )
            }
        }

        item {
            SavedItemsSection(
                counts = uiState.bookmarks,
                onOpen = { onAction(ProfileAction.OnSavedClick(it)) },
                modifier = Modifier.padding(horizontal = AsosSpacing.xl),
            )
        }

        item {
            SettingsSection(
                notifyDaily = uiState.notifyDaily,
                locale = user.locale,
                textSize = uiState.textSize,
                onPersonalData = { onAction(ProfileAction.OnPersonalDataClick) },
                onNotifyDailyChange = { onAction(ProfileAction.OnNotifyDailyChange(it)) },
                onLanguage = { onAction(ProfileAction.OnLanguageOpen) },
                onTextSize = { onAction(ProfileAction.OnTextSizeOpen) },
                onPrivacy = { onAction(ProfileAction.OnPrivacyClick) },
                onLogout = { onAction(ProfileAction.OnLogoutClick) },
                modifier = Modifier.padding(horizontal = AsosSpacing.xl),
            )
        }
    }
}

@Composable
private fun WeeklyActivityCard(
    week: uz.asos.core.model.user.WeekActivity,
    levelProgress: LevelProgressUi?,
) {
    Box(modifier = Modifier.padding(horizontal = AsosSpacing.xl)) {
        uz.asos.feature.profile.components.WeeklyActivityChart(
            week = week,
            levelProgress = levelProgress,
        )
    }
}

@Composable
private fun GuestCard(onSignIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(AsosSpacing.xl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyState(
            title = stringResource(R.string.profile_guest_title),
            message = stringResource(R.string.profile_guest_message),
            icon = AsosIcons.Person,
        )
        AsosButton(
            text = stringResource(R.string.profile_guest_action),
            onClick = onSignIn,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AsosSpacing.l),
        )
    }
}

@AsosPreviews
@Composable
private fun ProfileScreenGuestPreview() {
    AsosTheme {
        ProfileScreen(
            uiState = ProfileUiState(isLoading = false, isSignedIn = false),
            onAction = {},
        )
    }
}
