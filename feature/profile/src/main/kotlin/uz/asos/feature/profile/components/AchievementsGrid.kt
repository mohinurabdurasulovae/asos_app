/*
 * AchievementsGrid.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * "Yutuqlar 3 / 12": a row of badges, the locked ones grey with their progress underneath.
 */
package uz.asos.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.SectionHeader
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTones
import uz.asos.core.model.user.Achievement
import uz.asos.feature.profile.R

@Composable
internal fun AchievementsGrid(
    achievements: List<Achievement>,
    earned: Int,
    total: Int,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.m),
    ) {
        SectionHeader(
            title = stringResource(R.string.profile_achievements),
            actionLabel = stringResource(R.string.profile_achievements_count, earned, total),
            onAction = onSeeAll,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            achievements.forEach { achievement ->
                AchievementBadge(achievement)
            }
        }
    }
}

@Composable
private fun AchievementBadge(achievement: Achievement) {
    val tone = AsosTones.fromName(achievement.tone)
    Column(
        modifier = Modifier.width(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(AsosShapes.Circle)
                .background(if (achievement.earned) tone.container else AsosColors.LockedCircle),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (achievement.earned) achievement.icon() else AsosIcons.Lock,
                contentDescription = null,
                tint = if (achievement.earned) tone.content else AsosColors.SoftGray,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = achievement.title,
            style = AsosTextStyles.Tag,
            color = if (achievement.earned) AsosColors.Navy else AsosColors.Text2,
            textAlign = TextAlign.Center,
            maxLines = 2,
        )
        if (!achievement.earned && achievement.threshold > 0) {
            Text(
                text = "${achievement.progress} / ${achievement.threshold}",
                style = AsosTextStyles.Meta,
                color = AsosColors.SoftGray,
            )
        }
    }
}

/** Icon for a badge, picked from the API's icon name, then its metric. */
private fun Achievement.icon(): ImageVector = when (icon) {
    "flame" -> AsosIcons.Flame
    "star" -> AsosIcons.Star
    "trophy" -> AsosIcons.Trophy
    "scales" -> AsosIcons.Scales
    "book" -> AsosIcons.Book
    "graduation" -> AsosIcons.GraduationCap
    else -> AsosIcons.TrophyLarge
}
