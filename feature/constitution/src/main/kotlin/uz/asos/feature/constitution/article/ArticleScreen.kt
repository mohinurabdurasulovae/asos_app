/*
 * ArticleScreen.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Modda (50-modda) (design artboard: Modda.dc.html)
 *
 * Article reader. ArticleRoute connects the ViewModel, snackbars, sharing and navigation;
 * ArticleScreen(uiState, onAction) is stateless and previewable.
 *
 * Layout (top to bottom, as on the canvas):
 *   ArticleTopBar
 *   ArticleHeader · ArticlePartsTabs · OfficialTextCard · PlainSummarySection
 *   RealLifeExamplesSection · RelatedArticlesSection + GlossaryTermChips + DiscussionCard
 *   ArticleActionsBar (fixed at the bottom) · TextSizeSheet (from "Aa")
 */
package uz.asos.feature.constitution.article

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.user.ReaderTextSize
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.article.components.ArticleActionsBar
import uz.asos.feature.constitution.article.components.ArticleHeader
import uz.asos.feature.constitution.article.components.ArticlePartsTabs
import uz.asos.feature.constitution.article.components.ArticleTopBar
import uz.asos.feature.constitution.article.components.DiscussionCard
import uz.asos.feature.constitution.article.components.GlossaryTermChips
import uz.asos.feature.constitution.article.components.OfficialTextCard
import uz.asos.feature.constitution.article.components.PlainSummarySection
import uz.asos.feature.constitution.article.components.ReaderSection
import uz.asos.feature.constitution.article.components.RealLifeExamplesSection
import uz.asos.feature.constitution.article.components.RelatedArticlesSection
import uz.asos.feature.constitution.article.components.TextSizeSheet

/** Reading at least this long, then reaching the end, marks the article as read. */
private const val MIN_READ_MILLIS = 5_000L

@Composable
internal fun ArticleRoute(
    onBack: () -> Unit,
    onOpenArticle: (key: String) -> Unit,
    onReplaceArticle: (key: String) -> Unit,
    onOpenTerm: (slug: String) -> Unit,
    onOpenCommunity: (articleNumber: Int) -> Unit,
    onOpenCases: () -> Unit,
    onOpenQuiz: (quizId: String) -> Unit,
    onSignIn: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ArticleEvent.PointsEarned -> snackbarHostState.showSnackbar(
                    context.getString(R.string.constitution_points_earned, event.points),
                )
                ArticleEvent.SignInRequired -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.constitution_saved_signin_title),
                        actionLabel = context.getString(R.string.constitution_signin_action),
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) onSignIn()
                }
                is ArticleEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    ArticleScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            val success = uiState as? ArticleUiState.Success
            when (action) {
                ArticleAction.OnBack -> onBack()
                ArticleAction.OnShareClick -> success?.let {
                    shareArticle(context, it.article.shareText, context.getString(R.string.constitution_share_chooser))
                }
                is ArticleAction.OnArticleClick -> onOpenArticle(action.number.toString())
                is ArticleAction.OnSiblingClick -> onReplaceArticle(action.number.toString())
                is ArticleAction.OnTermClick -> onOpenTerm(action.slug)
                ArticleAction.OnDiscussionClick -> success?.let { onOpenCommunity(it.article.number) }
                ArticleAction.OnCasesClick -> onOpenCases()
                is ArticleAction.OnQuizClick -> onOpenQuiz(action.quizId)
                ArticleAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun ArticleScreen(
    uiState: ArticleUiState,
    onAction: (ArticleAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var showTextSizeSheet by rememberSaveable { mutableStateOf(false) }
    val success = uiState as? ArticleUiState.Success

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AsosColors.Surface,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ArticleTopBar(
                sectionRoman = success?.article?.sectionRoman,
                chapterRoman = success?.article?.chapterRoman,
                isSaved = success?.isSaved == true,
                onBack = { onAction(ArticleAction.OnBack) },
                onTextSizeClick = { if (success != null) showTextSizeSheet = true },
                onBookmarkClick = { onAction(ArticleAction.OnBookmarkClick) },
                onShareClick = { onAction(ArticleAction.OnShareClick) },
            )
        },
        bottomBar = {
            if (success != null) {
                ArticleActionsBar(
                    previousNumber = success.article.previousNumber,
                    nextNumber = success.article.nextNumber,
                    quizId = success.article.quizId,
                    onSiblingClick = { onAction(ArticleAction.OnSiblingClick(it)) },
                    onQuizClick = { onAction(ArticleAction.OnQuizClick(it)) },
                )
            }
        },
    ) { padding ->
        when (uiState) {
            ArticleUiState.Loading -> LoadingState(
                modifier = Modifier.padding(padding),
                blocks = listOf(16.dp, 46.dp, 24.dp, 44.dp, 260.dp, 180.dp),
            )
            is ArticleUiState.Error -> ErrorState(
                title = stringResource(R.string.constitution_article_load_error_title),
                message = uiState.message,
                onRetry = { onAction(ArticleAction.OnRetry) },
                modifier = Modifier.padding(padding),
            )
            is ArticleUiState.Success -> ArticleContent(
                state = uiState,
                onAction = onAction,
                contentPadding = padding,
            )
        }
    }

    if (showTextSizeSheet && success != null) {
        TextSizeSheet(
            current = success.textSize,
            onSelect = { onAction(ArticleAction.OnTextSizeSelect(it)) },
            onDismiss = { showTextSizeSheet = false },
        )
    }
}

