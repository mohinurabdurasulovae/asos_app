/*
 * ChatScreen.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 * Screen: Advokat bilan chat (design artboard: Chat.dc.html)
 *
 * ChatRoute connects the ViewModel and handles navigation and snackbars;
 * ChatScreen(uiState, onAction) is stateless and previewable.
 *
 * Layout: ChatTopBar · "Bugun" pill + MessageBubbles + TypingIndicator ·
 *         QuickReplyChips · ChatComposer (above the keyboard).
 */
package uz.asos.feature.legalhelp.chat

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
import androidx.compose.foundation.lazy.items
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
import uz.asos.feature.legalhelp.chat.components.TypingIndicator
import uz.asos.feature.legalhelp.data.temp.AvatarTone

@Composable
internal fun ChatRoute(
    onBack: () -> Unit,
    onOpenArticle: (number: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val comingSoon: () -> Unit = {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(context.getString(R.string.legalhelp_coming_soon))
        }
    }

    ChatScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                ChatAction.OnBackClick -> onBack()
                is ChatAction.OnArticleClick -> onOpenArticle(action.articleNumber)
                is ChatAction.OnFileClick,
                ChatAction.OnCallClick,
                ChatAction.OnVideoCallClick,
                ChatAction.OnAttachClick,
                ChatAction.OnVoiceClick,
                -> comingSoon()
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
                    uiState.isLoading && uiState.messages.isEmpty() -> LoadingState()

                    uiState.errorMessage != null && uiState.lawyer == null -> ErrorState(
                        title = stringResource(R.string.legalhelp_chat_load_error_title),
                        message = uiState.errorMessage,
                        onRetry = { onAction(ChatAction.OnRetry) },
                    )

                    uiState.messages.isEmpty() && !uiState.isLawyerTyping -> EmptyState(
                        title = stringResource(R.string.legalhelp_chat_empty_title),
                        message = stringResource(R.string.legalhelp_chat_empty_message),
                        icon = AsosIcons.Comment,
                    )

                    else -> MessageList(uiState = uiState, onAction = onAction)
                }
            }

            if (uiState.lawyer != null) {
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
                .padding(bottom = 140.dp),
        )
    }
}

@Composable
private fun MessageList(
    uiState: ChatUiState,
    onAction: (ChatAction) -> Unit,
) {
    val listState = rememberLazyListState()
    // Keep the newest message (or the typing indicator) in view.
    val lastIndex = uiState.messages.size + if (uiState.isLawyerTyping) 1 else 0
    LaunchedEffect(lastIndex) {
        listState.animateScrollToItem(lastIndex)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = AsosSpacing.l, vertical = AsosSpacing.l),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        item(key = "day") {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.legalhelp_chat_today),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.Text2,
                    modifier = Modifier
                        .background(AsosColors.SegmentTrack, AsosShapes.Pill)
                        .padding(horizontal = AsosSpacing.m, vertical = AsosSpacing.xxs),
                )
            }
        }
        items(uiState.messages, key = { it.id }) { message ->
            MessageBubble(
                message = message,
                onFileClick = { onAction(ChatAction.OnFileClick(it)) },
                onArticleClick = { onAction(ChatAction.OnArticleClick(it)) },
            )
        }
        if (uiState.isLawyerTyping && uiState.lawyer != null) {
            item(key = "typing") {
                TypingIndicator(name = uiState.lawyer.firstName)
            }
        }
    }
}

// ---------------------------------------------------------------------- previews

private val PreviewLawyer = ChatLawyerUi(
    name = "Sardor Rahimov",
    firstName = "Sardor",
    initials = "SR",
    isOnline = true,
    isVerified = true,
    avatarTone = AvatarTone.NAVY,
)

internal val PreviewChatState = ChatUiState(
    isLoading = false,
    lawyer = PreviewLawyer,
    messages = listOf(
        ChatMessageUi(1, true, "Assalomu alaykum. Uy egasi men yo‘qligimda kvartiraga kirib turibdi.", "14:02", true),
        ChatMessageUi(2, false, "Va alaykum assalom, Aziza. Ijara shartnomangiz notarial tasdiqlanganmi?", "14:05", false),
        ChatMessageUi(3, true, "Yo‘q, oddiy yozma shartnoma.", "14:06", true),
        ChatMessageUi(
            id = 4,
            fromMe = false,
            text = "Tushunarli. Birinchi qadam — uy egasiga yozma ogohlantirish. Namunani va tayanadigan moddani yubordim:",
            time = "14:08",
            isRead = false,
            file = ChatFileUi("Ogohlantirish_xati.docx", "24 KB · Word"),
            article = ChatArticleUi(31, "Shaxsiy hayot va uy-joy daxlsizligi"),
        ),
    ),
    isLawyerTyping = true,
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
private fun ChatScreenErrorPreview() {
    AsosTheme {
        ChatScreen(uiState = ChatUiState(isLoading = false, errorMessage = "Suhbat topilmadi"), onAction = {})
    }
}
