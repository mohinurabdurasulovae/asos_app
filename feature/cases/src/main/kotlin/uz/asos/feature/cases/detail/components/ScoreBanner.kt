package uz.asos.feature.cases.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import uz.asos.feature.cases.R

@Composable
internal fun ScoreBanner(
    isCorrect: Boolean,
    points: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R16,
        color = if (isCorrect) AsosColors.SuccessBg else AsosColors.ErrorBg,
        border = BorderStroke(
            width = 1.dp,
            color = if (isCorrect) {
                AsosColors.Success.copy(alpha = 0.25f)
            } else {
                AsosColors.Error.copy(alpha = 0.25f)
            },
        ),
    ) {
        Row(
            modifier = Modifier.padding(AsosSpacing.mm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AsosSpacing.m),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(AsosShapes.R12)
                    .background(if (isCorrect) AsosColors.Success else AsosColors.Error),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (isCorrect) AsosIcons.Trophy else AsosIcons.Close,
                    contentDescription = null,
                    tint = AsosColors.Surface,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column {
                Text(
                    text = if (isCorrect) {
                        stringResource(R.string.cases_result_correct_title, points)
                    } else {
                        stringResource(R.string.cases_result_wrong_title)
                    },
                    style = AsosTextStyles.CardTitle,
                    color = if (isCorrect) AsosColors.SuccessOnLight else AsosColors.ErrorOnLight,
                )
                Text(
                    text = stringResource(
                        if (isCorrect) {
                            R.string.cases_result_correct_subtitle
                        } else {
                            R.string.cases_result_wrong_subtitle
                        },
                    ),
                    style = AsosTextStyles.Subtitle,
                    color = AsosColors.Text2,
                )
            }
        }
    }
}
