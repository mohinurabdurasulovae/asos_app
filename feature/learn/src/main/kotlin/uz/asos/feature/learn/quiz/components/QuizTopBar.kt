/*
 * QuizTopBar.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * X button, progress strip (green = right, red = wrong, blue = current, grey = not yet) and
 * the "5/10" counter.
 */
package uz.asos.feature.learn.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.model.learning.QuestionMark
import uz.asos.feature.learn.R

@Composable
internal fun QuizTopBar(
    marks: List<QuestionMark>,
    currentNumber: Int,
    total: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QuizCloseButton(onClose = onClose)
        val progressLabel = stringResource(R.string.learn_quiz_progress, total, currentNumber)
        Row(
            modifier = Modifier
                .weight(1f)
                .clearAndSetSemantics { contentDescription = progressLabel },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            marks.forEach { mark ->
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .background(mark.color(), AsosShapes.Pill),
                )
            }
        }
        if (total > 0) {
            Text(
                text = stringResource(R.string.learn_quiz_counter, currentNumber, total),
                style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.ExtraBold),
                color = AsosColors.Text2,
                modifier = Modifier.clearAndSetSemantics { },
            )
        }
    }
}

/** 44 dp X button (also used on the sign-in, error and result views). */
@Composable
internal fun QuizCloseButton(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val label = stringResource(R.string.learn_quiz_close)
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(AsosShapes.R14)
            .background(AsosColors.Background)
            .clickable(role = Role.Button, onClickLabel = label, onClick = onClose)
            .clearAndSetSemantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = AsosIcons.Close,
            contentDescription = null,
            tint = AsosColors.Navy,
            modifier = Modifier.size(20.dp),
        )
    }
}

private fun QuestionMark.color() = when (this) {
    QuestionMark.CORRECT -> AsosColors.Success
    QuestionMark.WRONG -> AsosColors.Error
    QuestionMark.CURRENT -> AsosColors.Brand500
    QuestionMark.PENDING -> AsosColors.Track
}

@Preview(showBackground = true)
@Composable
private fun QuizTopBarPreview() {
    AsosTheme {
        QuizTopBar(
            marks = listOf(
                QuestionMark.CORRECT, QuestionMark.CORRECT, QuestionMark.WRONG, QuestionMark.CORRECT, QuestionMark.CURRENT,
            ) + List(5) { QuestionMark.PENDING },
            currentNumber = 5,
            total = 10,
            onClose = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
