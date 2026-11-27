package uz.asos.feature.legalhelp.help.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.ArticleNumberBadgeVariant
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.help.AnswerArticleUi
import uz.asos.feature.legalhelp.help.AssistantAnswerUi
import uz.asos.feature.legalhelp.help.AssistantUi

private val OnNavyBorder = AsosColors.Surface.copy(alpha = 0.14f)
private val OnNavyField = AsosColors.Surface.copy(alpha = 0.06f)

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AiAssistantCard(
    state: AssistantUi,
    onInputChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onSend: () -> Unit,
    onVoiceClick: () -> Unit,
    onArticleClick: (Int) -> Unit,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Navy, AsosShapes.R24)
            .padding(AsosSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(AsosColors.Brand500, AsosShapes.R14),
                contentAlignment = Alignment.Center,
            ) {
                Icon(AsosIcons.Sparkles, contentDescription = null, tint = AsosColors.Surface, modifier = Modifier.size(22.dp))
            }
            Column(Modifier.padding(start = AsosSpacing.m)) {
                Text(
                    text = state.title.ifBlank { stringResource(R.string.legalhelp_ai_title) },
                    style = AsosTextStyles.CardTitle,
                    color = AsosColors.Surface,
                )
                Text(
                    text = state.subtitle.ifBlank { stringResource(R.string.legalhelp_ai_subtitle) },
                    style = AsosTextStyles.Meta,
                    color = AsosColors.TextOnNavy,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(OnNavyField, AsosShapes.R18)
                .border(1.dp, OnNavyBorder, AsosShapes.R18)
                .padding(AsosSpacing.l),
        ) {
            BasicTextField(
                value = state.input,
                onValueChange = onInputChange,
                textStyle = AsosTextStyles.Body.copy(color = AsosColors.Surface),
                cursorBrush = SolidColor(AsosColors.BrandOnNavy),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (state.canSend) onSend() }),
                minLines = 3,
                maxLines = 6,
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    Box {
                        if (state.input.isEmpty()) {
                            Text(
                                text = stringResource(R.string.legalhelp_ai_placeholder),
                                style = AsosTextStyles.Body,
                                color = AsosColors.TextOnNavy.copy(alpha = 0.75f),
                            )
                        }
                        inner()
                    }
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AsosSpacing.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = AsosSpacing.minTouch)
                        .clip(AsosShapes.R10)
                        .clickable(role = Role.Button, onClick = onVoiceClick),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(AsosIcons.Microphone, contentDescription = null, tint = AsosColors.TextOnNavy, modifier = Modifier.size(16.dp))
                    Text(
                        text = stringResource(R.string.legalhelp_ai_voice),
                        style = AsosTextStyles.Tag,
                        color = AsosColors.TextOnNavy,
                        modifier = Modifier.padding(start = AsosSpacing.xs),
                    )
                }
                SendButton(enabled = state.canSend, isSending = state.isSending, onClick = onSend)
            }
        }

        if (state.suggestions.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
                verticalArrangement = Arrangement.spacedBy(AsosSpacing.s),
            ) {
                state.suggestions.forEach { label ->
                    OnNavyChip(label = label, onClick = { onSuggestionClick(label) })
                }
            }
        }

        if (state.isSending) {
            Text(
                text = stringResource(R.string.legalhelp_ai_thinking),
                style = AsosTextStyles.Meta,
                color = AsosColors.TextOnNavy,
            )
        }
        if (state.isTooShort) {
            Text(
                text = stringResource(R.string.legalhelp_ai_too_short, AssistantUi.MIN_QUESTION_LENGTH),
                style = AsosTextStyles.Meta,
                color = AsosColors.WarningBg,
            )
        }
        state.errorMessage?.let { message ->
            Text(text = message, style = AsosTextStyles.Meta, color = AsosColors.ErrorBg)
        }
        if (state.needsSignIn) {
            SignInRow(onSignIn = onSignIn)
        }
        state.answer?.let { answer ->
            AssistantAnswer(answer = answer, onArticleClick = onArticleClick)
        }

        Text(
            text = state.disclaimer.ifBlank { stringResource(R.string.legalhelp_ai_disclaimer) },
            style = AsosTextStyles.Meta.copy(fontSize = AsosTextStyles.Tag.fontSize),
            color = AsosColors.TextOnNavy.copy(alpha = 0.8f),
        )
    }
}

