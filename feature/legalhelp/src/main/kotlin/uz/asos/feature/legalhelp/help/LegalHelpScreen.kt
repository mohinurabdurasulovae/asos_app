package uz.asos.feature.legalhelp.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
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

private const val QUOTE_ARTICLE = 29

@Composable
internal fun LegalHelpRoute(
    onBack: () -> Unit,
    onOpenChat: (conversationId: Int) -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LegalHelpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val currentOnSignIn by rememberUpdatedState(onSignIn)
    val currentOnOpenChat by rememberUpdatedState(onOpenChat)

    fun show(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(message = message, actionLabel = actionLabel)
            if (result == SnackbarResult.ActionPerformed) onAction?.invoke()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LegalHelpEvent.OpenChat -> currentOnOpenChat(event.conversationId)
                LegalHelpEvent.NeedsSignIn -> show(
                    message = context.getString(R.string.legalhelp_sign_in_required),
                    actionLabel = context.getString(R.string.legalhelp_sign_in),
                    onAction = { currentOnSignIn() },
                )
                LegalHelpEvent.CallbackSent -> show(context.getString(R.string.legalhelp_hotline_callback_done))
                LegalHelpEvent.BookingDone -> show(context.getString(R.string.legalhelp_book_done))
                LegalHelpEvent.NoFreeSlot -> show(context.getString(R.string.legalhelp_book_no_slot))
                is LegalHelpEvent.ShowError -> show(event.message)
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onAction(LegalHelpAction.OnResume)
        onPauseOrDispose { }
    }

    LegalHelpScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                LegalHelpAction.OnBackClick -> onBack()
                LegalHelpAction.OnSignInClick -> onSignIn()
                is LegalHelpAction.OnArticleClick -> onOpenArticle(action.articleNumber)
                is LegalHelpAction.OnHotlineCall -> {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${action.dialNumber}"))
                    try {
                        context.startActivity(intent)
                    } catch (@Suppress("UNUSED_PARAMETER") e: ActivityNotFoundException) {
                        show(context.getString(R.string.legalhelp_dialer_missing))
                    }
                }
                LegalHelpAction.OnVoiceInputClick,
                is LegalHelpAction.OnCallLawyer,
                is LegalHelpAction.OnVideoCallLawyer,
                -> show(context.getString(R.string.legalhelp_coming_soon))
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

        uiState.callbackDialog?.let { dialog -> CallbackDialog(dialog = dialog, onAction = onAction) }
        uiState.bookingDialog?.let { dialog -> BookingDialog(dialog = dialog, onAction = onAction) }

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
            QuoteCard(
                quote = uiState.quote,
                onClick = { onAction(LegalHelpAction.OnArticleClick(uiState.quote?.articleNumber ?: QUOTE_ARTICLE)) },
                modifier = Modifier.padding(horizontal = gutter),
            )
        }
        item(key = "assistant") {
            AiAssistantCard(
                state = uiState.assistant,
                onInputChange = { onAction(LegalHelpAction.OnAssistantInputChange(it)) },
                onSuggestionClick = { onAction(LegalHelpAction.OnSuggestionClick(it)) },
                onSend = { onAction(LegalHelpAction.OnAssistantSend) },
                onVoiceClick = { onAction(LegalHelpAction.OnVoiceInputClick) },
                onArticleClick = { onAction(LegalHelpAction.OnArticleClick(it)) },
                onSignIn = { onAction(LegalHelpAction.OnSignInClick) },
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
                specialties = uiState.specialties,
                selected = uiState.selectedSpecialty,
                onSelect = { onAction(LegalHelpAction.OnSpecialtySelect(it)) },
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
                openingChatForLawyerId = uiState.openingChatForLawyerId,
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
private fun QuoteCard(quote: QuoteUi?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val text = quote?.text?.takeIf { it.isNotBlank() }?.let { "“$it”" } ?: stringResource(R.string.legalhelp_quote)
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        onClickLabel = stringResource(R.string.legalhelp_chat_open_article),
    ) {
        Row(
            modifier = Modifier.padding(AsosSpacing.l),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            ArticleNumberBadge(
                text = (quote?.articleNumber ?: QUOTE_ARTICLE).toString(),
                size = 30.dp,
                cornerRadius = 8.dp,
                fontSize = 13.sp,
            )
            Text(
                text = text,
                style = AsosTextStyles.SerifQuote.copy(fontStyle = FontStyle.Normal, fontSize = 17.sp, lineHeight = 25.sp),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CallbackDialog(dialog: CallbackDialogUi, onAction: (LegalHelpAction) -> Unit) {
    AlertDialog(
        onDismissRequest = { onAction(LegalHelpAction.OnCallbackDismiss) },
        containerColor = AsosColors.Surface,
        shape = AsosShapes.R22,
        title = { Text(stringResource(R.string.legalhelp_callback_title), style = AsosTextStyles.CardTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.m)) {
                Text(stringResource(R.string.legalhelp_callback_message), style = AsosTextStyles.BodySmall)
                OutlinedTextField(
                    value = dialog.phone,
                    onValueChange = { onAction(LegalHelpAction.OnCallbackPhoneChange(it)) },
                    label = { Text(stringResource(R.string.legalhelp_callback_phone)) },
                    singleLine = true,
                    isError = dialog.isInvalid,
                    enabled = !dialog.isSending,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onAction(LegalHelpAction.OnCallbackConfirm) }),
                    supportingText = if (dialog.isInvalid || dialog.errorMessage != null) {
                        {
                            Text(
                                if (dialog.isInvalid) {
                                    stringResource(R.string.legalhelp_callback_invalid)
                                } else {
                                    dialog.errorMessage.orEmpty()
                                },
                            )
                        }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onAction(LegalHelpAction.OnCallbackConfirm) }, enabled = !dialog.isSending) {
                Text(stringResource(R.string.legalhelp_callback_send), style = AsosTextStyles.Button, color = AsosColors.Brand600)
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(LegalHelpAction.OnCallbackDismiss) }) {
                Text(stringResource(R.string.legalhelp_cancel), style = AsosTextStyles.Button, color = AsosColors.Text2)
            }
        },
    )
}

@Composable
private fun BookingDialog(dialog: BookingDialogUi, onAction: (LegalHelpAction) -> Unit) {
    val message = if (dialog.slot.isToday) {
        stringResource(R.string.legalhelp_book_message_today, dialog.lawyerName, dialog.slot.time)
    } else {
        stringResource(R.string.legalhelp_book_message_on, dialog.lawyerName, dialog.slot.dateLabel, dialog.slot.time)
    }
    AlertDialog(
        onDismissRequest = { onAction(LegalHelpAction.OnBookingDismiss) },
        containerColor = AsosColors.Surface,
        shape = AsosShapes.R22,
        title = { Text(stringResource(R.string.legalhelp_book_title), style = AsosTextStyles.CardTitle) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                Text(message, style = AsosTextStyles.BodySmall)
                dialog.errorMessage?.let { Text(it, style = AsosTextStyles.Meta, color = AsosColors.ErrorOnLight) }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAction(LegalHelpAction.OnBookingConfirm) }, enabled = !dialog.isSending) {
                Text(stringResource(R.string.legalhelp_book_confirm), style = AsosTextStyles.Button, color = AsosColors.Brand600)
            }
        },
        dismissButton = {
            TextButton(onClick = { onAction(LegalHelpAction.OnBookingDismiss) }) {
                Text(stringResource(R.string.legalhelp_cancel), style = AsosTextStyles.Button, color = AsosColors.Text2)
            }
        },
    )
}

