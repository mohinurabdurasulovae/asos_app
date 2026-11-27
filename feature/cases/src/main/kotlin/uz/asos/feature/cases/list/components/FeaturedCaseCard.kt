/*
 * FeaturedCaseCard.kt
 * Module: :feature:cases   ·   Work package: WP-08
 * Screen: Kazuslar (design artboard: Kazuslar.dc.html)
 *
 * Dark "HAFTANING KAZUSI" card: the case number watermarked in serif behind the text,
 * progress bar and "Davom etish" ("Ko'rish" once solved).
 */
package uz.asos.feature.cases.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.cases.R
import uz.asos.feature.cases.list.FeaturedCaseUi
import uz.asos.feature.cases.ui.CaseDifficultyBars
import uz.asos.feature.cases.ui.CasesPalette

@Composable
internal fun FeaturedCaseCard(
    featured: FeaturedCaseUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R22,
        color = AsosColors.Navy,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = featured.number.toString(),
                style = AsosTextStyles.SerifNumber.copy(fontSize = 112.sp, lineHeight = 112.sp),
                color = AsosColors.Surface.copy(alpha = 0.06f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = AsosSpacing.mm),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AsosSpacing.xl),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                ) {
                    Surface(
                        shape = AsosShapes.R10,
                        color = CasesPalette.NavyCardChip,
                    ) {
                        Text(
                            text = stringResource(R.string.cases_featured_badge),
                            style = AsosTextStyles.Overline,
                            color = AsosColors.BrandOnNavySoft,
                            modifier = Modifier.padding(horizontal = AsosSpacing.sm, vertical = AsosSpacing.xxs),
                        )
                    }
                    Text(
                        text = featured.numberLabel,
                        style = AsosTextStyles.Meta,
                        color = AsosColors.TextOnNavy,
                    )
                }

                Spacer(modifier = Modifier.height(AsosSpacing.mm))

                Text(
                    text = featured.title,
                    style = AsosTextStyles.SectionTitle.copy(fontSize = 21.sp, lineHeight = 27.sp),
                    color = AsosColors.Surface,
                )

                Spacer(modifier = Modifier.height(AsosSpacing.s))

                Text(
                    text = featured.shortDescription,
                    style = AsosTextStyles.BodySmall,
                    color = AsosColors.TextOnNavy,
                )

                Spacer(modifier = Modifier.height(AsosSpacing.mm))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
                ) {
                    Text(
                        text = featured.topic.label,
                        style = AsosTextStyles.Meta,
                        color = AsosColors.TextOnNavy,
                    )
                    CaseDifficultyBars(difficulty = featured.difficulty, onDark = true)
                    Text(
                        text = featured.durationLabel,
                        style = AsosTextStyles.Meta,
                        color = AsosColors.TextOnNavy,
                    )
                }

                Spacer(modifier = Modifier.height(AsosSpacing.l))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AsosSpacing.mm),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(AsosSpacing.xs)
                            .clip(AsosShapes.Pill)
                            .background(AsosColors.Surface.copy(alpha = 0.16f)),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(featured.progressFraction.coerceIn(0f, 1f))
                                .clip(AsosShapes.Pill)
                                .background(AsosColors.BrandOnNavy),
                        )
                    }

                    Surface(
                        onClick = onClick,
                        shape = AsosShapes.R12,
                        color = AsosColors.Surface,
                    ) {
                        Text(
                            text = stringResource(
                                if (featured.isDone) R.string.cases_featured_view else R.string.cases_featured_continue,
                            ),
                            style = AsosTextStyles.Chip,
                            color = AsosColors.Navy,
                            modifier = Modifier.padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.sm),
                        )
                    }
                }
            }
        }
    }
}
