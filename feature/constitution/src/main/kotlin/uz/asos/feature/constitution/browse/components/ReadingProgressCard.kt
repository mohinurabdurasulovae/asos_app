package uz.asos.feature.constitution.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.component.ArticleNumberBadge
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R
import uz.asos.feature.constitution.browse.ContinueReadingUi

@Composable
internal fun ReadingProgressCard(
    continueReading: ContinueReadingUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsosCard(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R20,
        onClick = onClick,
        onClickLabel = stringResource(R.string.constitution_continue_action),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            ArticleNumberBadge(
                text = continueReading.numberLabel,
                size = 52.dp,
                cornerRadius = 15.dp,
                fontSize = 24.sp,
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = stringResource(R.string.constitution_continue_overline).uppercase(),
                    style = AsosTextStyles.Overline,
                    color = AsosColors.Brand600,
                )
                Text(
                    text = continueReading.title,
                    style = AsosTextStyles.RowTitle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(text = continueReading.meta, style = AsosTextStyles.Meta)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AsosColors.Brand50, AsosShapes.Circle),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = AsosIcons.Play,
                    contentDescription = null,
                    tint = AsosColors.Brand600,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun ReadingProgressCardPreview() {
    AsosTheme {
        ReadingProgressCard(
            continueReading = ContinueReadingUi("50", "50", "Ta’lim olish huquqi", "IX bob · 3 daqiqa qoldi"),
            onClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
