package uz.asos.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.navigation.ArticleRoute
import uz.asos.core.navigation.AsosRoute
import uz.asos.core.navigation.CaseRoute
import uz.asos.core.navigation.CommunityRoute
import uz.asos.core.navigation.ConstitutionRoute
import uz.asos.core.navigation.DictionaryRoute
import uz.asos.core.navigation.LearnRoute
import uz.asos.core.navigation.LegalHelpRoute
import uz.asos.core.navigation.LoginRoute
import uz.asos.core.navigation.PostRoute
import uz.asos.core.navigation.ProfileRoute
import uz.asos.core.navigation.SearchRoute
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.home.components.ArticleOfTheDayCard
import uz.asos.feature.home.components.CommunityHighlightsSection
import uz.asos.feature.home.components.ContinueLearningSection
import uz.asos.feature.home.components.DailyQuizCard
import uz.asos.feature.home.components.HelpShortcutsSection
import uz.asos.feature.home.components.HomeSearchBar
import uz.asos.feature.home.components.HomeTopBar
import uz.asos.feature.home.components.TopicsSection

private val Gutter = AsosSpacing.screenGutter
private val HeadlineStyle = AsosTextStyles.ScreenTitle.copy(fontSize = 31.sp, lineHeight = 35.sp, letterSpacing = (-0.025).em)

@Composable
internal fun HomeRoute(
    onNavigate: (AsosRoute) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val currentOnNavigate by rememberUpdatedState(onNavigate)

    LifecycleResumeEffect(Unit) {
        viewModel.onAction(HomeAction.OnResume)
        onPauseOrDispose { }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                HomeEvent.SignInRequired -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.home_signin_required),
                        actionLabel = context.getString(R.string.home_signin_action),
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) currentOnNavigate(LoginRoute)
                }
                HomeEvent.ArticleSaved -> snackbarHostState.showSnackbar(context.getString(R.string.home_article_saved))
                HomeEvent.NotificationsComingSoon ->
                    snackbarHostState.showSnackbar(context.getString(R.string.home_notifications_soon))
                is HomeEvent.PointsEarned ->
                    snackbarHostState.showSnackbar(context.getString(R.string.home_points_earned, event.points))
                is HomeEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (val route = action.toRoute()) {
                null -> viewModel.onAction(action)
                else -> onNavigate(route)
            }
        },
        modifier = modifier,
    )
}