@Composable
private fun SendButton(enabled: Boolean, isSending: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(AsosSpacing.minTouch)
            .clip(AsosShapes.R14)
            .background(if (enabled || isSending) AsosColors.Surface else AsosColors.Surface.copy(alpha = 0.4f))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isSending) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = AsosColors.Brand600,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                AsosIcons.ArrowUp,
                contentDescription = stringResource(R.string.legalhelp_ai_send),
                tint = AsosColors.Brand600,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun OnNavyChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .heightIn(min = AsosSpacing.minTouch)
            .clip(AsosShapes.Pill)
            .border(1.dp, OnNavyBorder, AsosShapes.Pill)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = AsosSpacing.mm),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, style = AsosTextStyles.Tag.copy(fontSize = AsosTextStyles.Chip.fontSize), color = AsosColors.Surface)
    }
}

@Composable
private fun SignInRow(onSignIn: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(OnNavyField, AsosShapes.R14)
            .padding(start = AsosSpacing.m, end = AsosSpacing.xxs, top = AsosSpacing.xxs, bottom = AsosSpacing.xxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.legalhelp_ai_sign_in),
            style = AsosTextStyles.Meta,
            color = AsosColors.Surface,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .heightIn(min = AsosSpacing.minTouch)
                .clip(AsosShapes.R12)
                .background(AsosColors.Surface)
                .clickable(role = Role.Button, onClick = onSignIn)
                .padding(horizontal = AsosSpacing.l),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.legalhelp_sign_in), style = AsosTextStyles.Tag, color = AsosColors.Brand600)
        }
    }
}

@Composable
private fun AssistantAnswer(answer: AssistantAnswerUi, onArticleClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AsosColors.Surface, AsosShapes.R18)
            .padding(AsosSpacing.l),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        if (answer.text.isNotBlank()) {
            Text(text = answer.text, style = AsosTextStyles.BodySmall, color = AsosColors.Navy)
        }
        answer.articles.forEach { article ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AsosShapes.R14)
                    .background(AsosColors.Brand50)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = stringResource(R.string.legalhelp_chat_open_article),
                        onClick = { onArticleClick(article.number) },
                    )
                    .padding(AsosSpacing.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArticleNumberBadge(
                    text = article.number.toString(),
                    variant = ArticleNumberBadgeVariant.Brand,
                    size = 36.dp,
                    cornerRadius = 10.dp,
                )
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = AsosSpacing.m),
                ) {
                    Text(
                        text = stringResource(R.string.legalhelp_article_label, article.number),
                        style = AsosTextStyles.RowTitle,
                    )
                    Text(text = article.title, style = AsosTextStyles.Meta)
                    article.hint?.let { hint ->
                        Text(
                            text = hint,
                            style = AsosTextStyles.Meta.copy(color = AsosColors.Text3),
                            modifier = Modifier.padding(top = AsosSpacing.xxs),
                        )
                    }
                }
                Icon(AsosIcons.ChevronRight, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun AiAssistantCardPreview() {
    AsosTheme {
        AiAssistantCard(
            state = AssistantUi(
                suggestions = listOf("Ish haqi kechikdi", "Ijara muammosi", "Talaba huquqlari"),
            ),
            onInputChange = {},
            onSuggestionClick = {},
            onSend = {},
            onVoiceClick = {},
            onArticleClick = {},
            onSignIn = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun AiAssistantCardAnswerPreview() {
    AsosTheme {
        AiAssistantCard(
            state = AssistantUi(
                input = "Ijara muammosi",
                suggestions = listOf("Ish haqi kechikdi", "Ijara muammosi", "Talaba huquqlari"),
                answer = AssistantAnswerUi(
                    text = "Uy-joy daxlsiz. Avval uy egasiga yozma ogohlantirish yuboring.",
                    articles = listOf(
                        AnswerArticleUi(31, "Shaxsiy hayot va uy-joy daxlsizligi", "Har kim uy-joy daxlsizligi huquqiga ega."),
                    ),
                ),
            ),
            onInputChange = {},
            onSuggestionClick = {},
            onSend = {},
            onVoiceClick = {},
            onArticleClick = {},
            onSignIn = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
