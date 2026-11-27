/*
 * CaseDetailScreen.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazus — yechim (design artboard: Kazus.dc.html)
 *
 * Layout (top to bottom, 16 dp gutters, 16 dp gaps):
 *   top bar (back · "Kazus № 14" · bookmark)
 *   CaseStepper
 *   title + topic and difficulty tags
 *   CaseScenarioCard
 *   ArticleChoiceList
 *   SolutionInput
 *   after checking: ScoreBanner · LawyerExplanationCard · CaseFooterActions
 *   ArticlePreviewSheet behind "Moddani o'qish"
 *
 * CaseDetailRoute collects the ViewModel; CaseDetailScreen(uiState, onAction) is stateless
 * and previewable.
 */
package uz.asos.feature.cases.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.ArticleOption
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseTopic
import uz.asos.feature.cases.data.model.LawyerComment
import uz.asos.feature.cases.detail.components.ArticleChoiceList
import uz.asos.feature.cases.detail.components.ArticlePreviewSheet
import uz.asos.feature.cases.detail.components.CaseFooterActions
import uz.asos.feature.cases.detail.components.CaseScenarioCard
import uz.asos.feature.cases.detail.components.CaseStepper
import uz.asos.feature.cases.detail.components.LawyerExplanationCard
import uz.asos.feature.cases.detail.components.ScoreBanner
import uz.asos.feature.cases.detail.components.SolutionInput
import uz.asos.feature.cases.ui.tone

