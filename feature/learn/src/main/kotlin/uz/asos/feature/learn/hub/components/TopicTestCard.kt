package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.ProgressBar
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow
import uz.asos.core.model.learning.QuizDifficulty
import uz.asos.feature.learn.R
import uz.asos.feature.learn.hub.QuizRowState
import uz.asos.feature.learn.hub.QuizRowUi
import uz.asos.feature.learn.ui.LearnTones
import uz.asos.feature.learn.ui.quizIcon

@Composable
internal fun TopicTestCard(
    quizzes: List<QuizRowUi>,
    onQuizClick: (key: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(AsosShapes.R22)
            .clip(AsosShapes.R22)
            .background(AsosColors.Surface),
    ) {
        quizzes.forEachIndexed { index, quiz ->
            TopicTestRow(quiz = quiz, onClick = { onQuizClick(quiz.key) })
            if (index < quizzes.lastIndex) HorizontalDivider(thickness = 1.dp, color = AsosColors.Divider)
        }
    }
}

@Composable
private fun TopicTestRow(
    quiz: QuizRowUi,
    onClick: () -> Unit,
) {
    val tone = LearnTones.of(quiz.tone, quiz.key)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(tone.container, AsosShapes.R14),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = quizIcon(quiz.iconHint),
                contentDescription = null,
                tint = tone.content,
                modifier = Modifier.size(21.dp),
            )
        }

        val titleStyle = AsosTextStyles.RowTitle.copy(fontWeight = FontWeight.ExtraBold)
        val metaStyle = AsosTextStyles.Meta
        when (quiz.state) {
            QuizRowState.IN_PROGRESS -> Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = quiz.title,
                        style = titleStyle,
                        color = AsosColors.Navy,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "${quiz.percent}%",
                        style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.ExtraBold),
                        color = AsosColors.Brand600,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
                ProgressBar(progress = quiz.percent / 100f, height = 6.dp)
                Text(text = quiz.meta, style = metaStyle, color = AsosColors.Text2)
            }

            QuizRowState.NEW -> {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = quiz.title, style = titleStyle, color = AsosColors.Navy)
                    Text(text = quiz.meta, style = metaStyle, color = AsosColors.Text2)
                }
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(AsosColors.Brand600, AsosShapes.R12)
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.learn_test_start),
                        style = AsosTextStyles.Subtitle.copy(fontWeight = FontWeight.ExtraBold),
                        color = AsosColors.Surface,
                    )
                }
            }

            QuizRowState.DONE -> {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = quiz.title, style = titleStyle, color = AsosColors.Navy)
                    Text(
                        text = quiz.meta,
                        style = metaStyle.copy(fontWeight = FontWeight.Bold),
                        color = AsosColors.SuccessOnLight,
                    )
                }
                Icon(
                    imageVector = AsosIcons.CheckCircleFilled,
                    contentDescription = stringResource(R.string.learn_test_done),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun TopicTestCardPreview() {
    AsosTheme {
        TopicTestCard(
            quizzes = listOf(
                QuizRowUi("shaxsiy-huquqlar", "Shaxsiy huquqlar", QuizDifficulty.MEDIUM, "shield", "brand", QuizRowState.IN_PROGRESS, "12 savol · 9/12 to‘g‘ri · Davom etish", 75),
                QuizRowUi("siyosiy-huquqlar", "Siyosiy huquqlar", QuizDifficulty.MEDIUM, "ballot", "purple", QuizRowState.NEW, "10 savol · ≈ 6 daqiqa", 0),
                QuizRowUi("burchlar", "Fuqarolarning burchlari", QuizDifficulty.MEDIUM, "clipboard", "orange", QuizRowState.DONE, "8/8 to‘g‘ri · Tugallangan", 100),
            ),
            onQuizClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
