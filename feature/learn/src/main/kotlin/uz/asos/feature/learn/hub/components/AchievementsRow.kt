/*
 * AchievementsRow.kt
 * Module: :feature:learn   ·   Work package: WP-07
 *
 * "Yutuqlar" header with "Barchasi" and a horizontal row of 104 dp achievement cards.
 * Earned: white card, coloured icon. Locked: grey card, lock icon, "7 / 10".
 */
package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.SectionHeader
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.hub.AchievementUi
import uz.asos.feature.learn.ui.achievementIcon
import uz.asos.feature.learn.ui.achievementTone

@Composable
internal fun AchievementsRow(
    achievements: List<AchievementUi>,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
    gutter: Dp = 20.dp,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        SectionHeader(
            title = stringResource(R.string.learn_achievements_title),
            actionLabel = stringResource(R.string.learn_achievements_all),
            onAction = onSeeAll,
            modifier = Modifier.padding(horizontal = gutter),
        )
        LazyRow(
            contentPadding = PaddingValues(start = gutter, end = gutter, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(achievements, key = { it.slug }) { achievement ->
                AchievementCard(achievement)
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: AchievementUi) {
    val locked = !achievement.earned
    val lockedLabel = stringResource(R.string.learn_achievement_locked)
    Column(
        modifier = Modifier
            .width(104.dp)
            .then(
                if (locked) {
                    Modifier.background(AsosColors.SurfaceMuted, AsosShapes.R20)
                } else {
                    Modifier
                        .background(AsosColors.Surface, AsosShapes.R20)
                        .border(1.dp, AsosColors.BorderSoft, AsosShapes.R20)
                },
            )
            .padding(horizontal = 10.dp, vertical = 14.dp)
            .semantics(mergeDescendants = true) {
                if (locked) contentDescription = "${achievement.title}. $lockedLabel"
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val tone = achievementTone(achievement.tone, achievement.iconHint)
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(if (locked) AsosColors.LockedCircle else tone.container, AsosShapes.Circle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (locked) AsosIcons.Lock else achievementIcon(achievement.iconHint),
                contentDescription = null,
                tint = if (locked) AsosColors.Text2 else tone.content,
                modifier = Modifier.size(if (locked) 22.dp else 24.dp),
            )
        }
        Text(
            text = achievement.title,
            style = AsosTextStyles.Meta.copy(fontWeight = FontWeight.ExtraBold, lineHeight = 15.6.sp),
            color = if (locked) AsosColors.Text3 else AsosColors.Navy,
            textAlign = TextAlign.Center,
        )
        if (achievement.progressLabel != null) {
            Text(
                text = achievement.progressLabel,
                style = AsosTextStyles.Overline.copy(fontWeight = FontWeight.Bold),
                color = AsosColors.Text2,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun AchievementsRowPreview() {
    AsosTheme {
        AchievementsRow(
            achievements = listOf(
                AchievementUi("birinchi-qadam", "Birinchi qadam", "book articles_read", "brand", true, null),
                AchievementUi("seriya-7", "7 kunlik seriya", "flame streak_days", null, true, null),
                AchievementUi("burchlar", "Burchlar bilimdoni", "trophy quizzes_done", "teal", true, null),
                AchievementUi("kazus-ustasi", "Kazus ustasi", "scales cases_solved", null, false, "7 / 10"),
            ),
            onSeeAll = {},
        )
    }
}
