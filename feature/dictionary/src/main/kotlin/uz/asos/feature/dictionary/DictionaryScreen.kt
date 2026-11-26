/*
 * DictionaryScreen.kt
 * Module: :feature:dictionary   ·   Work package: WP-06
 * Screen: Konstitutsiyaviy lug‘at (design artboard: Lugat.dc.html)
 *
 * DictionaryRoute connects the ViewModel (plus text-to-speech and snackbars);
 * DictionaryScreen(uiState, onAction) is stateless and previewable.
 *
 * Layout (top to bottom, as on the canvas):
 *   DictionaryTopBar · DictionarySearchField · SavedTermsFilter        (fixed)
 *   TermOfTheDayCard · letter header + TermListItem × n                 (scrolls)
 *   AlphabetIndex on the right edge, jumping to a letter.
 * Tapping a term opens it in a bottom sheet (same body as the "Kun atamasi" card).
 */
package uz.asos.feature.dictionary

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.InlineLoading
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.dictionary.components.AlphabetIndex
import uz.asos.feature.dictionary.components.DictionarySearchField
import uz.asos.feature.dictionary.components.DictionaryTopBar
import uz.asos.feature.dictionary.components.LetterHeader
import uz.asos.feature.dictionary.components.SavedTermsFilter
import uz.asos.feature.dictionary.components.TermDetails
import uz.asos.feature.dictionary.components.TermListItem
import uz.asos.feature.dictionary.components.TermOfTheDayCard
import uz.asos.feature.dictionary.components.previewTerm
import java.util.Locale

@Composable
internal fun DictionaryRoute(
    onBack: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val speaker = rememberSpeaker()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                DictionaryEvent.SignInRequired -> {
                    val result = snackbarHostState.showSnackbar(
                        message = context.getString(R.string.dictionary_saved_signin_title),
                        actionLabel = context.getString(R.string.dictionary_signin_action),
                        duration = SnackbarDuration.Short,
                    )
                    if (result == SnackbarResult.ActionPerformed) onSignIn()
                }
                is DictionaryEvent.Message -> snackbarHostState.showSnackbar(event.text)
            }
        }
    }

    DictionaryScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                DictionaryAction.OnBack -> onBack()
                is DictionaryAction.OnArticleClick -> {
                    viewModel.onAction(DictionaryAction.OnTermSheetDismiss)
                    onOpenArticle(action.number)
                }
                is DictionaryAction.OnListenClick -> speaker.speak(action.text)
                DictionaryAction.OnSignInClick -> onSignIn()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun DictionaryScreen(
    uiState: DictionaryUiState,
    onAction: (DictionaryAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = Gutter, top = 8.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                DictionaryTopBar(onBack = { onAction(DictionaryAction.OnBack) })
                Column(
                    modifier = Modifier.padding(start = Gutter - 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    DictionarySearchField(
                        query = uiState.query,
                        onQueryChange = { onAction(DictionaryAction.OnQueryChange(it)) },
                    )
                    SavedTermsFilter(
                        selected = uiState.filter,
                        savedCount = uiState.savedCount,
                        onSelect = { onAction(DictionaryAction.OnFilterSelect(it)) },
                    )
                }
            }

            when {
                uiState.isLoading && uiState.list !is TermListState.Loaded ->
                    LoadingState(blocks = listOf(260.dp, 30.dp, 76.dp, 76.dp, 76.dp))

                uiState.errorMessage != null ->
                    ErrorState(
                        title = stringResource(R.string.dictionary_load_error_title),
                        message = uiState.errorMessage,
                        retryLabel = stringResource(R.string.dictionary_retry),
                        onRetry = { onAction(DictionaryAction.OnRetry) },
                    )

                else -> DictionaryContent(uiState = uiState, onAction = onAction)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { DictionarySnackbar(it) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(16.dp),
        )

        uiState.openedTerm?.let { TermSheet(state = it, onAction = onAction) }
    }
}

private val Gutter = 20.dp

/** One row of the scrolling list; [letter] drives the rail highlight. */
private sealed interface Entry {
    val key: String
    val letter: String?

    data class TermOfTheDay(val term: TermCardUi) : Entry {
        override val key = "term-of-the-day"
        override val letter: String? = null
    }
    data class Header(override val letter: String) : Entry {
        override val key = "letter-$letter"
    }
    data class Row(val term: TermRowUi, override val letter: String) : Entry {
        override val key = "term-${term.slug}"
    }
    data class Status(val state: TermListState) : Entry {
        override val key = "status"
        override val letter: String? = null
    }
}

private fun entries(uiState: DictionaryUiState): List<Entry> = buildList {
    if (uiState.showTermOfTheDay) uiState.termOfTheDay?.let { add(Entry.TermOfTheDay(it)) }
    val list = uiState.list
    if (list is TermListState.Loaded && !list.isEmpty) {
        list.groups.forEach { group ->
            add(Entry.Header(group.letter))
            group.items.forEach { add(Entry.Row(it, group.letter)) }
        }
    } else {
        add(Entry.Status(list))
    }
}

@Composable
private fun DictionaryContent(
    uiState: DictionaryUiState,
    onAction: (DictionaryAction) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val entries = remember(uiState) { entries(uiState) }
    // Letter picked on the rail. Kept while the list can't scroll that group to the top (end of
    // the list); cleared as soon as the user drags the list.
    var tappedLetter by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(listState) {
        listState.interactionSource.interactions.collect { if (it is DragInteraction.Start) tappedLetter = null }
    }
    val currentLetter by remember(entries) {
        derivedStateOf { tappedLetter ?: currentLetter(entries, listState) }
    }
    val showRail = uiState.list is TermListState.Loaded && uiState.letters.any { it.enabled }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Gutter,
                end = if (showRail) Gutter + 14.dp else Gutter,
                top = 4.dp,
                bottom = 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(entries, key = { it.key }) { entry ->
                when (entry) {
                    is Entry.TermOfTheDay -> TermOfTheDayCard(
                        term = entry.term,
                        onListen = { onAction(DictionaryAction.OnListenClick(entry.term.term)) },
                        onBookmark = { onAction(entry.term.bookmarkAction()) },
                        onArticleClick = { onAction(DictionaryAction.OnArticleClick(it)) },
                        modifier = Modifier.padding(bottom = 10.dp),
                    )
                    is Entry.Header -> LetterHeader(entry.letter)
                    is Entry.Row -> TermListItem(
                        term = entry.term,
                        onClick = { onAction(DictionaryAction.OnTermClick(entry.term.slug)) },
                        onBookmark = {
                            onAction(DictionaryAction.OnBookmarkClick(entry.term.id, entry.term.slug, entry.term.isSaved))
                        },
                    )
                    is Entry.Status -> ListStatus(uiState = uiState, onAction = onAction)
                }
            }
        }

        if (showRail) {
            AlphabetIndex(
                letters = uiState.letters,
                currentLetter = currentLetter,
                onLetterClick = { letter ->
                    val index = entries.indexOfFirst { it is Entry.Header && it.letter == letter }
                    if (index >= 0) {
                        tappedLetter = letter
                        scope.launch { listState.animateScrollToItem(index) }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp),
            )
        }
    }
}

