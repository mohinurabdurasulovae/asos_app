/*
 * ChatScreen.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * ChatRoute connects the ViewModel and handles navigation, file opening and snackbars;
 * ChatScreen(uiState, onAction) is stateless and previewable.
 *
 * Layout: ChatTopBar · day headers ("Bugun") + MessageBubbles ·
 *         QuickReplyChips · ChatComposer (above the keyboard).
 */
package uz.asos.feature.legalhelp.chat

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.core.ui.state.EmptyState
import uz.asos.core.ui.state.ErrorState
import uz.asos.core.ui.state.LoadingState
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.chat.components.ChatComposer
import uz.asos.feature.legalhelp.chat.components.ChatTopBar
import uz.asos.feature.legalhelp.chat.components.MessageBubble
import uz.asos.feature.legalhelp.chat.components.QuickReplyChips
import uz.asos.feature.legalhelp.ui.AvatarTone

@Composable
internal fun ChatRoute(
    onBack: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun show(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    // Refresh for the lawyer's replies only while the chat is on screen.
    LifecycleResumeEffect(viewModel) {
        viewModel.onAction(ChatAction.OnResume)
        onPauseOrDispose { viewModel.onAction(ChatAction.OnPause) }
    }

    LaunchedEffect(uiState.sendError) {
        uiState.sendError?.let { message ->
            show(message)
            viewModel.onAction(ChatAction.OnSendErrorShown)
        }
    }

    ChatScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                ChatAction.OnBackClick -> onBack()
                ChatAction.OnSignInClick -> onSignIn()
                is ChatAction.OnArticleClick -> onOpenArticle(action.articleNumber)
                is ChatAction.OnFileClick -> {
                    val url = action.file.url
                    if (url.isNullOrBlank()) {
                        show(context.getString(R.string.legalhelp_chat_file_unavailable))
                    } else {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        } catch (@Suppress("UNUSED_PARAMETER") e: ActivityNotFoundException) {
                            show(context.getString(R.string.legalhelp_chat_file_unavailable))
                        }
                    }
                }
                // No calls, uploads or voice messages in the API yet.
                ChatAction.OnCallClick,
                ChatAction.OnVideoCallClick,
                ChatAction.OnAttachClick,
                ChatAction.OnVoiceClick,
                -> show(context.getString(R.string.legalhelp_coming_soon))
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun ChatScreen(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val quickReplies = listOf(
        stringResource(R.string.legalhelp_quick_thanks),
        stringResource(R.string.legalhelp_quick_next_step),
        stringResource(R.string.legalhelp_quick_can_call),
    )
    val showComposer = uiState.lawyer != null && !uiState.needsSignIn

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background),
    ) {
        Column(Modifier.fillMaxSize()) {
            ChatTopBar(
                lawyer = uiState.lawyer,
                onBack = { onAction(ChatAction.OnBackClick) },
                onCall = { onAction(ChatAction.OnCallClick) },
                onVideoCall = { onAction(ChatAction.OnVideoCallClick) },
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                when {
                    uiState.needsSignIn -> EmptyState(
                        title = stringResource(R.string.legalhelp_chat_sign_in_title),
                        message = stringResource(R.string.legalhelp_chat_sign_in_message),
                        icon = AsosIcons.Lock,
                        actionLabel = stringResource(R.string.legalhelp_sign_in),
                        onAction = { onAction(ChatAction.OnSignInClick) },
                    )

                    uiState.isLoading && uiState.messages.isEmpty() -> LoadingState()

                    uiState.errorMessage != null && uiState.lawyer == null -> ErrorState(
                        title = stringResource(R.string.legalhelp_chat_load_error_title),
                        message = uiState.errorMessage,
                        onRetry = { onAction(ChatAction.OnRetry) },
                    )

                    uiState.messages.isEmpty() -> EmptyState(
                        title = stringResource(R.string.legalhelp_chat_empty_title),
                        message = stringResource(R.string.legalhelp_chat_empty_message),
                        icon = AsosIcons.Comment,
                    )

                    else -> MessageList(uiState = uiState, onAction = onAction)
                }
            }

            if (showComposer) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AsosColors.Surface)
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(top = AsosSpacing.s),
                ) {
                    QuickReplyChips(
                        replies = quickReplies,
                        onClick = { onAction(ChatAction.OnQuickReply(it)) },
                        enabled = !uiState.isSending,
                    )
                    ChatComposer(
                        value = uiState.input,
                        onValueChange = { onAction(ChatAction.OnInputChange(it)) },
                        canSend = uiState.canSend,
                        onSend = { onAction(ChatAction.OnSend) },
                        onAttach = { onAction(ChatAction.OnAttachClick) },
                        onVoice = { onAction(ChatAction.OnVoiceClick) },
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = if (showComposer) 140.dp else 0.dp),
        )
    }
}