internal val PreviewLegalHelpState = LegalHelpUiState(
    isLoading = false,
    quote = QuoteUi(articleNumber = 29, text = "Har kimga malakali yuridik yordam olish huquqi kafolatlanadi."),
    assistant = AssistantUi(
        title = "Asos AI yordamchi",
        subtitle = "Tegishli moddalarni topib, sodda tushuntiradi",
        suggestions = listOf("Ish haqi kechikdi", "Ijara muammosi", "Talaba huquqlari"),
        disclaimer = "AI umumiy huquqiy ma’lumot beradi va advokat maslahati o‘rnini bosmaydi.",
    ),
    hotline = HotlineUi(
        displayNumber = "+998 (71) 000-00-00",
        dialNumber = "+998710000000",
        hours = "09:00–21:00",
        everyDay = true,
        isFree = true,
        isOpenNow = true,
        note = null,
    ),
    lawyers = PreviewLawyers,
    lawyersOnline = 2,
    specialties = listOf("Uy-joy", "Oila", "Mehnat", "Ta’lim"),
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
private fun LegalHelpScreenCallbackPreview() {
    AsosTheme {
        LegalHelpScreen(uiState = PreviewLegalHelpState.copy(callbackDialog = CallbackDialogUi()), onAction = {})
    }
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