/** Letter of the group at the top of the list; the first letter while the card is on top. */
private fun currentLetter(entries: List<Entry>, listState: LazyListState): String? {
    val first = listState.firstVisibleItemIndex
    return entries.getOrNull(first)?.letter
        ?: entries.drop(first).firstNotNullOfOrNull { it.letter }
}

@Composable
private fun ListStatus(
    uiState: DictionaryUiState,
    onAction: (DictionaryAction) -> Unit,
) {
    when (val list = uiState.list) {
        TermListState.Loading -> InlineLoading(Modifier.padding(vertical = 24.dp))
        is TermListState.Failed -> EmptyState(
            title = list.message,
            icon = AsosIcons.Close,
            actionLabel = stringResource(R.string.dictionary_retry),
            onAction = { onAction(DictionaryAction.OnListRetry) },
        )
        TermListState.NeedsSignIn -> EmptyState(
            title = stringResource(R.string.dictionary_saved_signin_title),
            message = stringResource(R.string.dictionary_saved_signin_message),
            icon = AsosIcons.Bookmark,
            actionLabel = stringResource(R.string.dictionary_signin_action),
            onAction = { onAction(DictionaryAction.OnSignInClick) },
        )
        is TermListState.Loaded -> if (uiState.filter == DictionaryFilter.SAVED && !uiState.isSearching) {
            EmptyState(
                title = stringResource(R.string.dictionary_saved_empty_title),
                message = stringResource(R.string.dictionary_saved_empty_message),
                icon = AsosIcons.Bookmark,
            )
        } else {
            EmptyState(
                title = stringResource(R.string.dictionary_search_empty_title),
                message = stringResource(R.string.dictionary_search_empty_message),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermSheet(
    state: OpenedTermState,
    onAction: (DictionaryAction) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onAction(DictionaryAction.OnTermSheetDismiss) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AsosColors.Surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, bottom = 32.dp),
        ) {
            when (state) {
                OpenedTermState.Loading -> InlineLoading(Modifier.padding(vertical = 32.dp))
                is OpenedTermState.Failed -> EmptyState(
                    title = state.message,
                    icon = AsosIcons.Close,
                    actionLabel = stringResource(R.string.dictionary_retry),
                    onAction = { onAction(DictionaryAction.OnTermSheetRetry) },
                )
                is OpenedTermState.Loaded -> TermDetails(
                    term = state.term,
                    overline = null,
                    onListen = { onAction(DictionaryAction.OnListenClick(state.term.term)) },
                    onBookmark = { onAction(state.term.bookmarkAction()) },
                    onArticleClick = { onAction(DictionaryAction.OnArticleClick(it)) },
                )
            }
        }
    }
}

/**
 * Snackbar with explicit text colours. The Material snackbar takes its text style from
 * bodyMedium, which in AsosTheme carries Navy, so its text would be navy on navy.
 */
@Composable
private fun DictionarySnackbar(data: SnackbarData) {
    Snackbar(
        containerColor = AsosColors.Navy,
        shape = AsosShapes.R14,
        action = data.visuals.actionLabel?.let { label ->
            {
                TextButton(onClick = { data.performAction() }) {
                    Text(text = label, style = AsosTextStyles.Chip, color = AsosColors.BrandOnNavy)
                }
            }
        },
    ) {
        Text(text = data.visuals.message, style = AsosTextStyles.BodySmall, color = AsosColors.Surface)
    }
}

private fun TermCardUi.bookmarkAction() = DictionaryAction.OnBookmarkClick(id, slug, isSaved)

// ---------------------------------------------------------------------- text-to-speech

/** Reads a term aloud. Uses Uzbek when the device has that voice, otherwise the default one. */
private class Speaker(context: android.content.Context) : TextToSpeech.OnInitListener {
    private val tts = TextToSpeech(context.applicationContext, this)
    private var ready = false

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val uzbek = Locale.forLanguageTag("uz")
        if (tts.isLanguageAvailable(uzbek) >= TextToSpeech.LANG_AVAILABLE) tts.language = uzbek
        ready = true
    }

    fun speak(text: String) {
        if (ready) tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "asos-term")
    }

    fun shutdown() = tts.shutdown()
}

