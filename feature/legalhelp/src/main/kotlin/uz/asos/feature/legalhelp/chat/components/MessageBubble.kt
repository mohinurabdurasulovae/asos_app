/*
 * MessageBubble.kt
 * Module: :feature:legalhelp   ·   Work package: WP-10
 *
 * One chat message. Mine: right, Brand600, white text, "14:02 · O‘qildi".
 * Lawyer's: left, white card; may also hold a file and an article link.
 */
package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.chat.ChatArticleUi
import uz.asos.feature.legalhelp.chat.ChatFileUi
import uz.asos.feature.legalhelp.chat.ChatMessageUi

private val MyBubbleShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 6.dp)
private val TheirBubbleShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 6.dp, bottomEnd = 20.dp)

@Composable
internal fun MessageBubble(
    message: ChatMessageUi,
    onFileClick: (String) -> Unit,
    onArticleClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasCards = message.file != null || message.article != null
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (message.fromMe) Alignment.End else Alignment.Start,
    ) {
        Column(
            modifier = Modifier
                // Bubbles with cards are wider, like on the canvas.
                .widthIn(max = if (hasCards) 320.dp else 290.dp)
                .then(
                    if (message.fromMe) {
                        Modifier.background(AsosColors.Brand600, MyBubbleShape)
                    } else {
                        Modifier
                            .softShadow(TheirBubbleShape, AsosElevation.card)
                            .background(AsosColors.Surface, TheirBubbleShape)
                    },
                )
                .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.m),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            if (message.text.isNotEmpty()) {
                Text(
                    text = message.text,
                    style = AsosTextStyles.Body,
                    color = if (message.fromMe) AsosColors.Surface else AsosColors.Navy,
                )
            }
            message.file?.let { file -> FileAttachmentMessage(file = file, onClick = { onFileClick(file.name) }) }
            message.article?.let { article -> ArticleLinkMessage(article = article, onClick = { onArticleClick(article.number) }) }
        }
        val meta = if (message.fromMe && message.isRead) {
            "${message.time} · ${stringResource(R.string.legalhelp_chat_read)}"
        } else {
            message.time
        }
        Text(
            text = meta,
            style = AsosTextStyles.Meta.copy(fontSize = AsosTextStyles.Tag.fontSize),
            modifier = Modifier.padding(top = AsosSpacing.xxs, start = AsosSpacing.xxs, end = AsosSpacing.xxs),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun MessageBubblePreview() {
    AsosTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MessageBubble(
                message = ChatMessageUi(1, fromMe = true, text = "Yo‘q, oddiy yozma shartnoma.", time = "14:06", isRead = true),
                onFileClick = {},
                onArticleClick = {},
            )
            MessageBubble(
                message = ChatMessageUi(
                    id = 2,
                    fromMe = false,
                    text = "Tushunarli. Birinchi qadam — uy egasiga yozma ogohlantirish.",
                    time = "14:08",
                    isRead = false,
                    file = ChatFileUi("Ogohlantirish_xati.docx", "24 KB · Word"),
                    article = ChatArticleUi(31, "Shaxsiy hayot va uy-joy daxlsizligi"),
                ),
                onFileClick = {},
                onArticleClick = {},
            )
        }
    }
}