@Composable
private fun MessageList(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
) {
    val listState = rememberLazyListState()
    val todayLabel = stringResource(R.string.legalhelp_chat_today)

    // One header per day, then that day's messages.
    val rows = remember(uiState.messages, uiState.today) {
        buildList {
            var lastDay: String? = null
            uiState.messages.forEach { message ->
                if (message.day != lastDay) {
                    lastDay = message.day
                    add(ChatRow.Day(key = "day-${message.day}", isToday = message.day == uiState.today, label = message.dayLabel))
                }
                add(ChatRow.Message(message))
            }
        }
    }

    // Keep the newest message in view.
    LaunchedEffect(rows.size) {
        if (rows.isNotEmpty()) listState.animateScrollToItem(rows.lastIndex)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AsosSpacing.l, vertical = AsosSpacing.l),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        rows.forEach { row ->
            when (row) {
                is ChatRow.Day -> item(key = row.key) {
                    DayHeader(label = if (row.isToday || row.label.isEmpty()) todayLabel else row.label)
                }
                is ChatRow.Message -> item(key = row.message.id) {
                    MessageBubble(
                        message = row.message,
                        onFileClick = { onAction(ChatAction.OnFileClick(it)) },
                        onArticleClick = { onAction(ChatAction.OnArticleClick(it)) },
                    )
                }
            }
        }
    }
}

private sealed interface ChatRow {
    data class Day(val key: String, val isToday: Boolean, val label: String) : ChatRow
    data class Message(val message: ChatMessageUi) : ChatRow
}

@Composable
private fun DayHeader(label: String) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = label,
            style = AsosTextStyles.Tag,
            color = AsosColors.Text2,
            modifier = Modifier
                .background(AsosColors.SegmentTrack, AsosShapes.Pill)
                .padding(horizontal = AsosSpacing.m, vertical = AsosSpacing.xxs),
        )
    }
}

// ---------------------------------------------------------------------- previews

private val PreviewLawyer = ChatLawyerUi(
    name = "Sardor Rahimov",
    initials = "SR",
    isOnline = true,
    isVerified = true,
    avatarTone = AvatarTone.NAVY,
)

private fun previewMessage(id: Int, mine: Boolean, text: String, time: String) = ChatMessageUi(
    id = id, fromMe = mine, text = text, time = time, day = "2026-11-21", dayLabel = "21.11.2026", isRead = mine,
)

internal val PreviewChatState = ChatUiState(
    isLoading = false,
    today = "2026-11-21",
    lawyer = PreviewLawyer,
    messages = listOf(
        previewMessage(1, true, "Assalomu alaykum. Uy egasi men yo‘qligimda kvartiraga kirib turibdi.", "14:02"),
        previewMessage(2, false, "Va alaykum assalom, Aziza. Ijara shartnomangiz notarial tasdiqlanganmi?", "14:05"),
        previewMessage(3, true, "Yo‘q, oddiy yozma shartnoma.", "14:06"),
        previewMessage(
            4,
            false,
            "Tushunarli. Birinchi qadam — uy egasiga yozma ogohlantirish. Namunani va tayanadigan moddani yubordim:",
            "14:08",
        ).copy(
            files = listOf(ChatFileUi("Ogohlantirish_xati.docx", "24 KB · Word", url = null)),
            articles = listOf(ChatArticleUi(31, "Shaxsiy hayot va uy-joy daxlsizligi")),
        ),
    ),
)

@AsosPreviews
@Composable
private fun ChatScreenPreview() {
    AsosTheme { ChatScreen(uiState = PreviewChatState, onAction = {}) }
}

@AsosPreviews
@Composable
private fun ChatScreenEmptyPreview() {
    AsosTheme { ChatScreen(uiState = ChatUiState(isLoading = false, lawyer = PreviewLawyer), onAction = {}) }
}

@AsosPreviews
@Composable
private fun ChatScreenSignInPreview() {
    AsosTheme { ChatScreen(uiState = ChatUiState(isLoading = false, needsSignIn = true), onAction = {}) }
}

@AsosPreviews
@Composable
private fun ChatScreenErrorPreview() {
    AsosTheme {
        ChatScreen(uiState = ChatUiState(isLoading = false, errorMessage = "Ma’lumot topilmadi."), onAction = {})
    }
}
