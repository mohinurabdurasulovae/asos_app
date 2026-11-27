/*
 * AnswerOptionList.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Answer options (design: Javob variantlari). 62 dp rows, 18 dp corners, 2 dp border,
 * 38 dp letter badge. Looks:
 *  - default:  grey border, grey badge
 *  - selected: blue (while the answer is being checked)
 *  - correct:  green border + background, green badge, check icon
 *  - wrong:    red border + background, red badge, x icon
 *  - dimmed:   other options after answering (55 % opacity)
 */
package uz.asos.feature.learn.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.quiz.FeedbackUi
import uz.asos.feature.learn.quiz.OptionUi

internal enum class OptionLook { DEFAULT, SELECTED, CORRECT, WRONG, DIMMED }

@Composable
internal fun AnswerOptionList(
    options: List<OptionUi>,
    selectedOptionId: Int?,
    feedback: FeedbackUi?,
    enabled: Boolean,
    onOptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEach { option ->
            AnswerOptionItem(
                option = option,
                look = lookOf(option.id, selectedOptionId, feedback),
                enabled = enabled,
                onClick = { onOptionClick(option.id) },
            )
        }
    }
}

internal fun lookOf(optionId: Int, selectedOptionId: Int?, feedback: FeedbackUi?): OptionLook = when {
    feedback != null && optionId == feedback.correctOptionId -> OptionLook.CORRECT
    feedback != null && optionId == selectedOptionId -> OptionLook.WRONG
    feedback != null -> OptionLook.DIMMED
    optionId == selectedOptionId -> OptionLook.SELECTED
    else -> OptionLook.DEFAULT
}

private data class OptionColors(
    val border: Color,
    val container: Color,
    val badge: Color,
    val badgeText: Color,
)

private fun OptionLook.colors(): OptionColors = when (this) {
    OptionLook.DEFAULT, OptionLook.DIMMED ->
        OptionColors(AsosColors.Border, AsosColors.Surface, AsosColors.SurfaceMuted, AsosColors.Navy)
    OptionLook.SELECTED ->
        OptionColors(AsosColors.Brand500, AsosColors.Brand25, AsosColors.Brand500, AsosColors.Surface)
    OptionLook.CORRECT ->
        OptionColors(AsosColors.Success, AsosColors.SuccessBgSoft, AsosColors.Success, AsosColors.Surface)
    OptionLook.WRONG ->
        OptionColors(AsosColors.Error, AsosColors.ErrorBg, AsosColors.Error, AsosColors.Surface)
}

@Composable
private fun AnswerOptionItem(
    option: OptionUi,
    look: OptionLook,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val colors = look.colors()
    val correctLabel = stringResource(R.string.learn_quiz_option_correct)
    val wrongLabel = stringResource(R.string.learn_quiz_option_wrong)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 62.dp)
            .alpha(if (look == OptionLook.DIMMED) 0.55f else 1f)
            .clip(AsosShapes.R18)
            .background(colors.container)
            .border(2.dp, colors.border, AsosShapes.R18)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .semantics {
                when (look) {
                    OptionLook.CORRECT -> stateDescription = correctLabel
                    OptionLook.WRONG -> stateDescription = wrongLabel
                    else -> Unit
                }
            }
            .padding(start = 10.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(colors.badge, AsosShapes.R12),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = option.label,
                style = AsosTextStyles.Chip.copy(fontSize = 14.sp, fontWeight = FontWeight.ExtraBold),
                color = colors.badgeText,
            )
        }
        Text(
            text = option.text,
            style = AsosTextStyles.CardTitle.copy(fontSize = 16.5.sp, lineHeight = 22.sp, fontWeight = FontWeight.Bold),
            color = AsosColors.Navy,
            modifier = Modifier.weight(1f),
        )
        when (look) {
            OptionLook.CORRECT -> Icon(
                imageVector = AsosIcons.CheckCircleFilled,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
            OptionLook.WRONG -> Icon(
                imageVector = AsosIcons.XCircleFilled,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
            else -> Unit
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnswerOptionListPreview() {
    val options = listOf(
        OptionUi(1, "A", "24 soat"),
        OptionUi(2, "B", "48 soat"),
        OptionUi(3, "C", "72 soat"),
        OptionUi(4, "D", "12 soat"),
    )
    AsosTheme {
        AnswerOptionList(
            options = options,
            selectedOptionId = 3,
            feedback = FeedbackUi(
                isCorrect = false,
                correctOptionId = 2,
                pointsEarned = 0,
                correctAnswerText = "48 soat",
                explanation = null,
                articleNumber = 27,
                isLastQuestion = false,
            ),
            enabled = false,
            onOptionClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
