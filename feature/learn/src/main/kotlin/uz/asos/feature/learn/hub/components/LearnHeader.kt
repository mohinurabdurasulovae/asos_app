package uz.asos.feature.learn.hub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.learn.R
import uz.asos.feature.learn.ui.FlameFilled

@Composable
internal fun LearnHeader(
    streakDays: Int?,
    pointsLabel: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.learn_title),
            style = AsosTextStyles.ScreenTitle,
            color = AsosColors.Navy,
            modifier = Modifier
                .weight(1f)
                .semantics { heading() },
        )
        if (streakDays != null) {
            StatPill(
                icon = FlameFilled,
                iconTint = AsosColors.StreakFlame,
                iconSize = 17.dp,
                text = streakDays.toString(),
                textColor = AsosColors.WarningOnLight,
                borderColor = AsosColors.StreakBorder,
                description = stringResource(R.string.learn_streak_label, streakDays),
            )
        }
        if (pointsLabel != null) {
            StatPill(
                icon = AsosIcons.Star,
                iconTint = AsosColors.Brand500,
                iconSize = 16.dp,
                text = pointsLabel,
                textColor = AsosColors.Brand600,
                borderColor = AsosColors.Brand100,
                description = stringResource(R.string.learn_points_label, pointsLabel),
            )
        }
    }
}

@Composable
private fun StatPill(
    icon: ImageVector,
    iconTint: Color,
    iconSize: Dp,
    text: String,
    textColor: Color,
    borderColor: Color,
    description: String,
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .background(AsosColors.Surface, AsosShapes.Pill)
            .border(1.dp, borderColor, AsosShapes.Pill)
            .padding(horizontal = 12.dp)
            .clearAndSetSemantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(iconSize))
        Text(
            text = text,
            style = AsosTextStyles.Chip.copy(fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold),
            color = textColor,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun LearnHeaderPreview() {
    AsosTheme {
        LearnHeader(streakDays = 7, pointsLabel = "1 240", modifier = Modifier.padding(20.dp))
    }
}
