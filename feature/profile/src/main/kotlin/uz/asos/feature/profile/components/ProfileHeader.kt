package uz.asos.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.model.user.User
import uz.asos.feature.profile.R

@Composable
internal fun ProfileHeader(
    user: User,
    pointsLabel: String,
    quizzesDone: Int,
    casesSolved: Int,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.Navy)
            .statusBarsPadding()
            .padding(horizontal = AsosSpacing.xl)
            .padding(top = AsosSpacing.m, bottom = AsosSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.profile_title),
                style = AsosTextStyles.SectionTitle,
                color = AsosColors.Surface,
                modifier = Modifier.semantics { heading() },
            )
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = AsosIcons.Sliders,
                    contentDescription = stringResource(R.string.profile_settings),
                    tint = AsosColors.TextOnNavy,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(AsosShapes.Circle)
                    .background(AsosColors.Brand600),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = user.initials,
                    style = AsosTextStyles.SectionTitle.copy(fontSize = 22.sp),
                    color = AsosColors.Surface,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(AsosSpacing.xxs)) {
                Text(
                    text = user.name,
                    style = AsosTextStyles.CardTitle,
                    color = AsosColors.Surface,
                )
                Text(
                    text = listOfNotNull(
                        user.username?.let { "@$it" },
                        user.city,
                    ).joinToString(" · "),
                    style = AsosTextStyles.Meta,
                    color = AsosColors.TextOnNavy,
                )
                Surface(shape = AsosShapes.Pill, color = AsosColors.BrandOnNavy.copy(alpha = 0.18f)) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = AsosSpacing.sm,
                            vertical = AsosSpacing.xxs,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
                    ) {
                        Icon(
                            imageVector = AsosIcons.Star,
                            contentDescription = null,
                            tint = AsosColors.BrandOnNavySoft,
                            modifier = Modifier.size(13.dp),
                        )
                        Text(
                            text = stringResource(
                                R.string.profile_level,
                                user.level.number,
                                user.level.title,
                            ),
                            style = AsosTextStyles.Tag,
                            color = AsosColors.BrandOnNavySoft,
                        )
                    }
                }
            }
        }

        StatsRow(
            pointsLabel = pointsLabel,
            quizzesDone = quizzesDone,
            casesSolved = casesSolved,
        )
    }
}
