package uz.asos.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.feature.home.R
import uz.asos.feature.home.TopicTileUi

private const val COLUMNS = 3

@Composable
internal fun TopicsSection(
    topics: List<TopicTileUi>,
    onTopicClick: (String) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HomeSection(
        title = stringResource(R.string.home_topics_title),
        actionLabel = stringResource(R.string.home_see_all),
        onAction = onSeeAllClick,
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            topics.chunked(COLUMNS).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { topic ->
                        TopicTile(
                            topic = topic,
                            onClick = { onTopicClick(topic.slug) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                    repeat(COLUMNS - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}

@Composable
private fun TopicTile(
    topic: TopicTileUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tone = AsosTones.fromName(topic.tone)
    AsosCard(modifier = modifier, shape = AsosShapes.R20, onClick = onClick) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(tone.container, AsosShapes.R12),
                contentAlignment = Alignment.Center,
            ) {
                Icon(topicIcon(topic.icon), contentDescription = null, tint = tone.content, modifier = Modifier.size(21.dp))
            }
            Text(
                text = topic.title,
                style = AsosTextStyles.RowTitleRegular.copy(fontSize = 13.5.sp, lineHeight = 17.sp),
            )
            Text(text = topic.countLabel, style = AsosTextStyles.Meta.copy(fontSize = 12.sp))
        }
    }
}

internal fun topicIcon(name: String?): ImageVector = when (name) {
    "shield" -> AsosIcons.ShieldCheck
    "vote" -> AsosIcons.Ballot
    "heart" -> AsosIcons.Briefcase
    "clipboard" -> AsosIcons.ClipboardCheck
    "family" -> AsosIcons.Family
    "scale" -> AsosIcons.Scales
    "cap" -> AsosIcons.GraduationCap
    "tools" -> AsosIcons.BriefcaseSimple
    "bird" -> AsosIcons.Sparkles
    "bank" -> AsosIcons.Government
    else -> AsosIcons.Book
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun TopicsSectionPreview() {
    AsosTheme {
        TopicsSection(
            topics = listOf(
                TopicTileUi("shaxsiy", "Shaxsiy huquqlar", "11 modda", "shield", "brand"),
                TopicTileUi("siyosiy", "Siyosiy huquqlar", "5 modda", "vote", "purple"),
                TopicTileUi("ijtimoiy", "Ijtimoiy huquqlar", "13 modda", "heart", "teal"),
                TopicTileUi("burchlar", "Burchlar", "6 modda", "clipboard", "orange"),
                TopicTileUi("oila", "Oila va yoshlar", "5 modda", "family", "pink"),
                TopicTileUi("sud", "Sud hokimiyati", "11 modda", "scale", "navy"),
            ),
            onTopicClick = {},
            onSeeAllClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