private const val TABS_INDEX = 1
private const val FIRST_SECTION_INDEX = 2

@Composable
private fun ArticleContent(
    state: ArticleUiState.Success,
    onAction: (ArticleAction) -> Unit,
    contentPadding: PaddingValues,
) {
    val article = state.article
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val sections = remember(article) {
        buildList {
            add(ReaderSection.OFFICIAL)
            if (article.summary != null) add(ReaderSection.PLAIN)
            if (article.examples.isNotEmpty()) add(ReaderSection.EXAMPLES)
            if (article.related.isNotEmpty() || article.terms.isNotEmpty() || article.discussion != null) {
                add(ReaderSection.RELATED)
            }
        }
    }
    val selectedSection by remember(sections) {
        derivedStateOf { currentSection(listState, sections) }
    }

    // Reaching the end after reading for a few seconds marks the article as read.
    LaunchedEffect(listState, article.key) {
        delay(MIN_READ_MILLIS)
        snapshotFlow { listState.layoutInfo.totalItemsCount > 0 && !listState.canScrollForward }.first { it }
        onAction(ArticleAction.OnReachedEnd)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
        contentPadding = PaddingValues(top = 24.dp, bottom = contentPadding.calculateBottomPadding() + 32.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item(key = "header") {
            ArticleHeader(
                chapterTitle = article.chapterTitle,
                heading = article.heading,
                title = article.title,
                topics = article.topics,
                readingTime = article.readingTime,
                effectiveFrom = article.effectiveFrom,
                modifier = Modifier.padding(horizontal = Gutter),
            )
        }
        item(key = "tabs") {
            ArticlePartsTabs(
                sections = sections,
                selected = selectedSection,
                onSelect = { section ->
                    val index = FIRST_SECTION_INDEX + sections.indexOf(section)
                    scope.launch { listState.animateScrollToItem(index) }
                },
            )
        }
        sections.forEach { section ->
            item(key = section.name) {
                when (section) {
                    ReaderSection.OFFICIAL -> OfficialTextCard(
                        parts = article.parts,
                        showAll = state.showFullText,
                        collapsedCount = article.collapsedPartCount,
                        textSize = state.textSize,
                        sourceLabel = article.sourceLabel,
                        onToggleShowAll = { onAction(ArticleAction.OnToggleFullText) },
                        modifier = Modifier.padding(horizontal = Gutter),
                    )
                    ReaderSection.PLAIN -> article.summary?.let {
                        PlainSummarySection(summary = it, modifier = Modifier.padding(horizontal = Gutter))
                    }
                    ReaderSection.EXAMPLES -> RealLifeExamplesSection(
                        examples = article.examples,
                        onCasesClick = { onAction(ArticleAction.OnCasesClick) },
                        modifier = Modifier.padding(horizontal = Gutter),
                    )
                    ReaderSection.RELATED -> RelatedBlock(article = article, onAction = onAction)
                }
            }
        }
    }
}

private val Gutter = 24.dp

@Composable
private fun RelatedBlock(
    article: ArticleUi,
    onAction: (ArticleAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Gutter),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (article.related.isNotEmpty()) {
            RelatedArticlesSection(
                related = article.related,
                onArticleClick = { onAction(ArticleAction.OnArticleClick(it)) },
            )
        }
        if (article.terms.isNotEmpty()) {
            GlossaryTermChips(
                terms = article.terms,
                onTermClick = { onAction(ArticleAction.OnTermClick(it)) },
            )
        }
        article.discussion?.let { discussion ->
            DiscussionCard(
                discussion = discussion,
                onClick = { onAction(ArticleAction.OnDiscussionClick) },
            )
        }
    }
}

