package uz.asos.feature.learn.hub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.learning.QuizDifficulty
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.learn.R
import uz.asos.feature.learn.hub.components.AchievementsRow
import uz.asos.feature.learn.hub.components.CasesPromoCard
import uz.asos.feature.learn.hub.components.DifficultySelector
import uz.asos.feature.learn.hub.components.LearnHeader
import uz.asos.feature.learn.hub.components.OverallProgressCard
import uz.asos.feature.learn.hub.components.SignInPromptCard
import uz.asos.feature.learn.hub.components.TopicTestCard

@Composable
internal fun LearnRoute(
    onOpenQuiz: (key: String) -> Unit,
    onOpenCases: () -> Unit,
    onOpenAchievements: () -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.onAction(LearnAction.OnResume)
        onPauseOrDispose { }
    }

    LearnScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is LearnAction.OnQuizClick -> onOpenQuiz(action.key)
                LearnAction.OnCasesClick -> onOpenCases()
                LearnAction.OnAchievementsClick -> onOpenAchievements()
                LearnAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun LearnScreen(
    uiState: LearnUiState,
    onAction: (LearnAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.quizzes.isEmpty() ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(36.dp, 144.dp, 170.dp, 24.dp, 48.dp, 300.dp),
                )

            uiState.errorMessage != null && uiState.quizzes.isEmpty() ->
                ErrorState(
                    title = stringResource(R.string.learn_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(LearnAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> LearnContent(uiState = uiState, onAction = onAction)
        }
    }
}

private val Gutter = 20.dp

@Composable
private fun LearnContent(
    uiState: LearnUiState,
    onAction: (LearnAction) -> Unit,
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = topInset + 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        item(key = "header") {
            LearnHeader(
                streakDays = uiState.streakDays,
                pointsLabel = uiState.pointsLabel,
                modifier = Modifier.padding(horizontal = Gutter),
            )
        }

        item(key = "progress") {
            val progress = uiState.progress
            if (progress != null) {
                OverallProgressCard(progress = progress, modifier = Modifier.padding(horizontal = Gutter))
            } else {
                SignInPromptCard(
                    onSignIn = { onAction(LearnAction.OnSignInClick) },
                    modifier = Modifier.padding(horizontal = Gutter),
                )
            }
        }

        item(key = "cases") {
            CasesPromoCard(
                cases = uiState.cases,
                onClick = { onAction(LearnAction.OnCasesClick) },
                modifier = Modifier.padding(horizontal = Gutter),
            )
        }

        item(key = "tests") {
            Column(
                modifier = Modifier.padding(horizontal = Gutter),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = stringResource(R.string.learn_tests_title),
                    style = AsosTextStyles.SectionTitle,
                    color = AsosColors.Navy,
                    modifier = Modifier.semantics { heading() },
                )
                DifficultySelector(
                    selected = uiState.selectedDifficulty,
                    onSelect = { onAction(LearnAction.OnDifficultySelect(it)) },
                )
                val quizzes = uiState.visibleQuizzes
                if (quizzes.isEmpty()) {
                    EmptyState(
                        title = stringResource(R.string.learn_tests_empty_title),
                        message = stringResource(R.string.learn_tests_empty_message),
                        icon = AsosIcons.ClipboardCheck,
                    )
                } else {
                    TopicTestCard(
                        quizzes = quizzes,
                        onQuizClick = { onAction(LearnAction.OnQuizClick(it)) },
                    )
                }
            }
        }

        if (uiState.achievements.isNotEmpty()) {
            item(key = "achievements") {
                AchievementsRow(
                    achievements = uiState.achievements,
                    onSeeAll = { onAction(LearnAction.OnAchievementsClick) },
                    gutter = Gutter,
                )
            }
        }
    }
}

private val previewState = LearnUiState(
    isLoading = false,
    isSignedIn = true,
    streakDays = 7,
    pointsLabel = "1 240",
    progress = OverallProgressUi(percent = 38, articlesRead = 59, articlesTotal = 155, goal = 3, done = 2),
    cases = CasesPromoUi(solved = 7, new = 5),
    selectedDifficulty = QuizDifficulty.MEDIUM,
    quizzes = listOf(
        QuizRowUi("shaxsiy-huquqlar", "Shaxsiy huquqlar", QuizDifficulty.MEDIUM, "shield", "brand", QuizRowState.IN_PROGRESS, "12 savol · 9/12 to‘g‘ri · Davom etish", 75),
        QuizRowUi("ijtimoiy-huquqlar", "Ijtimoiy huquqlar", QuizDifficulty.MEDIUM, "briefcase", "teal", QuizRowState.IN_PROGRESS, "15 savol · 6/15 to‘g‘ri", 40),
        QuizRowUi("siyosiy-huquqlar", "Siyosiy huquqlar", QuizDifficulty.MEDIUM, "ballot", "purple", QuizRowState.NEW, "10 savol · ≈ 6 daqiqa", 0),
        QuizRowUi("burchlar", "Fuqarolarning burchlari", QuizDifficulty.MEDIUM, "clipboard", "orange", QuizRowState.DONE, "8/8 to‘g‘ri · Tugallangan", 100),
        QuizRowUi("davlat-hokimiyati", "Davlat hokimiyati", QuizDifficulty.MEDIUM, "government", "navy", QuizRowState.NEW, "14 savol · ≈ 9 daqiqa", 0),
    ),
    achievements = listOf(
        AchievementUi("birinchi-qadam", "Birinchi qadam", "book articles_read", "brand", true, null),
        AchievementUi("seriya-7", "7 kunlik seriya", "flame streak_days", null, true, null),
        AchievementUi("burchlar", "Burchlar bilimdoni", "trophy quizzes_done", "teal", true, null),
        AchievementUi("kazus-ustasi", "Kazus ustasi", "scales cases_solved", null, false, "7 / 10"),
    ),
)

@AsosPreviews
@Composable
private fun LearnScreenPreview() {
    AsosTheme { LearnScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun LearnScreenGuestPreview() {
    AsosTheme {
        LearnScreen(
            uiState = previewState.copy(isSignedIn = false, streakDays = null, pointsLabel = null, progress = null),
            onAction = {},
        )
    }
}
