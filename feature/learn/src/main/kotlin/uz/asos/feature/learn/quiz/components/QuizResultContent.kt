package uz.asos.feature.learn.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.component.ProgressRing
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.quiz.QuizResultUi

@Composable
internal fun QuizResultContent(
    result: QuizResultUi,
    onRestart: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 30.dp),
    ) {
        QuizCloseButton(onClose = onClose)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            val ringColor = if (result.isPerfect) AsosColors.Success else AsosColors.Brand500
            val trackColor = if (result.isPerfect) AsosColors.SuccessBg else AsosColors.Brand50
            ProgressRing(
                progress = result.percent / 100f,
                size = 148.dp,
                strokeWidth = 14.dp,
                color = ringColor,
                trackColor = trackColor,
                contentDescription = stringResource(R.string.learn_quiz_result_ring, result.percent),
            ) {
                Text(
                    text = "${result.percent}%",
                    style = AsosTextStyles.SerifNumber.copy(fontSize = 38.sp, lineHeight = 38.sp),
                    color = AsosColors.Navy,
                )
            }
            Text(
                text = stringResource(if (result.isPerfect) R.string.learn_quiz_result_perfect else R.string.learn_quiz_result_title),
                style = AsosTextStyles.ScreenTitle.copy(fontSize = 23.sp, lineHeight = 30.sp, letterSpacing = (-0.015).em),
                color = AsosColors.Navy,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() },
            )
            Text(
                text = stringResource(R.string.learn_quiz_result_score, result.correct, result.total),
                style = AsosTextStyles.Body,
                color = AsosColors.Text2,
                textAlign = TextAlign.Center,
            )
            if (result.points > 0) {
                Row(
                    modifier = Modifier
                        .background(AsosColors.Brand50, AsosShapes.Pill)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(AsosIcons.Star, contentDescription = null, tint = AsosColors.Brand500, modifier = Modifier.size(16.dp))
                    Text(
                        text = stringResource(R.string.learn_quiz_result_points, result.points),
                        style = AsosTextStyles.Chip.copy(fontWeight = FontWeight.ExtraBold),
                        color = AsosColors.Brand600,
                    )
                }
            }
            result.newAchievements.forEach { title ->
                Row(
                    modifier = Modifier
                        .background(AsosColors.TealBg, AsosShapes.R14)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(AsosColors.Surface, AsosShapes.Circle),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(AsosIcons.Trophy, contentDescription = null, tint = AsosColors.TealIcon, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.learn_quiz_result_new_achievement),
                            style = AsosTextStyles.Overline,
                            color = AsosColors.Teal,
                        )
                        Text(
                            text = title,
                            style = AsosTextStyles.RowTitle.copy(fontWeight = FontWeight.ExtraBold),
                            color = AsosColors.Navy,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AsosButton(
                text = stringResource(R.string.learn_quiz_restart),
                onClick = onRestart,
                variant = AsosButtonVariant.Secondary,
            )
            AsosButton(
                text = stringResource(R.string.learn_quiz_back_to_learn),
                onClick = onClose,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 760)
@Composable
private fun QuizResultContentPreview() {
    AsosTheme {
        QuizResultContent(
            result = QuizResultUi(correct = 8, total = 10, percent = 80, points = 160, isPerfect = false, newAchievements = listOf("Burchlar bilimdoni")),
            onRestart = {},
            onClose = {},
        )
    }
}
