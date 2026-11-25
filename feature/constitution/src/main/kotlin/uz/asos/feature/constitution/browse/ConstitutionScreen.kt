/*
 * ConstitutionScreen.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * ConstitutionRoute connects the ViewModel; ConstitutionScreen(uiState, onAction) is
 * stateless and previewable.
 *
 * Layout (top to bottom, as on the canvas):
 *   ConstitutionHeader · search · Mavzular | Tuzilish | Saqlangan
 *   Tuzilish:  ReadingProgressCard · topic chips · Muqaddima · SectionAccordion × 6
 *   Mavzular:  topic rows        Saqlangan: saved articles        Search: results
 * The bottom bar is drawn by the app scaffold.
 */
package uz.asos.feature.constitution.browse

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.component.AsosSearchField
import uz.asos.core.designsystem.component.AsosSegmentedControl
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.components.ArticleListSection
import uz.asos.feature.constitution.browse.components.ConstitutionHeader
import uz.asos.feature.constitution.browse.components.PreambleRow
import uz.asos.feature.constitution.browse.components.ReadingProgressCard
import uz.asos.feature.constitution.browse.components.SectionAccordion
import uz.asos.feature.constitution.browse.components.TopicChipsRow
import uz.asos.feature.constitution.browse.components.TopicListItem

@Composable
internal fun ConstitutionRoute(
    onOpenArticle: (key: String) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ConstitutionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Coming back from the reader: refresh progress (read status, "Oxirgi o‘qilgan").
    LifecycleResumeEffect(Unit) {
        viewModel.onAction(ConstitutionAction.OnResume)
        onPauseOrDispose { }
    }

    ConstitutionScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is ConstitutionAction.OnArticleClick -> onOpenArticle(action.key)
                ConstitutionAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun ConstitutionScreen(
    uiState: ConstitutionUiState,
    onAction: (ConstitutionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.sections.isEmpty() ->
                LoadingState(modifier = Modifier.statusBarsPadding())

            uiState.errorMessage != null && uiState.sections.isEmpty() ->
                ErrorState(
                    title = stringResource(R.string.constitution_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(ConstitutionAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> ConstitutionContent(uiState = uiState, onAction = onAction)
        }
    }
}

private val Gutter = 20.dp

@Composable
private fun ConstitutionContent(
    uiState: ConstitutionUiState,
    onAction: (ConstitutionAction) -> Unit,
) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val tabs = listOf(
        stringResource(R.string.constitution_tab_topics),
        stringResource(R.string.constitution_tab_structure),
        stringResource(R.string.constitution_tab_saved),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = topInset + 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "top") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Gutter)
                    .padding(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ConstitutionHeader(
                    subtitle = uiState.subtitle,
                    onSavedClick = { onAction(ConstitutionAction.OnSavedClick) },
                )
                AsosSearchField(
                    value = uiState.query,
                    onValueChange = { onAction(ConstitutionAction.OnQueryChange(it)) },
                    placeholder = stringResource(R.string.constitution_search_placeholder),
                    accessibilityLabel = stringResource(R.string.constitution_search_label),
                )
                AsosSegmentedControl(
                    options = tabs,
                    selectedIndex = uiState.selectedTab.ordinal,
                    onSelect = { onAction(ConstitutionAction.OnTabSelect(ConstitutionTab.entries[it])) },
                )
            }
        }

        when {
            uiState.isSearching -> articleList(
                state = uiState.articleList,
                emptyTitle = R.string.constitution_search_empty_title,
                emptyMessage = R.string.constitution_search_empty_message,
                onAction = onAction,
            )
            uiState.selectedTab == ConstitutionTab.TOPICS -> topicsTab(uiState, onAction)
            uiState.selectedTab == ConstitutionTab.SAVED -> articleList(
                state = uiState.articleList,
                emptyTitle = R.string.constitution_saved_empty_title,
                emptyMessage = R.string.constitution_saved_empty_message,
                onAction = onAction,
            )
            else -> structureTab(uiState, onAction)
        }
    }
}

