package uz.asos.feature.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.core.model.dictionary.AlphabetLetter
import uz.asos.core.model.dictionary.TermArticle
import uz.asos.core.model.dictionary.TermDetail
import uz.asos.core.model.dictionary.TermSummary
import uz.asos.feature.dictionary.components.AlphabetIndex
import uz.asos.feature.dictionary.components.DictionarySearchField
import uz.asos.feature.dictionary.components.SavedTermsFilter
import uz.asos.feature.dictionary.components.TermDetailSheet
import uz.asos.feature.dictionary.components.TermListItem
import uz.asos.feature.dictionary.components.TermOfTheDayCard

@Composable
internal fun DictionaryRoute(
    onBack: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DictionaryScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                DictionaryAction.OnBackClick -> onBack()
                is DictionaryAction.OnArticleClick -> onOpenArticle(action.number)
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
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.isEmpty && uiState.query.isBlank() ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(32.dp, 52.dp, 40.dp, 260.dp, 80.dp, 80.dp),
                )

            uiState.errorMessage != null && uiState.isEmpty ->
                ErrorState(
                    title = stringResource(R.string.dictionary_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(DictionaryAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> DictionaryContent(uiState = uiState, onAction = onAction)
        }
    }

    uiState.openTerm?.let { term ->
        TermDetailSheet(
            term = term,
            onSaveToggle = { onAction(DictionaryAction.OnSaveToggle(term.id, term.isSaved)) },
            onArticleClick = { number -> onAction(DictionaryAction.OnArticleClick(number)) },
            onDismiss = { onAction(DictionaryAction.OnTermDismiss) },
        )
    }
}

@Composable
private fun DictionaryContent(
    uiState: DictionaryUiState,
    onAction: (DictionaryAction) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(
                start = AsosSpacing.xl,
                end = AsosSpacing.xl + ALPHABET_RAIL_WIDTH,
                top = AsosSpacing.m,
                bottom = AsosSpacing.xxxl,
            ),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.ll),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onAction(DictionaryAction.OnBackClick) }) {
                        Icon(
                            imageVector = AsosIcons.ChevronLeft,
                            contentDescription = stringResource(R.string.dictionary_back),
                            tint = AsosColors.Navy,
                        )
                    }
                    Text(
                        text = stringResource(R.string.dictionary_title),
                        style = AsosTextStyles.ScreenTitle.copy(fontSize = 21.sp, lineHeight = 27.sp),
                        modifier = Modifier.semantics { heading() },
                    )
                }
            }

            item {
                DictionarySearchField(
                    query = uiState.query,
                    onQueryChange = { onAction(DictionaryAction.OnQueryChange(it)) },
                    onClear = { onAction(DictionaryAction.OnQueryClear) },
                )
            }

            item {
                SavedTermsFilter(
                    savedOnly = uiState.savedOnly,
                    savedCount = uiState.savedCount,
                    onSavedOnlyChange = { onAction(DictionaryAction.OnSavedOnlyChange(it)) },
                )
            }

            if (uiState.showTermOfTheDay) {
                uiState.termOfTheDay?.let { term ->
                    item(key = "term-of-the-day-${term.id}") {
                        TermOfTheDayCard(
                            term = term,
                            onSaveToggle = {
                                onAction(DictionaryAction.OnSaveToggle(term.id, term.isSaved))
                            },
                            onArticleClick = { onAction(DictionaryAction.OnArticleClick(it)) },
                        )
                    }
                }
            }

            if (uiState.isEmpty) {
                item {
                    EmptyState(
                        title = stringResource(
                            if (uiState.savedOnly) {
                                R.string.dictionary_empty_saved
                            } else {
                                R.string.dictionary_empty_search
                            },
                        ),
                        icon = AsosIcons.Book,
                    )
                }
            }

            uiState.groups.forEach { group ->
                item(key = "letter-${group.letter}") {
                    Text(
                        text = group.letter,
                        style = AsosTextStyles.SerifNumber.copy(fontSize = 22.sp, lineHeight = 26.sp),
                        color = AsosColors.Brand600,
                        modifier = Modifier.padding(start = AsosSpacing.xxs, top = AsosSpacing.xs),
                    )
                }
                items(group.terms, key = { "term-${it.id}" }) { term ->
                    TermListItem(
                        term = term,
                        onClick = { onAction(DictionaryAction.OnTermClick(term.slug)) },
                        onSaveToggle = {
                            onAction(DictionaryAction.OnSaveToggle(term.id, term.isSaved))
                        },
                    )
                }
            }
        }

        if (uiState.letters.isNotEmpty()) {
            AlphabetIndex(
                letters = uiState.letters,
                selectedLetter = uiState.selectedLetter,
                onLetterSelect = { onAction(DictionaryAction.OnLetterSelect(it)) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = AsosSpacing.xxs),
            )
        }
    }
}

private val ALPHABET_RAIL_WIDTH = 26.dp

private val previewState = DictionaryUiState(
    isLoading = false,
    savedCount = 12,
    termOfTheDay = TermDetail(
        id = 1,
        term = "Daxlsizlik",
        slug = "daxlsizlik",
        letter = "D",
        partOfSpeech = "ot",
        etymology = "arabcha “daxl” — tegish, aralashish",
        definition = "Shaxs, uy-joy yoki yozishmalarga ruxsatsiz tegib, aralashib " +
            "bo‘lmasligi — ularning qonun bilan himoyalanganligi.",
        inLife = "Qonunda belgilangan holatlardan tashqari, telefoningizdagi xabarlarni " +
            "sizning roziligingizsiz hech kim o‘qiy olmaydi.",
        shortDef = "Ruxsatsiz tegib, aralashib bo‘lmaslik.",
        articles = listOf(
            TermArticle(27, "Erkinlik va shaxsiy daxlsizlik", ""),
            TermArticle(31, "Shaxsiy hayot va uy-joy daxlsizligi", ""),
        ),
        isSaved = true,
    ),
    letters = listOf("A", "B", "D", "E", "F", "G", "H", "I", "J", "K")
        .mapIndexed { index, letter -> AlphabetLetter(letter, if (index % 3 == 0) 0 else 2) },
    groups = listOf(
        TermGroupUi(
            letter = "D",
            terms = listOf(
                TermSummary(1, "Daxlsizlik", "daxlsizlik", "D", "Ruxsatsiz tegib, aralashib bo‘lmaslik.", listOf(27, 31), true),
                TermSummary(2, "Davlat tili", "davlat-tili", "D", "Davlat ishlarida rasmiy qo‘llanadigan til.", listOf(4), false),
            ),
        ),
        TermGroupUi(
            letter = "E",
            terms = listOf(
                TermSummary(5, "E’tiqod erkinligi", "etiqod-erkinligi", "E", "Har kimning o‘z qarashlariga ega bo‘lish huquqi.", listOf(33), false),
            ),
        ),
    ),
)

@AsosPreviews
@Composable
private fun DictionaryScreenPreview() {
    AsosTheme { DictionaryScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun DictionaryScreenSavedPreview() {
    AsosTheme {
        DictionaryScreen(
            uiState = previewState.copy(savedOnly = true, groups = emptyList()),
            onAction = {},
        )
    }
}
