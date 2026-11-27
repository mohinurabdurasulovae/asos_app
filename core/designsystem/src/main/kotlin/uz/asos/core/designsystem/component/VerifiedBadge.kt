package uz.asos.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    contentDescription: String? = "Tasdiqlangan",
) {
    Icon(
        imageVector = AsosIcons.Verified,
        contentDescription = contentDescription,
        tint = Color.Unspecified,
        modifier = modifier.size(size),
    )
}

@Preview
@Composable
private fun VerifiedBadgePreview() {
    AsosTheme { VerifiedBadge(Modifier.padding(16.dp)) }
}
