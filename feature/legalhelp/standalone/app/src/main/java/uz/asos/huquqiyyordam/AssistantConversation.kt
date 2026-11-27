package uz.asos.huquqiyyordam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
internal fun AssistantConversation(
    state: AssistantChatState,
    onRetry: () -> Unit,
) {
    val pending = state.pendingQuestion
    if (state.turns.isEmpty() && pending == null) return

    val listState = rememberLazyListState()
    LaunchedEffect(state.turns.size, pending) {
        val latestTurn = state.turns.size - if (pending == null) 1 else 0
        if (latestTurn >= 0) listState.scrollToItem(latestTurn)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(state.turns) { turn ->
            ConversationTurn(turn.question) {
                ReplyBubble(turn.reply)
            }
        }
        if (pending != null) {
            item {
                ConversationTurn(pending) {
                    when {
                        state.error != null -> {
                            Column {
                                Text(
                                    state.error,
                                    Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                                    style = asosTextStyle(13.0, 19.0, 600, Color.White),
                                )
                                if (BuildConfig.GEMINI_API_KEY.isNotBlank()) {
                                    TextButton(
                                        onClick = onRetry,
                                        colors = ButtonDefaults.textButtonColors(contentColor = AsosColors.OnNavy),
                                    ) { Text("Qayta urinish") }
                                }
                            }
                        }
                        state.sending -> {
                            Row(
                                Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                                horizontalArrangement = Arrangement.spacedBy(9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                CircularProgressIndicator(
                                    Modifier.size(15.dp),
                                    color = AsosColors.OnNavy,
                                    strokeWidth = 2.dp,
                                )
                                Text(
                                    "Javob tayyorlanmoqda…",
                                    style = asosTextStyle(13.0, 19.0, 600, AsosColors.OnNavy),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationTurn(question: String, reply: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(
            Modifier.align(Alignment.End).widthIn(max = 290.dp)
                .background(AsosColors.ActionBlue, RoundedCornerShape(16.dp))
                .padding(12.dp),
        ) {
            Text(question, style = asosTextStyle(13.0, 19.0, 600, Color.White))
        }
        reply()
    }
}

@Composable
private fun ReplyBubble(reply: AssistantReply) {
    Column(
        Modifier.fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text("Asos AI", style = asosTextStyle(12.0, 17.0, 800, AsosColors.ActionBlue))
        SelectionContainer {
            Text(reply.text, style = asosTextStyle(14.0, 21.0, 400))
        }
    }
}
