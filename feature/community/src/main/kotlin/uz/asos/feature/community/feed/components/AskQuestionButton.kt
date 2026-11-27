/*
 * AskQuestionButton.kt
 * Module: :feature:community   ·   Work package: WP-09
 * Screen: Hamjamiyat (design artboard: Hamjamiyat.dc.html)
 *
 * Two entry points to the same sheet: the composer row under the topic chips, and the
 * floating "Savol berish" button.
 */
package uz.asos.feature.community.feed.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.feature.community.R

/** The row that looks like an input but opens the sheet. */
@Composable
internal fun AskQuestionComposer(
    initials: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R16,
        color = AsosColors.Surface,
        border = BorderStroke(1.dp, AsosColors.Border),
    ) {
        Row(
            modifier = Modifier.padding(AsosSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.sm),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(AsosShapes.Circle)
                    .background(AsosColors.Brand50),
                contentAlignment = Alignment.Center,
            ) {
                if (initials.isNullOrBlank()) {
                    Icon(
                        imageVector = AsosIcons.Person,
                        contentDescription = null,
                        tint = AsosColors.Brand600,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    Text(
                        text = initials,
                        style = AsosTextStyles.Tag,
                        color = AsosColors.Brand600,
                    )
                }
            }

            Text(
                text = stringResource(R.string.community_ask_placeholder),
                style = AsosTextStyles.BodySmall,
                color = AsosColors.Text2,
                modifier = Modifier.weight(1f),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AsosSpacing.xxs),
            ) {
                Icon(
                    imageVector = AsosIcons.EyeOff,
                    contentDescription = null,
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(15.dp),
                )
                Text(
                    text = stringResource(R.string.community_anonymous),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.Text2,
                )
            }
        }
    }
}

/** Floating "Savol berish". */
@Composable
internal fun AskQuestionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = AsosShapes.Pill,
        color = AsosColors.Brand600,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = AsosSpacing.xl, vertical = AsosSpacing.m),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
        ) {
            Icon(
                imageVector = AsosIcons.Edit,
                contentDescription = null,
                tint = AsosColors.Surface,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = stringResource(R.string.community_ask_button),
                style = AsosTextStyles.Button,
                color = AsosColors.Surface,
            )
        }
    }
}
