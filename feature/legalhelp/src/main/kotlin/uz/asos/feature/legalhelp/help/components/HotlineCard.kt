package uz.asos.feature.legalhelp.help.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.component.AsosCard
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.help.HotlineUi

@Composable
internal fun HotlineCard(
    hotline: HotlineUi,
    onCall: () -> Unit,
    onCallback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hours = if (hotline.everyDay) stringResource(R.string.legalhelp_hotline_every_day, hotline.hours) else hotline.hours
    val subtitle = listOf(hours, if (hotline.isFree) stringResource(R.string.legalhelp_hotline_free) else "")
        .filter { it.isNotBlank() }
        .joinToString(" · ")

    AsosCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AsosSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(AsosSpacing.l),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AsosColors.SuccessBg, AsosShapes.R14),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(AsosIcons.Phone, contentDescription = null, tint = AsosColors.Success, modifier = Modifier.size(20.dp))
                }
                Column(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = AsosSpacing.m),
                ) {
                    Text(text = stringResource(R.string.legalhelp_hotline_title), style = AsosTextStyles.RowTitle)
                    if (subtitle.isNotEmpty()) Text(text = subtitle, style = AsosTextStyles.Meta)
                }
                OpenStatus(isOpen = hotline.isOpenNow)
            }

            Text(
                text = hotline.displayNumber,
                style = AsosTextStyles.SerifNumber.copy(fontSize = AsosTextStyles.SerifNumber.fontSize * 1.1f),
            )
            hotline.note?.let { note -> Text(text = note, style = AsosTextStyles.Meta) }

            Row(horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s)) {
                AsosButton(
                    text = stringResource(R.string.legalhelp_hotline_call),
                    onClick = onCall,
                    modifier = Modifier.weight(1f),
                    height = 48.dp,
                )
                AsosButton(
                    text = stringResource(R.string.legalhelp_hotline_callback),
                    onClick = onCallback,
                    modifier = Modifier.weight(1f),
                    variant = AsosButtonVariant.Secondary,
                    height = 48.dp,
                )
            }
        }
    }
}

@Composable
private fun OpenStatus(isOpen: Boolean) {
    val color = if (isOpen) AsosColors.SuccessOnLight else AsosColors.ErrorOnLight
    val dot = if (isOpen) AsosColors.Success else AsosColors.Error
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(7.dp)
                .background(dot, AsosShapes.Circle),
        )
        Text(
            text = stringResource(if (isOpen) R.string.legalhelp_hotline_open else R.string.legalhelp_hotline_closed),
            style = AsosTextStyles.Tag,
            color = color,
            modifier = Modifier.padding(start = AsosSpacing.xs),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun HotlineCardPreview() {
    AsosTheme {
        HotlineCard(
            hotline = HotlineUi(
                displayNumber = "+998 (71) 000-00-00",
                dialNumber = "+998710000000",
                hours = "09:00–21:00",
                everyDay = true,
                isFree = true,
                isOpenNow = true,
                note = null,
            ),
            onCall = {},
            onCallback = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
