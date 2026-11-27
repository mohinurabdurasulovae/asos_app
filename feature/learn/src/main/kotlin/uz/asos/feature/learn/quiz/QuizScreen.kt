/*
 * QuizScreen.kt
 * Module: :feature:learn   ·   Work package: WP-07
 * Screen: Test savoli (design artboard: Test.dc.html)
 *
 * Full-screen quiz (no bottom bar), white background:
 *   QuizTopBar (X · progress strip · 5/10)
 *   QuestionCard (quiz chip · difficulty chip · timer · question)
 *   AnswerOptionList — tapping an option answers it
 *   bottom: PickAnswerBar before answering, ExplanationPanel after
 * Other phases: loading skeleton, sign-in prompt, error, QuizResultContent.
 *
 * QuizRoute collects the ViewModel; QuizScreen(uiState, onAction) is stateless and previewable.
 */
package uz.asos.feature.learn.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.learning.QuestionMark
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.learn.R
import uz.asos.feature.learn.quiz.components.AnswerOptionList
import uz.asos.feature.learn.quiz.components.ExplanationPanel
import uz.asos.feature.learn.quiz.components.PickAnswerBar
import uz.asos.feature.learn.quiz.components.QuestionCard
import uz.asos.feature.learn.quiz.components.QuizCloseButton
import uz.asos.feature.learn.quiz.components.QuizResultContent
import uz.asos.feature.learn.quiz.components.QuizTopBar

@Composable
internal fun QuizRoute(
    onClose: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is QuizEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    QuizScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                QuizAction.OnCloseClick -> onClose()
                QuizAction.OnSignInClick -> onSignIn()
                is QuizAction.OnArticleClick -> onOpenArticle(action.number)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun QuizScreen(
    uiState: QuizUiState,
    onAction: (QuizAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Surface),
    ) {
        when (uiState.phase) {
            QuizPhase.LOADING -> LoadingState(
                modifier = Modifier.statusBarsPadding(),
                blocks = listOf(44.dp, 30.dp, 92.dp, 62.dp, 62.dp, 62.dp, 62.dp),
            )

            QuizPhase.NEEDS_SIGN_IN -> MessageLayout(onClose = { onAction(QuizAction.OnCloseClick) }) {
                EmptyState(
                    title = stringResource(R.string.learn_quiz_signin_title),
                    message = stringResource(R.string.learn_quiz_signin_message),
                    icon = AsosIcons.Lock,
                    actionLabel = stringResource(R.string.learn_signin_action),
                    onAction = { onAction(QuizAction.OnSignInClick) },
                )
            }

            QuizPhase.ERROR -> MessageLayout(onClose = { onAction(QuizAction.OnCloseClick) }) {
                ErrorState(
                    title = stringResource(R.string.learn_quiz_load_error_title),
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = { onAction(QuizAction.OnRetry) },
                )
            }

            QuizPhase.QUESTION -> QuestionContent(uiState = uiState, onAction = onAction)

            QuizPhase.RESULT -> uiState.result?.let { result ->
                QuizResultContent(
                    result = result,
                    onRestart = { onAction(QuizAction.OnRestartClick) },
                    onClose = { onAction(QuizAction.OnCloseClick) },
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 96.dp),
        )
    }
}

@Composable
private fun QuestionContent(
    uiState: QuizUiState,
    onAction: (QuizAction) -> Unit,
) {
    val question = uiState.question ?: return
    val feedback = uiState.feedback
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            QuizTopBar(
                marks = uiState.marks,
                currentNumber = uiState.currentNumber,
                total = uiState.total,
                onClose = { onAction(QuizAction.OnCloseClick) },
            )
            QuestionCard(
                quizTitle = uiState.quizTitle,
                difficultyLabel = uiState.difficultyLabel,
                questionStartedAt = uiState.questionStartedAt,
                frozenSeconds = uiState.answeredAfterSeconds,
                question = question.body,
            )
            AnswerOptionList(
                options = question.options,
                selectedOptionId = uiState.selectedOptionId,
                feedback = feedback,
                enabled = feedback == null && !uiState.isSubmitting,
                onOptionClick = { onAction(QuizAction.OnOptionClick(it)) },
            )
        }
        if (feedback != null) {
            ExplanationPanel(
                feedback = feedback,
                isFinishing = uiState.isFinishing,
                onArticleClick = { onAction(QuizAction.OnArticleClick(it)) },
                onFinishClick = { onAction(QuizAction.OnFinishClick) },
                onNextClick = { onAction(QuizAction.OnNextClick) },
            )
        } else {
            PickAnswerBar(isSubmitting = uiState.isSubmitting)
        }
    }
}

/** X button at the top, message in the middle (sign-in and error phases). */
@Composable
private fun MessageLayout(
    onClose: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
    ) {
        QuizCloseButton(onClose = onClose)
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { content() }
    }
}

// ---------------------------------------------------------------------- previews

private val previewQuestion = QuizUiState(
    phase = QuizPhase.QUESTION,
    quizTitle = "Shaxsiy huquqlar",
    difficultyLabel = "O‘rta",
    marks = listOf(
        QuestionMark.CORRECT, QuestionMark.CORRECT, QuestionMark.WRONG, QuestionMark.CORRECT, QuestionMark.CURRENT,
    ) + List(5) { QuestionMark.PENDING },
    currentNumber = 5,
    total = 10,
    question = QuestionUi(
        id = 5,
        body = "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?",
        options = listOf(
            OptionUi(1, "A", "24 soat"),
            OptionUi(2, "B", "48 soat"),
            OptionUi(3, "C", "72 soat"),
            OptionUi(4, "D", "12 soat"),
        ),
    ),
    answeredAfterSeconds = 42,
)

@AsosPreviews
@Composable
private fun QuizScreenAnsweredPreview() {
    AsosTheme {
        QuizScreen(
            uiState = previewQuestion.copy(
                selectedOptionId = 2,
                marks = previewQuestion.marks.toMutableList().also { it[4] = QuestionMark.CORRECT },
                feedback = FeedbackUi(
                    isCorrect = true,
                    correctOptionId = 2,
                    pointsEarned = 20,
                    correctAnswerText = null,
                    explanation = "27-moddaga ko‘ra, shaxsni sud qarorisiz 48 soatdan ortiq ushlab turish mumkin emas. " +
                        "Bu qoida erkinlik va shaxsiy daxlsizlik huquqini himoya qiladi.",
                    articleNumber = 27,
                    isLastQuestion = false,
                ),
            ),
            onAction = {},
        )
    }
}

@AsosPreviews
@Composable
private fun QuizScreenNotAnsweredPreview() {
    AsosTheme { QuizScreen(uiState = previewQuestion.copy(answeredAfterSeconds = null), onAction = {}) }
}

@AsosPreviews
@Composable
private fun QuizScreenSignInPreview() {
    AsosTheme { QuizScreen(uiState = QuizUiState(phase = QuizPhase.NEEDS_SIGN_IN), onAction = {}) }
}
