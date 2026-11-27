package uz.asos.feature.cases.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.cases.R
import uz.asos.feature.cases.data.model.CaseDifficulty
import uz.asos.feature.cases.data.model.CaseStatus
import uz.asos.feature.cases.data.model.CaseTopic
import uz.asos.feature.cases.list.components.CaseCategoryChips
import uz.asos.feature.cases.list.components.CaseListItem
import uz.asos.feature.cases.list.components.CasesFilterSheet
import uz.asos.feature.cases.list.components.CasesHeader
import uz.asos.feature.cases.list.components.DifficultyFilter
import uz.asos.feature.cases.list.components.FeaturedCaseCard

@Composable
internal fun CasesRoute(
    onOpenCase: (id: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CasesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CasesScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                is CasesAction.OnCaseClick -> onOpenCase(action.id)
                CasesAction.OnBackClick -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun CasesScreen(
    uiState: CasesUiState,
    onAction: (CasesAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.cases.isEmpty() && uiState.featured == null ->
                LoadingState(
                    modifier = Modifier.statusBarsPadding(),
                    blocks = listOf(36.dp, 40.dp, 28.dp, 190.dp, 120.dp, 120.dp),
                )

            uiState.errorMessage != null && uiState.cases.isEmpty() && uiState.featured == null ->
                ErrorState(
                    title = stringResource(R.string.cases_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(CasesAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> CasesContent(uiState = uiState, onAction = onAction)
        }
    }

    if (uiState.isFilterSheetOpen) {
        CasesFilterSheet(
            statusFilter = uiState.statusFilter,
            onlyBookmarked = uiState.onlyBookmarked,
            onStatusSelect = { onAction(CasesAction.OnStatusFilterSelect(it)) },
            onOnlyBookmarkedChange = { onAction(CasesAction.OnOnlyBookmarkedChange(it)) },
            onDismiss = { onAction(CasesAction.OnFilterSheetDismiss) },
        )
    }
}

@Composable
private fun CasesContent(
    uiState: CasesUiState,
    onAction: (CasesAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(
            start = AsosSpacing.l,
            end = AsosSpacing.l,
            top = AsosSpacing.m,
            bottom = AsosSpacing.xxl,
        ),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        item {
            CasesHeader(
                hasFilter = uiState.hasSheetFilter,
                onBackClick = { onAction(CasesAction.OnBackClick) },
                onSortClick = { onAction(CasesAction.OnFilterSheetOpen) },
            )
        }

        item {
            CaseCategoryChips(
                selected = uiState.selectedTopic,
                onSelect = { onAction(CasesAction.OnTopicSelect(it)) },
            )
        }

        item {
            DifficultyFilter(
                selected = uiState.selectedDifficulty,
                onSelect = { onAction(CasesAction.OnDifficultySelect(it)) },
            )
        }

        uiState.featured?.let { featured ->
            item(key = "featured-${featured.id}") {
                FeaturedCaseCard(
                    featured = featured,
                    onClick = { onAction(CasesAction.OnCaseClick(featured.id)) },
                )
            }
        }

        if (uiState.isEmpty) {
            item {
                EmptyState(
                    title = stringResource(R.string.cases_empty_title),
                    icon = AsosIcons.Scales,
                    actionLabel = stringResource(R.string.cases_empty_action),
                    onAction = { onAction(CasesAction.OnClearFilters) },
                )
            }
        } else {
            items(uiState.cases, key = { it.id }) { case ->
                CaseListItem(
                    case = case,
                    onClick = { onAction(CasesAction.OnCaseClick(case.id)) },
                )
            }
        }
    }
}

private val previewState = CasesUiState(
    isLoading = false,
    featured = FeaturedCaseUi(
        id = 14,
        number = 14,
        numberLabel = "№ 14",
        title = "Direktor va telefon",
        shortDescription = "Maktab direktori darsda olib qo‘yilgan telefondagi o‘quvchi " +
            "yozishmalarini o‘qib chiqdi. U haqmi?",
        topic = CaseTopic.SHAXSIY_HAYOT,
        difficulty = CaseDifficulty.ORTA,
        durationLabel = "≈ 6 daqiqa",
        progressFraction = 0.66f,
        isDone = false,
    ),
    cases = listOf(
        CaseRowUi(
            id = 13,
            numberLabel = "№ 13",
            title = "Inklyuziv sinf",
            shortDescription = "Maktab eshitishida muammosi bor qizni qabul qilishdan bosh tortdi.",
            topic = CaseTopic.TALIM,
            difficulty = CaseDifficulty.OSON,
            status = CaseStatus.YECHILGAN,
        ),
        CaseRowUi(
            id = 12,
            numberLabel = "№ 12",
            title = "Qirq sakkiz soat",
            shortDescription = "Akasi ikki kundan beri sud qarorisiz ushlab turilibdi. " +
                "Oila nima qila oladi?",
            topic = CaseTopic.SUD,
            difficulty = CaseDifficulty.MURAKKAB,
            status = CaseStatus.YANGI,
        ),
    ),
)

@AsosPreviews
@Composable
private fun CasesScreenPreview() {
    AsosTheme { CasesScreen(uiState = previewState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun CasesScreenEmptyPreview() {
    AsosTheme {
        CasesScreen(
            uiState = previewState.copy(
                featured = null,
                cases = emptyList(),
                selectedTopic = CaseTopic.MEHNAT,
                onlyBookmarked = true,
            ),
            onAction = {},
        )
    }
}
