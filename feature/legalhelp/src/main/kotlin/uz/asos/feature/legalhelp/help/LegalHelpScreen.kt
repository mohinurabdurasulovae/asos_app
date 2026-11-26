/*
 * LegalHelpScreen.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Huquqiy yordam (design artboard: Yordam.dc.html)
 *
 * LegalHelpRoute connects the ViewModel and handles navigation, the dialer and snackbars;
 * LegalHelpScreen(uiState, onAction) is stateless and previewable.
 *
 * Layout (top to bottom, as on the canvas):
 *   back + title · article 29 quote · AiAssistantCard · HotlineCard ·
 *   "Advokatlar" header · filter chips · LawyerList
 */
package uz.asos.feature.legalhelp.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.help.components.AiAssistantCard
import uz.asos.feature.legalhelp.help.components.HotlineCard
import uz.asos.feature.legalhelp.help.components.LawyerFilterChips
import uz.asos.feature.legalhelp.help.components.LawyerList
import uz.asos.feature.legalhelp.help.components.LawyersHeader
import uz.asos.feature.legalhelp.help.components.PreviewLawyers

/** The article quoted at the top: "Har kimga malakali yuridik yordam olish huquqi kafolatlanadi." */
private const val QUOTE_ARTICLE = 29

@Composable
internal fun LegalHelpRoute(
    onBack: () -> Unit,
    onOpenChat: (chatId: Int) -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LegalHelpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val showSnackbar: (Int) -> Unit = { res ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(context.getString(res))
        }
    }

    LegalHelpScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                LegalHelpAction.OnBackClick -> onBack()
                is LegalHelpAction.OnArticleClick -> onOpenArticle(action.articleNumber)
                // TODO(WP-02): POST /chats returns the chatId. Until then chatId = lawyerId.
                is LegalHelpAction.OnWriteToLawyer -> onOpenChat(action.lawyerId)
                is LegalHelpAction.OnHotlineCall -> {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${action.dialNumber}"))
                    try {
                        context.startActivity(intent)
                    } catch (@Suppress("UNUSED_PARAMETER") e: ActivityNotFoundException) {
                        showSnackbar(R.string.legalhelp_coming_soon)
                    }
                }
                LegalHelpAction.OnCallbackRequest -> showSnackbar(R.string.legalhelp_hotline_callback_done)
                is LegalHelpAction.OnBookLawyer -> showSnackbar(R.string.legalhelp_lawyer_book_done)
                LegalHelpAction.OnVoiceInputClick,
                is LegalHelpAction.OnCallLawyer,
                is LegalHelpAction.OnVideoCallLawyer,
                -> showSnackbar(R.string.legalhelp_coming_soon)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun LegalHelpScreen(
    uiState: LegalHelpUiState,
    onAction: (LegalHelpAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        when {
            uiState.isLoading && uiState.lawyers.isEmpty() ->
                LoadingState(modifier = Modifier.statusBarsPadding())

            uiState.errorMessage != null && uiState.lawyers.isEmpty() ->
                ErrorState(
                    title = stringResource(R.string.legalhelp_load_error_title),
                    message = uiState.errorMessage,
                    onRetry = { onAction(LegalHelpAction.OnRetry) },
                    modifier = Modifier.statusBarsPadding(),
                )

            else -> LegalHelpContent(uiState = uiState, onAction = onAction)
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Composable
private fun LegalHelpContent(
    uiState: LegalHelpUiState,
    onAction: (LegalHelpAction) -> Unit,
) {
    val gutter = AsosSpacing.screenGutter
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = topInset + AsosSpacing.s, bottom = AsosSpacing.xxl),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
    ) {
        item(key = "header") {
            LegalHelpHeader(
                onBack = { onAction(LegalHelpAction.OnBackClick) },
                modifier = Modifier.padding(horizontal = gutter - AsosSpacing.s),
            )
        }
        item(key = "quote") {
            QuoteCard(modifier = Modifier.padding(horizontal = gutter))
        }
        item(key = "assistant") {
            AiAssistantCard(
                state = uiState.assistant,
                onInputChange = { onAction(LegalHelpAction.OnAssistantInputChange(it)) },
                onSuggestionClick = { onAction(LegalHelpAction.OnSuggestionClick(it)) },
                onSend = { onAction(LegalHelpAction.OnAssistantSend) },
                onVoiceClick = { onAction(LegalHelpAction.OnVoiceInputClick) },
                onArticleClick = { onAction(LegalHelpAction.OnArticleClick(it)) },
                modifier = Modifier.padding(horizontal = gutter),
            )
        }
        uiState.hotline?.let { hotline ->
            item(key = "hotline") {
                HotlineCard(
                    hotline = hotline,
                    onCall = { onAction(LegalHelpAction.OnHotlineCall(hotline.dialNumber)) },
                    onCallback = { onAction(LegalHelpAction.OnCallbackRequest) },
                    modifier = Modifier.padding(horizontal = gutter),
                )
            }
        }
        item(key = "lawyers-header") {
            LawyersHeader(
                onlineCount = uiState.onlineCount,
                modifier = Modifier.padding(start = gutter, end = gutter, top = AsosSpacing.s),
            )
        }
        item(key = "filters") {
            LawyerFilterChips(
                selected = uiState.selectedCategory,
                onSelect = { onAction(LegalHelpAction.OnCategorySelect(it)) },
                contentPadding = PaddingValues(horizontal = gutter),
            )
        }
        item(key = "lawyers") {
            LawyerList(
                lawyers = uiState.visibleLawyers,
                onWrite = { onAction(LegalHelpAction.OnWriteToLawyer(it)) },
                onCall = { onAction(LegalHelpAction.OnCallLawyer(it)) },
                onVideoCall = { onAction(LegalHelpAction.OnVideoCallLawyer(it)) },
                onBook = { onAction(LegalHelpAction.OnBookLawyer(it)) },
                modifier = Modifier.padding(horizontal = gutter),
            )
        }
    }
}

@Composable
private fun LegalHelpHeader(onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(AsosSpacing.minTouch)
                .clip(AsosShapes.Circle)
                .clickable(role = Role.Button, onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                AsosIcons.ChevronLeft,
                contentDescription = stringResource(R.string.legalhelp_back),
                tint = AsosColors.Navy,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(
            text = stringResource(R.string.legalhelp_title),
            style = AsosTextStyles.ScreenTitle.copy(fontSize = 24.sp, lineHeight = 30.sp),
            modifier = Modifier
                .padding(start = AsosSpacing.xxs)
                .semantics { heading() },
        )
    }
}

@Composable
private fun QuoteCard(modifier: Modifier = Modifier) {
    AsosCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(AsosSpacing.l),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            ArticleNumberBadge(
                text = QUOTE_ARTICLE.toString(),
                size = 30.dp,
                cornerRadius = 8.dp,
                fontSize = 13.sp,
            )
            Text(
                text = stringResource(R.string.legalhelp_quote),
                style = AsosTextStyles.SerifQuote.copy(fontStyle = FontStyle.Normal, fontSize = 17.sp, lineHeight = 25.sp),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

// ---------------------------------------------------------------------- previews

internal val PreviewLegalHelpState = LegalHelpUiState(
    isLoading = false,
    hotline = HotlineUi(
        displayNumber = "+998 (71) 000-00-00",
        dialNumber = "+998710000000",
        opensAt = "09:00",
        closesAt = "21:00",
        isFree = true,
        isOpenNow = true,
    ),
    lawyers = PreviewLawyers,
)

@AsosPreviews
@Composable
private fun LegalHelpScreenPreview() {
    AsosTheme { LegalHelpScreen(uiState = PreviewLegalHelpState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun LegalHelpScreenLoadingPreview() {
    AsosTheme { LegalHelpScreen(uiState = LegalHelpUiState(), onAction = {}) }
}

@AsosPreviews
@Composable
private fun LegalHelpScreenErrorPreview() {
    AsosTheme {
        LegalHelpScreen(
            uiState = LegalHelpUiState(isLoading = false, errorMessage = "Internet aloqasi yo‘q"),
            onAction = {},
        )
    }
}