private fun LazyListScope.structureTab(
    uiState: ConstitutionUiState,
    onAction: (ConstitutionAction) -> Unit,
) {
    uiState.continueReading?.let { continueReading ->
        item(key = "continue") {
            ReadingProgressCard(
                continueReading = continueReading,
                onClick = { onAction(ConstitutionAction.OnArticleClick(continueReading.articleKey)) },
                modifier = Modifier
                    .padding(horizontal = Gutter)
                    .padding(bottom = 8.dp),
            )
        }
    }

    item(key = "chips") {
        TopicChipsRow(
            topics = uiState.topics,
            selectedSlug = uiState.selectedTopicSlug,
            onSelect = { onAction(ConstitutionAction.OnTopicSelect(it)) },
            contentPadding = PaddingValues(horizontal = Gutter),
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }

    if (uiState.selectedTopicSlug != null) {
        articleList(
            state = uiState.articleList,
            emptyTitle = R.string.constitution_topic_empty_title,
            emptyMessage = null,
            onAction = onAction,
        )
        return
    }

    item(key = "preamble") {
        PreambleRow(
            onClick = { onAction(ConstitutionAction.OnArticleClick(PREAMBLE_KEY)) },
            modifier = Modifier.padding(horizontal = Gutter),
        )
    }

    items(uiState.sections, key = { "section-${it.roman}" }) { section ->
        SectionAccordion(
            section = section,
            expanded = section.roman in uiState.expandedSections,
            expandedChapters = uiState.expandedChapters,
            groupArticles = { key -> uiState.chapterArticles[key] },
            directArticlesKey = ConstitutionViewModel.sectionKey(section.roman),
            onToggle = { onAction(ConstitutionAction.OnSectionToggle(section.roman)) },
            onChapterToggle = { onAction(ConstitutionAction.OnChapterToggle(it)) },
            onGroupRetry = { onAction(ConstitutionAction.OnChapterRetry(it)) },
            onArticleClick = { onAction(ConstitutionAction.OnArticleClick(it)) },
            modifier = Modifier.padding(horizontal = Gutter),
        )
    }
}

private fun LazyListScope.topicsTab(
    uiState: ConstitutionUiState,
    onAction: (ConstitutionAction) -> Unit,
) {
    if (uiState.topics.isEmpty()) {
        item(key = "topics-empty") {
            EmptyState(title = stringResource(R.string.constitution_topics_empty_title))
        }
        return
    }
    items(uiState.topics, key = { "topic-${it.slug}" }) { topic ->
        TopicListItem(
            topic = topic,
            onClick = { onAction(ConstitutionAction.OnTopicOpen(topic.slug)) },
            modifier = Modifier.padding(horizontal = Gutter),
        )
    }
}

private fun LazyListScope.articleList(
    state: ArticleListState,
    emptyTitle: Int,
    emptyMessage: Int?,
    onAction: (ConstitutionAction) -> Unit,
) {
    item(key = "article-list") {
        ArticleListSection(
            state = state,
            emptyTitle = stringResource(emptyTitle),
            emptyMessage = emptyMessage?.let { stringResource(it) },
            onArticleClick = { onAction(ConstitutionAction.OnArticleClick(it)) },
            onRetry = { onAction(ConstitutionAction.OnListRetry) },
            onSignIn = { onAction(ConstitutionAction.OnSignInClick) },
            modifier = Modifier.padding(horizontal = Gutter),
        )
    }
}

// ---------------------------------------------------------------------- previews

private val previewState = ConstitutionUiState(
    isLoading = false,
    subtitle = "2023-yil tahriri · 6 bo‘lim · 27 bob · 155 modda",
    isSignedIn = true,
    continueReading = ContinueReadingUi("50", "50", "Ta’lim olish huquqi", "IX bob · 3 daqiqa qoldi"),
    topics = listOf(
        TopicUi("huquqlar", "Huquqlar", "24 modda", "brand"),
        TopicUi("erkinliklar", "Erkinliklar", "11 modda", "purple"),
        TopicUi("burchlar", "Burchlar", "6 modda", "orange"),
        TopicUi("talim", "Ta’lim", "3 modda", "teal"),
    ),
    sections = listOf(
        SectionUi("I", "I BO‘LIM", "Asosiy prinsiplar", "4 bob · 1–18-moddalar", emptyList(), null),
        SectionUi(
            "II", "II BO‘LIM", "Inson va fuqaroning asosiy huquqlari, erkinliklari va burchlari", "7 bob · 19–64-moddalar",
            listOf(
                ChapterUi("V", "Umumiy qoidalar", "19–21-moddalar", ReadState.READ, 100),
                ChapterUi("VI", "Fuqarolik", "22–24-moddalar", ReadState.READ, 100),
                ChapterUi("VII", "Shaxsiy huquq va erkinliklar", "25–35-moddalar", ReadState.READ, 100),
                ChapterUi("VIII", "Siyosiy huquqlar", "36–40-moddalar", ReadState.NOT_STARTED, 0),
                ChapterUi("IX", "Iqtisodiy, ijtimoiy, madaniy va ekologik huquqlar", "41–53-moddalar · 8/13 o‘qildi", ReadState.IN_PROGRESS, 62),
                ChapterUi("X", "Inson hamda fuqaroning huquq va erkinliklari kafolatlari", "54–58-moddalar", ReadState.NOT_STARTED, 0),
                ChapterUi("XI", "Fuqarolarning burchlari", "59–64-moddalar", ReadState.NOT_STARTED, 0),
            ),
            null,
        ),
        SectionUi("III", "III BO‘LIM", "Jamiyat va shaxs", "4 bob · 65–82-moddalar", emptyList(), null),
    ),
    expandedSections = setOf("II"),
)

@AsosPreviews
@Composable
private fun ConstitutionScreenPreview() {
    AsosTheme { ConstitutionScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun ConstitutionScreenSavedSignedOutPreview() {
    AsosTheme {
        ConstitutionScreen(
            uiState = previewState.copy(
                isSignedIn = false,
                continueReading = null,
                selectedTab = ConstitutionTab.SAVED,
                articleList = ArticleListState.NeedsSignIn,
            ),
            onAction = {},
        )
    }
}
