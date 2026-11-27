package uz.asos.app.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.app.R
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun ComingSoonScreen(
    @StringRes titleRes: Int,
    onBack: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(titleRes)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Background)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(AsosIcons.LogoMark, contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(64.dp))
            Text(text = title, style = AsosTextStyles.ScreenTitle, textAlign = TextAlign.Center)
            Text(
                text = stringResource(R.string.app_coming_soon_message, title),
                style = AsosTextStyles.Body,
                color = AsosColors.Text2,
                textAlign = TextAlign.Center,
            )
            if (onBack != null) {
                AsosButton(
                    text = stringResource(R.string.app_coming_soon_back),
                    onClick = onBack,
                    variant = AsosButtonVariant.Secondary,
                    leadingIcon = AsosIcons.ChevronLeft,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ComingSoonScreenPreview() {
    AsosTheme { ComingSoonScreen(titleRes = R.string.app_screen_cases, onBack = {}) }
}
