package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.ProgressRing
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.feature.learn.R
import uz.asos.feature.learn.hub.OverallProgressUi

@Composable
internal fun OverallProgressCard(
    progress: OverallProgressUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(AsosShapes.R26, elevation = AsosElevation.raised)
            .background(AsosColors.Surface, AsosShapes.R26)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Box(modifier = Modifier.size(104.dp), contentAlignment = Alignment.Center) {
            ProgressRing(
                progress = progress.percent / 100f,
                size = 99.dp,
                strokeWidth = 11.dp,
                color = AsosColors.Brand500,
                trackColor = AsosColors.Brand50,
                contentDescription = stringResource(R.string.learn_progress_ring, progress.percent),
            ) {
                Text(
                    text = "${progress.percent}%",
                    style = AsosTextStyles.SerifNumber.copy(fontSize = 28.sp, lineHeight = 28.sp),
                    color = AsosColors.Navy,
                )
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.learn_progress_title),
                    style = AsosTextStyles.CardTitle.copy(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold),
                    color = AsosColors.Navy,
                )
                Text(
                    text = stringResource(R.string.learn_progress_articles, progress.articlesRead, progress.articlesTotal),
                    style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.Medium),
                    color = AsosColors.Text2,
                )
            }
            if (progress.goal > 0) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = stringResource(R.string.learn_progress_goal, progress.done.coerceAtMost(progress.goal), progress.goal),
                        style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.Bold),
                        color = AsosColors.Navy,
                    )
                    GoalBars(goal = progress.goal, done = progress.done)
                }
            }
        }
    }
}

@Composable
private fun GoalBars(goal: Int, done: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        repeat(goal.coerceAtMost(MAX_BARS)) { index ->
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(7.dp)
                    .background(if (index < done) AsosColors.Brand500 else AsosColors.Track, AsosShapes.Pill),
            )
        }
    }
}

private const val MAX_BARS = 10

@Composable
internal fun SignInPromptCard(
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(AsosShapes.R26, elevation = AsosElevation.raised)
            .background(AsosColors.Surface, AsosShapes.R26)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.learn_signin_title),
            style = AsosTextStyles.CardTitle.copy(fontSize = 16.sp, fontWeight = FontWeight.ExtraBold),
            color = AsosColors.Navy,
        )
        Text(
            text = stringResource(R.string.learn_signin_message),
            style = AsosTextStyles.BodySmall,
            color = AsosColors.Text2,
        )
        AsosButton(
            text = stringResource(R.string.learn_signin_action),
            onClick = onSignIn,
            height = 46.dp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun OverallProgressCardPreview() {
    AsosTheme {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OverallProgressCard(OverallProgressUi(percent = 38, articlesRead = 59, articlesTotal = 155, goal = 3, done = 2))
            SignInPromptCard(onSignIn = {})
        }
    }
}