@Composable
internal fun CaseDetailRoute(
    onBack: () -> Unit,
    onOpenCase: (id: Int) -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CaseDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CaseDetailScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                CaseDetailAction.OnBackClick -> onBack()
                CaseDetailAction.OnNextCaseClick -> uiState.nextCaseId?.let(onOpenCase)
                is CaseDetailAction.OnOpenArticleReader -> onOpenArticle(action.number)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CaseDetailScreen(
    uiState: CaseDetailUiState,
    onAction: (CaseDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = AsosColors.Background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AsosColors.Background),
                navigationIcon = {
                    IconButton(onClick = { onAction(CaseDetailAction.OnBackClick) }) {
                        Icon(
                            imageVector = AsosIcons.ChevronLeft,
                            contentDescription = stringResource(R.string.cases_back),
                            tint = AsosColors.Navy,
                        )
                    }
                },
                title = {
                    Text(
                        text = uiState.titleLabel,
                        style = AsosTextStyles.CardTitle,
                    )
                },
                actions = {
                    IconButton(onClick = { onAction(CaseDetailAction.OnBookmarkToggle) }) {
                        Icon(
                            imageVector = if (uiState.isBookmarked) {
                                AsosIcons.BookmarkFilled
                            } else {
                                AsosIcons.Bookmark
                            },
                            contentDescription = stringResource(R.string.cases_bookmark),
                            tint = if (uiState.isBookmarked) AsosColors.Brand600 else AsosColors.Navy,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        when {
            uiState.isLoading ->
                LoadingState(
                    modifier = Modifier.padding(innerPadding),
                    blocks = listOf(24.dp, 40.dp, 220.dp, 72.dp, 72.dp, 110.dp),
                )

            uiState.errorMessage != null && uiState.title.isEmpty() ->
                ErrorState(
                    title = stringResource(R.string.cases_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(CaseDetailAction.OnReload) },
                    modifier = Modifier.padding(innerPadding),
                )

            else -> CaseDetailContent(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }

    uiState.previewArticle?.let { article ->
        ArticlePreviewSheet(
            article = article,
            onOpenReader = { number -> onAction(CaseDetailAction.OnOpenArticleReader(number)) },
            onDismiss = { onAction(CaseDetailAction.OnArticlePreviewDismiss) },
        )
    }
}

@Composable
private fun CaseDetailContent(
    uiState: CaseDetailUiState,
    onAction: (CaseDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
    ) {
        CaseStepper(currentStep = uiState.currentStep)

        Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
            Text(
                text = uiState.title,
                style = AsosTextStyles.ScreenTitle.copy(fontSize = 25.sp, lineHeight = 31.sp),
                modifier = Modifier.semantics { heading() },
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                uiState.topic?.let { topic ->
                    AsosTag(text = topic.label, tone = topic.tone())
                }
                uiState.difficulty?.let { difficulty ->
                    AsosTag(text = difficulty.label, tone = AsosTones.Neutral)
                }
            }
        }

        uiState.persona?.let { persona ->
            CaseScenarioCard(
                persona = persona,
                storyText = uiState.storyText,
                questionText = uiState.questionText,
            )
        }

        ArticleChoiceList(
            options = uiState.options,
            isSubmitted = uiState.isSubmitted,
            onToggle = { number -> onAction(CaseDetailAction.OnArticleToggle(number)) },
        )

        SolutionInput(
            solution = uiState.userSolution,
            isSubmitted = uiState.isSubmitted,
            canSubmit = uiState.canSubmit,
            onSolutionChange = { text -> onAction(CaseDetailAction.OnSolutionChange(text)) },
            onSubmit = { onAction(CaseDetailAction.OnSubmit) },
        )

        AnimatedVisibility(
            visible = uiState.isSubmitted,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.mm)) {
                ScoreBanner(
                    isCorrect = uiState.isCorrect,
                    points = uiState.rewardPoints,
                )

                uiState.lawyerComment?.let { comment ->
                    LawyerExplanationCard(
                        comment = comment,
                        correctArticles = uiState.correctArticles,
                        onReadArticleClick = { article ->
                            onAction(CaseDetailAction.OnArticlePreviewOpen(article))
                        },
                    )
                }

                CaseFooterActions(
                    hasNextCase = uiState.hasNextCase,
                    onRetry = { onAction(CaseDetailAction.OnRetry) },
                    onNextCase = { onAction(CaseDetailAction.OnNextCaseClick) },
                )
            }
        }

        Spacer(modifier = Modifier.height(AsosSpacing.xl))
    }
}

private val previewArticle31 = ArticleOption(
    number = 31,
    title = "Shaxsiy hayot va yozishmalar siri",
    officialText = "Har bir inson shaxsiy hayotining daxlsizligi, shaxsiy va oilaviy sirga " +
        "ega bo‘lish, o‘z sha’ni va qadr-qimmatini himoya qilish huquqiga ega.",
    simpleExplanation = "Yozishmalaringiz, telefoningiz va uy-joyingiz qonun bilan himoyalangan.",
)

private val previewState = CaseDetailUiState(
    isLoading = false,
    titleLabel = "Kazus № 14",
    title = "Direktor va telefon",
    topic = CaseTopic.SHAXSIY_HAYOT,
    difficulty = CaseDifficulty.ORTA,
    persona = PersonaUi(initials = "T", name = "Temur, 16 yosh", subtitle = "10-sinf o‘quvchisi"),
    storyText = "Dars paytida Temurning telefoni jiringlab qoldi. O‘qituvchi telefonni olib, " +
        "direktorga topshirdi. Tanaffusda direktor telefonni ochib, Temurning do‘stlari bilan " +
        "yozishmalarini o‘qib chiqdi.",
    questionText = "Direktor o‘z vakolati doirasida harakat qildimi?",
    options = listOf(
        ArticleChoiceUi(previewArticle31, isSelected = true, isCorrectOption = true),
        ArticleChoiceUi(
            option = ArticleOption(
                number = 33,
                title = "Fikrlash va so‘z erkinligi",
                officialText = "Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega.",
                simpleExplanation = "O‘z fikringizni erkin ifoda etish huquqingiz bor.",
            ),
            isSelected = false,
            isCorrectOption = false,
        ),
    ),
    userSolution = "Direktor telefonni olib qo‘yishi mumkin, lekin yozishmalarni o‘qishga " +
        "haqqi yo‘q.",
    rewardPoints = 40,
    correctArticles = listOf(previewArticle31),
)

@AsosPreviews
@Composable
private fun CaseDetailScreenPreview() {
    AsosTheme { CaseDetailScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun CaseDetailScreenSolvedPreview() {
    AsosTheme {
        CaseDetailScreen(
            uiState = previewState.copy(
                isSubmitted = true,
                isCorrect = true,
                nextCaseId = 13,
                lawyerComment = LawyerComment(
                    initials = "MY",
                    fullName = "Malika Yusupova",
                    role = "advokat",
                    paragraphs = listOf(
                        "31-modda har kimga shaxsiy hayot daxlsizligini kafolatlaydi.",
                    ),
                    practicalSteps = listOf(
                        "Voqeani ota-onangizga xotirjam aytib bering.",
                        "Maktab ma’muriyatidan ichki tartib qoidalarini ko‘rsatishni so‘rang.",
                    ),
                ),
            ),
            onAction = {},
        )
    }
}
