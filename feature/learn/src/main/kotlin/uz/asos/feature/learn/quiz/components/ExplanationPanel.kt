/*
 * ExplanationPanel.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * Bottom panel after an answer: "To‘g‘ri!" (green) or "Afsus, noto‘g‘ri" (red), points or the
 * right answer, the explanation, "Moddani o‘qish" chip and the buttons.
 *
 * The design's left button is "Qaytadan", but the API accepts one answer per question (409), so
 * here it is "Tugatish" (finish the test now). "Qaytadan" lives on the result screen instead.
 *
 * PickAnswerBar is the disabled "Javobni tanlang" button shown before answering.
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.quiz.FeedbackUi

private val PanelShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

@Composable
internal fun ExplanationPanel(
    feedback: FeedbackUi,
    isFinishing: Boolean,
    onArticleClick: (Int) -> Unit,
    onFinishClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleColor = if (feedback.isCorrect) AsosColors.SuccessOnLight else AsosColors.ErrorOnLight
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = PanelShape, ambientColor = AsosColors.Navy.copy(alpha = 0.10f), spotColor = AsosColors.Navy.copy(alpha = 0.10f))
            .clip(PanelShape)
            .background(if (feedback.isCorrect) AsosColors.SuccessBg else AsosColors.ErrorPanel)
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = stringResource(if (feedback.isCorrect) R.string.learn_quiz_correct else R.string.learn_quiz_wrong),
                style = AsosTextStyles.SectionTitle,
                color = titleColor,
                modifier = Modifier.weight(1f),
            )
            val side = when {
                feedback.isCorrect && feedback.pointsEarned > 0 ->
                    stringResource(R.string.learn_quiz_points, feedback.pointsEarned)
                !feedback.isCorrect && feedback.correctAnswerText != null ->
                    stringResource(R.string.learn_quiz_correct_answer, feedback.correctAnswerText)
                else -> null
            }
            if (side != null) {
                Text(
                    text = side,
                    style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.ExtraBold),
                    color = titleColor,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f, fill = false),
                )
            }
        }

        if (feedback.explanation != null) {
            Text(
                text = feedback.explanation,
                style = AsosTextStyles.Body.copy(fontWeight = FontWeight.Normal),
                color = AsosColors.Navy,
            )
        }

        if (feedback.articleNumber != null) {
            ArticleChip(number = feedback.articleNumber, onClick = { onArticleClick(feedback.articleNumber) })
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (!feedback.isLastQuestion) {
                AsosButton(
                    text = stringResource(R.string.learn_quiz_finish),
                    onClick = onFinishClick,
                    variant = AsosButtonVariant.Secondary,
                    enabled = !isFinishing,
                )
            }
            AsosButton(
                text = stringResource(if (feedback.isLastQuestion) R.string.learn_quiz_see_result else R.string.learn_quiz_next),
                onClick = onNextClick,
                enabled = !isFinishing,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** White chip: navy serif number badge + "Moddani o‘qish". */
@Composable
private fun ArticleChip(number: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(AsosShapes.R10)
            .background(AsosColors.Surface)
            .border(1.dp, AsosColors.Navy.copy(alpha = 0.08f), AsosShapes.R10)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = 6.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .heightIn(min = 26.dp)
                .background(AsosColors.Navy, AsosShapes.R7)
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = number.toString(),
                style = AsosTextStyles.SerifRoman.copy(fontSize = 13.sp, lineHeight = 26.sp, fontWeight = FontWeight.Bold),
                color = AsosColors.Surface,
            )
        }
        Text(
            text = stringResource(R.string.learn_quiz_read_article),
            style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Bold),
            color = AsosColors.Navy,
        )
    }
}

/** Before answering: grey disabled button. */
@Composable
internal fun PickAnswerBar(
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 30.dp),
    ) {
        AsosButton(
            text = stringResource(if (isSubmitting) R.string.learn_quiz_checking else R.string.learn_quiz_pick),
            onClick = {},
            enabled = false,
            height = 54.dp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExplanationPanelPreview() {
    AsosTheme {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            ExplanationPanel(
                feedback = FeedbackUi(
                    isCorrect = true,
                    correctOptionId = 2,
                    pointsEarned = 20,
                    correctAnswerText = null,
                    explanation = "27-moddaga ko‘ra, shaxsni sud qarorisiz 48 soatdan ortiq ushlab turish mumkin emas.",
                    articleNumber = 27,
                    isLastQuestion = false,
                ),
                isFinishing = false,
                onArticleClick = {},
                onFinishClick = {},
                onNextClick = {},
            )
            ExplanationPanel(
                feedback = FeedbackUi(
                    isCorrect = false,
                    correctOptionId = 2,
                    pointsEarned = 0,
                    correctAnswerText = "48 soat",
                    explanation = "27-moddaga ko‘ra, shaxsni sud qarorisiz 48 soatdan ortiq ushlab turish mumkin emas.",
                    articleNumber = 27,
                    isLastQuestion = true,
                ),
                isFinishing = false,
                onArticleClick = {},
                onFinishClick = {},
                onNextClick = {},
            )
            PickAnswerBar(isSubmitting = false)
        }
    }
}
