/*
 * WeeklyActivityChart.kt
 * Module: :feature:profile   ·   Work package: WP-11
 * Screen: Profil (design artboard: Profil.dc.html)
 *
 * "Bu hafta": seven columns (Du–Ya) scaled to the busiest day, then the bar showing how far
 * the next level is.
 */
package uz.asos.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.component.ProgressBar
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.user.WeekActivity
import uz.asos.feature.profile.LevelProgressUi
import uz.asos.feature.profile.R

@Composable
internal fun WeeklyActivityChart(
    week: WeekActivity,
    levelProgress: LevelProgressUi?,
    modifier: Modifier = Modifier,
) {
    AsosCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AsosSpacing.ll),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.profile_week_title),
                    style = AsosTextStyles.CardTitle,
                )
                Text(
                    text = stringResource(
                        R.string.profile_week_summary,
                        week.totalMinutes,
                        week.activeDays,
                    ),
                    style = AsosTextStyles.Meta,
                    color = AsosColors.Text2,
                )
            }

            val busiest = week.days.maxOfOrNull { it.minutes }?.coerceAtLeast(1) ?: 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                week.days.forEach { day ->
                    val fraction = day.minutes.toFloat() / busiest.toFloat()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs),
                        modifier = Modifier.clearAndSetSemantics {
                            contentDescription = "${day.label}: ${day.minutes} daqiqa"
                        },
                    ) {
                        Box(
                            modifier = Modifier
                                .width(10.dp)
                                .height(BAR_MAX_HEIGHT),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(
                                        (BAR_MAX_HEIGHT.value * fraction).dp
                                            .coerceAtLeast(BAR_MIN_HEIGHT),
                                    )
                                    .background(
                                        color = when {
                                            day.isFuture -> AsosColors.Track
                                            day.isToday -> AsosColors.Brand600
                                            day.minutes > 0 -> AsosColors.Brand300
                                            else -> AsosColors.Track
                                        },
                                        shape = AsosShapes.Pill,
                                    ),
                            )
                        }
                        Text(
                            text = day.label,
                            style = AsosTextStyles.Tag,
                            color = if (day.isToday) AsosColors.Brand600 else AsosColors.Text2,
                        )
                    }
                }
            }

            if (levelProgress != null) {
                Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.xs)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(
                                R.string.profile_next_level,
                                levelProgress.nextLevel,
                            ),
                            style = AsosTextStyles.Tag,
                            color = AsosColors.Text2,
                        )
                        Text(
                            text = stringResource(
                                R.string.profile_level_progress,
                                levelProgress.pointsLabel,
                                levelProgress.targetLabel,
                            ),
                            style = AsosTextStyles.Tag,
                            color = AsosColors.Text2,
                        )
                    }
                    ProgressBar(progress = levelProgress.fraction)
                }
            } else {
                Text(
                    text = stringResource(R.string.profile_max_level),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.SuccessOnLight,
                )
            }
        }
    }
}

private val BAR_MAX_HEIGHT = 56.dp
private val BAR_MIN_HEIGHT = 4.dp
