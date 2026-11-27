package uz.asos.core.ui.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.component.AsosButtonVariant
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun EmptyState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    icon: ImageVector = AsosIcons.Search,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(AsosColors.Brand50, AsosShapes.R18),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(24.dp))
        }
        Text(text = title, style = AsosTextStyles.RowTitle, textAlign = TextAlign.Center)
        if (message != null) {
            Text(text = message, style = AsosTextStyles.BodySmall, color = AsosColors.Text2, textAlign = TextAlign.Center)
        }
        if (actionLabel != null && onAction != null) {
            AsosButton(
                text = actionLabel,
                onClick = onAction,
                variant = AsosButtonVariant.Secondary,
                height = 44.dp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun EmptyStatePreview() {
    AsosTheme {
        EmptyState(
            title = "Hech narsa topilmadi",
            message = "Boshqa so‘z yoki modda raqamini kiriting.",
        )
    }
}