private fun HomeAction.toRoute(): AsosRoute? = when (this) {
    HomeAction.OnProfileClick -> ProfileRoute
    HomeAction.OnSearchClick -> SearchRoute
    is HomeAction.OnArticleClick -> ArticleRoute(key)
    HomeAction.OnAllTopicsClick, is HomeAction.OnTopicClick -> ConstitutionRoute
    HomeAction.OnContinueAllClick -> LearnRoute
    is HomeAction.OnCaseClick -> CaseRoute(caseId)
    HomeAction.OnLegalHelpClick, HomeAction.OnAssistantClick -> LegalHelpRoute
    HomeAction.OnDictionaryClick -> DictionaryRoute()
    HomeAction.OnAllPostsClick -> CommunityRoute()
    is HomeAction.OnPostClick -> PostRoute(postId)
    else -> null
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.articleOfDay == null && uiState.topics.isEmpty() ->
                LoadingState(modifier = Modifier.statusBarsPadding())

            uiState.errorMessage != null ->
                ErrorState(
                    title = stringResource(R.string.home_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(HomeAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> HomeContent(uiState = uiState, onAction = onAction)
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val gutter = Modifier.padding(horizontal = Gutter)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = topInset + 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        item(key = "header") {
            Column(modifier = gutter, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                HomeTopBar(
                    greeting = uiState.greeting,
                    userName = uiState.userName,
                    initials = uiState.initials,
                    hasUnreadNotifications = uiState.hasUnreadNotifications,
                    onProfileClick = { onAction(HomeAction.OnProfileClick) },
                    onNotificationsClick = { onAction(HomeAction.OnNotificationsClick) },
                )
                Headline()
                HomeSearchBar(
                    onSearchClick = { onAction(HomeAction.OnSearchClick) },
                    onVoiceClick = { onAction(HomeAction.OnSearchClick) },
                )
            }
        }

        uiState.articleOfDay?.let { article ->
            item(key = "article-of-day") {
                ArticleOfTheDayCard(
                    article = article,
                    isSaved = uiState.isArticleSaved,
                    onReadClick = { onAction(HomeAction.OnArticleClick(article.articleKey)) },
                    onSaveClick = { onAction(HomeAction.OnSaveArticleClick) },
                    modifier = gutter,
                )
            }
        }

        if (uiState.topics.isNotEmpty()) {
            item(key = "topics") {
                TopicsSection(
                    topics = uiState.topics,
                    onTopicClick = { onAction(HomeAction.OnTopicClick(it)) },
                    onSeeAllClick = { onAction(HomeAction.OnAllTopicsClick) },
                    modifier = gutter,
                )
            }
        }

        if (uiState.hasContinueSection) {
            item(key = "continue") {
                ContinueLearningSection(
                    continueReading = uiState.continueReading,
                    caseOfWeek = uiState.caseOfWeek,
                    onArticleClick = { onAction(HomeAction.OnArticleClick(it)) },
                    onCaseClick = { onAction(HomeAction.OnCaseClick(it)) },
                    onSeeAllClick = { onAction(HomeAction.OnContinueAllClick) },
                    contentPadding = PaddingValues(horizontal = Gutter),
                )
            }
        }

        uiState.dailyQuiz?.let { quiz ->
            item(key = "daily-quiz") {
                DailyQuizCard(
                    quiz = quiz,
                    onOptionClick = { onAction(HomeAction.OnQuizOptionClick(it)) },
                    modifier = gutter,
                )
            }
        }

        item(key = "help") {
            HelpShortcutsSection(
                help = uiState.help,
                onLegalHelpClick = { onAction(HomeAction.OnLegalHelpClick) },
                onDictionaryClick = { onAction(HomeAction.OnDictionaryClick) },
                onAssistantClick = { onAction(HomeAction.OnAssistantClick) },
                modifier = gutter,
            )
        }

        if (uiState.posts.isNotEmpty()) {
            item(key = "community") {
                CommunityHighlightsSection(
                    posts = uiState.posts,
                    onPostClick = { onAction(HomeAction.OnPostClick(it)) },
                    onSeeAllClick = { onAction(HomeAction.OnAllPostsClick) },
                    modifier = gutter,
                )
            }
        }
    }
}

@Composable
private fun Headline() {
    val start = stringResource(R.string.home_headline_start)
    val accent = stringResource(R.string.home_headline_accent)
    Text(
        text = buildAnnotatedString {
            append(start)
            withStyle(SpanStyle(color = AsosColors.Brand500)) { append(accent) }
        },
        style = HeadlineStyle,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { heading() },
    )
}

private val previewState = HomeUiState(
    isLoading = false,
    isSignedIn = true,
    greeting = "Xayrli kech",
    userName = "Aziza",
    initials = "AK",
    hasUnreadNotifications = true,
    articleOfDay = ArticleOfDayUi(
        articleKey = "33",
        watermark = "33",
        meta = "VII bob · Shaxsiy huquqlar",
        quote = "“Har kim fikrlash, so‘z va e’tiqod erkinligi huquqiga ega.”",
        reference = "33-modda, 1-qism",
    ),
    topics = listOf(
        TopicTileUi("shaxsiy-huquqlar", "Shaxsiy huquqlar", "11 modda", "shield", "brand"),
        TopicTileUi("siyosiy-huquqlar", "Siyosiy huquqlar", "5 modda", "vote", "purple"),
        TopicTileUi("ijtimoiy-huquqlar", "Ijtimoiy huquqlar", "13 modda", "heart", "teal"),
        TopicTileUi("burchlar", "Burchlar", "6 modda", "clipboard", "orange"),
        TopicTileUi("oila-va-yoshlar", "Oila va yoshlar", "5 modda", "family", "pink"),
        TopicTileUi("sud-hokimiyati", "Sud hokimiyati", "11 modda", "scale", "navy"),
    ),
    continueReading = ContinueReadingUi(
        articleKey = "50",
        title = "Ijtimoiy huquqlar",
        meta = "IX bob",
        progressLabel = "8 / 13 modda o‘qildi",
        percentLabel = "62%",
        progress = 0.62f,
        nextLabel = "Keyingi: 50-modda · Ta’lim →",
    ),
    caseOfWeek = CaseOfWeekUi("14", "Direktor va telefon: shaxsiy hayot daxlsizligi", "2 / 3 bosqich", 0.66f),
    dailyQuiz = DailyQuizUi(
        meta = "Shaxsiy huquqlar · O‘rta",
        streakLabel = "7 kun ketma-ket",
        question = "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?",
        options = listOf(
            QuizOptionUi(1, "24 soat", QuizOptionState.DEFAULT),
            QuizOptionUi(2, "48 soat", QuizOptionState.DEFAULT),
            QuizOptionUi(3, "72 soat", QuizOptionState.DEFAULT),
            QuizOptionUi(4, "12 soat", QuizOptionState.DEFAULT),
        ),
        footer = "To‘g‘ri javob uchun +20 ball",
        explanation = null,
        isAnswered = false,
        isSubmitting = false,
    ),
    help = HelpUi(lawyersOnlineLabel = "3 advokat onlayn", termOfDayLabel = "Kun atamasi: Daxlsizlik"),
    posts = listOf(
        PostUi(1, "Uy-joy", "pink", "Anonim · 2 soat oldin", "Uy egasi ruxsatsiz kvartiraga kirib turibdi. Bu qonuniymi?", "128", "34", PostBadge.LAWYER_ANSWERED),
        PostUi(2, "Ta’lim", "teal", "Jasur T. · 5 soat oldin", "Kontrakt to‘lovi kechiksa, talabani darsga kiritmaslik mumkinmi?", "76", "19", PostBadge.IN_DISCUSSION),
    ),
)

@AsosPreviews
@Composable
private fun HomeScreenPreview() {
    AsosTheme { HomeScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun HomeScreenGuestPreview() {
    AsosTheme {
        HomeScreen(
            uiState = previewState.copy(isSignedIn = false, userName = null, initials = null, continueReading = null),
            onAction = {},
        )
    }
}

@AsosPreviews
@Composable
private fun HomeScreenErrorPreview() {
    AsosTheme {
        HomeScreen(
            uiState = HomeUiState(isLoading = false, errorMessage = "Internet aloqasi yo‘q. Ulanishni tekshirib, qayta urinib ko‘ring."),
            onAction = {},
        )
    }
}
