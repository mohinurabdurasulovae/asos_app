package uz.asos.feature.learn.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import java.util.Locale

@Composable
internal fun QuestionCard(
    quizTitle: String,
    difficultyLabel: String,
    questionStartedAt: Long,
    frozenSeconds: Int?,
    question: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (quizTitle.isNotBlank()) {
                    QuizChip(
                        text = quizTitle,
                        textColor = AsosColors.Brand600,
                        container = AsosColors.Brand50,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                }
                if (difficultyLabel.isNotBlank()) {
                    QuizChip(text = difficultyLabel, textColor = AsosColors.Text3, container = AsosColors.SurfaceMuted)
                }
            }
            QuizTimer(startedAt = questionStartedAt, frozenSeconds = frozenSeconds)
        }
        Text(
            text = question,
            style = AsosTextStyles.ScreenTitle.copy(
                fontSize = 23.sp,
                lineHeight = 30.4.sp,
                letterSpacing = (-0.015).em,
            ),
            color = AsosColors.Navy,
            modifier = Modifier.semantics { heading() },
        )
    }
}

@Composable
private fun QuizChip(
    text: String,
    textColor: Color,
    container: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = AsosTextStyles.Tag.copy(fontWeight = FontWeight.ExtraBold),
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .background(container, AsosShapes.Pill)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Composable
private fun QuizTimer(startedAt: Long, frozenSeconds: Int?) {
    val seconds by produceState(initialValue = frozenSeconds ?: elapsedSeconds(startedAt), startedAt, frozenSeconds) {
        if (frozenSeconds != null) {
            value = frozenSeconds
        } else {
            while (true) {
                value = elapsedSeconds(startedAt)
                delay(1_000)
            }
        }
    }
    val label = stringResource(R.string.learn_quiz_timer)
    val text = String.format(Locale.ROOT, "%02d:%02d", seconds / 60, seconds % 60)
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) { contentDescription = "$label $text" },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Icon(
            imageVector = AsosIcons.Clock,
            contentDescription = null,
            tint = AsosColors.Text2,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = text,
            style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold),
            color = AsosColors.Text2,
        )
    }
}

private fun elapsedSeconds(startedAt: Long): Int =
    if (startedAt <= 0L) 0 else ((System.currentTimeMillis() - startedAt) / 1_000L).toInt().coerceAtLeast(0)

@Preview(showBackground = true)
@Composable
private fun QuestionCardPreview() {
    AsosTheme {
        QuestionCard(
            quizTitle = "Shaxsiy huquqlar",
            difficultyLabel = "O‘rta",
            questionStartedAt = 0L,
            frozenSeconds = 42,
            question = "Konstitutsiyaga ko‘ra, shaxs sud qarorisiz necha soatdan ortiq ushlab turilishi mumkin emas?",
            modifier = Modifier.padding(20.dp),
        )
    }
}