@Composable
private fun rememberSpeaker(): Speaker {
    val context = LocalContext.current
    val speaker = remember(context) { Speaker(context) }
    DisposableEffect(speaker) { onDispose { speaker.shutdown() } }
    return speaker
}

// ---------------------------------------------------------------------- previews

private val previewState = DictionaryUiState(
    isLoading = false,
    isSignedIn = true,
    savedCount = 12,
    termOfTheDay = previewTerm,
    list = TermListState.Loaded(
        listOf(
            TermGroupUi(
                "D",
                listOf(
                    TermRowUi(1, "daxlsizlik", "Daxlsizlik", "Ruxsatsiz tegib, aralashib bo‘lmaslik.", "27 · 31-moddalar", true),
                    TermRowUi(2, "davlat-suvereniteti", "Davlat suvereniteti", "Davlatning o‘z hududida to‘liq mustaqilligi.", "1-modda", false),
                    TermRowUi(3, "davlat-tili", "Davlat tili", "Davlat ishlarida rasmiy qo‘llanadigan til.", "4-modda", false),
                    TermRowUi(4, "demokratiya", "Demokratiya", "Hokimiyat manbai xalq bo‘lgan boshqaruv tartibi.", null, true),
                ),
            ),
        ),
    ),
    letters = listOf(
        "A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "X", "Y", "Z", "O‘", "G‘", "Sh", "Ch", "Ng",
    ).map { LetterUi(it, enabled = it == "D") },
)

@AsosPreviews
@Composable
private fun DictionaryScreenPreview() {
    AsosTheme { DictionaryScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun DictionaryScreenSavedSignedOutPreview() {
    AsosTheme {
        DictionaryScreen(
            uiState = previewState.copy(
                isSignedIn = false,
                savedCount = null,
                filter = DictionaryFilter.SAVED,
                list = TermListState.NeedsSignIn,
            ),
            onAction = {},
        )
    }
}

@AsosPreviews
@Composable
private fun DictionaryScreenLoadingPreview() {
    AsosTheme { DictionaryScreen(uiState = DictionaryUiState(), onAction = {}) }
}