/** The section whose item is at (or just above) the top of the list. */
private fun currentSection(listState: LazyListState, sections: List<ReaderSection>): ReaderSection {
    val first = listState.firstVisibleItemIndex
    if (first <= TABS_INDEX) return sections.first()
    val position = first - FIRST_SECTION_INDEX
    return sections.getOrElse(position) { sections.last() }
}

private fun shareArticle(context: Context, text: String, chooserTitle: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(send, chooserTitle))
}

// ---------------------------------------------------------------------- previews

internal val previewArticle = ArticleUi(
    key = "50",
    id = 50,
    number = 50,
    isPreamble = false,
    sectionRoman = "II",
    chapterRoman = "IX",
    chapterTitle = "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar",
    heading = "50-modda",
    title = "Ta’lim olish huquqi",
    topics = listOf(TagUi("Ta’lim", "teal")),
    readingTime = "4 daqiqa",
    effectiveFrom = "01.05.2023",
    parts = listOf(
        PartUi(1, "Har kim ta’lim olish huquqiga ega.", emptyList()),
        PartUi(
            2,
            "Davlat uzluksiz ta’lim tizimi, uning har xil turlari va shakllari, davlat va nodavlat ta’lim tashkilotlari rivojlanishini ta’minlaydi.",
            listOf("uzluksiz ta’lim tizimi"),
        ),
        PartUi(3, "Davlat maktabgacha ta’lim va tarbiyani rivojlantirish uchun shart-sharoitlar yaratadi.", emptyList()),
        PartUi(4, "…", emptyList()),
    ),
    sourceLabel = null,
    summary = SummaryUi(
        bullets = listOf(
            "O‘qish — har bir insonning huquqi. Yoshingiz, millatingiz yoki oilangizning moddiy ahvoli bunga to‘siq bo‘lmasligi kerak.",
            "Davlat bog‘chadan oliy ta’limgacha bo‘lgan butun tizimni rivojlantirishi shart.",
            "Umumiy o‘rta ta’lim bepul — maktabda o‘qish uchun pul talab qilinmaydi.",
        ),
        reviewedBy = null,
        disclaimer = null,
    ),
    examples = listOf(
        ExampleUi(
            "Nodirning ota-onasi maktabga “homiylik puli” bermagani uchun uni darsdan chetlatishmoqchi.",
            "Umumiy o‘rta ta’lim bepul. To‘lov qilmagani uchun o‘quvchini darsdan chetlatish 50-moddaga zid.",
            isLawful = false,
        ),
    ),
    related = listOf(
        RelatedUi(19, "Qonun oldida tenglik"),
        RelatedUi(42, "Mehnat va kasb tanlash"),
        RelatedUi(55, "Huquqlarni himoya qilish"),
    ),
    terms = listOf(TermChipUi("inklyuziv-talim", "Inklyuziv ta’lim"), TermChipUi("davlat-granti", "Davlat granti")),
    discussion = DiscussionUi(24, "Kontrakt to‘lovi kechiksa, darsga kiritmaslik mumkinmi?"),
    quizId = "ijtimoiy-huquqlar",
    previousNumber = 49,
    nextNumber = 51,
    shareText = "",
)

@AsosPreviews
@Composable
private fun ArticleScreenPreview() {
    AsosTheme {
        ArticleScreen(
            uiState = ArticleUiState.Success(article = previewArticle, isSaved = true, textSize = ReaderTextSize.MEDIUM),
            onAction = {},
        )
    }
}

@AsosPreviews
@Composable
private fun ArticleScreenErrorPreview() {
    AsosTheme {
        ArticleScreen(
            uiState = ArticleUiState.Error("Internet aloqasi yo‘q. Ulanishni tekshirib, qayta urinib ko‘ring."),
            onAction = {},
        )
    }
}
