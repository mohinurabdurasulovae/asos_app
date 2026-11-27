package uz.asos.core.ui.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Yuklab bo‘lmadi",
    retryLabel: String = "Qayta urinish",
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(AsosColors.ErrorBg, AsosShapes.R20),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AsosIcons.Close, contentDescription = null, tint = AsosColors.Error, modifier = Modifier.size(28.dp))
        }
        Text(text = title, style = AsosTextStyles.CardTitle, textAlign = TextAlign.Center)
        Text(
            text = message,
            style = AsosTextStyles.Body,
            color = AsosColors.Text2,
            textAlign = TextAlign.Center,
        )
        AsosButton(text = retryLabel, onClick = onRetry, modifier = Modifier.padding(top = 6.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun ErrorStatePreview() {
    AsosTheme {
        ErrorState(message = "Internet aloqasi yo‘q. Ulanishni tekshirib, qayta urinib ko‘ring.", onRetry = {})
    }
}
