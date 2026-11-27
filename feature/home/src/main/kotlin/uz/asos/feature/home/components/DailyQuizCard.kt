package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosTag
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.home.DailyQuizUi
import uz.asos.feature.home.QuizOptionState
import uz.asos.feature.home.QuizOptionUi
import uz.asos.feature.home.R

private const val OPTION_COLUMNS = 2

private data class OptionColors(val border: Color, val container: Color, val content: Color)

@Composable
internal fun DailyQuizCard(
    quiz: DailyQuizUi,
    onOptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Surface, AsosShapes.R24)
            .border(1.5.dp, AsosColors.Brand100, AsosShapes.R24)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        QuizHeader(meta = quiz.meta, streakLabel = quiz.streakLabel)
        Text(text = quiz.question, style = AsosTextStyles.Body.copy(fontSize = 16.sp, lineHeight = 23.sp, fontWeight = FontWeight.Bold))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            quiz.options.chunked(OPTION_COLUMNS).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    row.forEach { option ->
                        AnswerButton(
                            option = option,
                            enabled = !quiz.isLocked,
                            onClick = { onOptionClick(option.id) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                    repeat(OPTION_COLUMNS - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        quiz.explanation?.let { explanation ->
            Text(
                text = explanation,
                style = AsosTextStyles.BodySmall.copy(fontSize = 13.5.sp, lineHeight = 20.sp),
                color = AsosColors.Text3,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AsosColors.Background, AsosShapes.R14)
                    .padding(12.dp),
            )
        }
        Text(text = quiz.footer, style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun QuizHeader(meta: String, streakLabel: String?) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AsosColors.WarningBg, AsosShapes.R12),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AsosIcons.Flame, contentDescription = null, tint = AsosColors.Warning, modifier = Modifier.size(21.dp))
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = stringResource(R.string.home_daily_title), style = AsosTextStyles.RowTitle.copy(fontSize = 16.sp))
            if (meta.isNotBlank()) {
                Text(
                    text = meta,
                    style = AsosTextStyles.Meta.copy(fontSize = 12.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        streakLabel?.let { AsosTag(text = it, tone = AsosTones.Orange) }
    }
}

@Composable
private fun AnswerButton(
    option: QuizOptionUi,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = optionColors(option.state)
    Box(
        modifier = modifier
            .heightIn(min = 46.dp)
            .clip(AsosShapes.R14)
            .background(colors.container)
            .border(1.5.dp, colors.border, AsosShapes.R14)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = option.text,
            style = AsosTextStyles.RowTitleRegular,
            color = colors.content,
            textAlign = TextAlign.Center,
        )
    }
}

private fun optionColors(state: QuizOptionState): OptionColors = when (state) {
    QuizOptionState.DEFAULT -> OptionColors(AsosColors.Border, AsosColors.Surface, AsosColors.Navy)
    QuizOptionState.SELECTED -> OptionColors(AsosColors.Brand500, AsosColors.Brand50, AsosColors.Brand600)
    QuizOptionState.CORRECT -> OptionColors(AsosColors.Success, AsosColors.SuccessBgSoft, AsosColors.SuccessText)
    QuizOptionState.WRONG -> OptionColors(AsosColors.Error, AsosColors.ErrorBg, AsosColors.ErrorOnLight)
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun DailyQuizCardPreview() {
    AsosTheme {
        DailyQuizCard(
            quiz = DailyQuizUi(
                meta = "Shaxsiy huquqlar · O‘rta",
                streakLabel = "7 kun ketma-ket",
                question = "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?",
                options = listOf(
                    QuizOptionUi(1, "24 soat", QuizOptionState.DEFAULT),
                    QuizOptionUi(2, "48 soat", QuizOptionState.CORRECT),
                    QuizOptionUi(3, "72 soat", QuizOptionState.WRONG),
                    QuizOptionUi(4, "12 soat", QuizOptionState.DEFAULT),
                ),
                footer = "To‘g‘ri javob uchun +20 ball",
                explanation = null,
                isAnswered = false,
                isSubmitting = false,
            ),
            onOptionClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
