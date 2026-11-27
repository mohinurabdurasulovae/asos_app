package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

enum class AsosButtonVariant { Primary, Secondary, Dark }

@Composable
fun AsosButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: AsosButtonVariant = AsosButtonVariant.Primary,
    enabled: Boolean = true,
    height: Dp = 52.dp,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    val shape = AsosShapes.R16
    val (container, content, border) = when {
        !enabled -> Triple(AsosColors.Track, AsosColors.Text2, null)
        variant == AsosButtonVariant.Primary -> Triple(AsosColors.Brand600, AsosColors.Surface, null)
        variant == AsosButtonVariant.Dark -> Triple(AsosColors.Navy, AsosColors.Surface, null)
        else -> Triple(AsosColors.Surface, AsosColors.Navy, AsosColors.Border)
    }
    Row(
        modifier = modifier
            .height(height)
            .background(container, shape)
            .then(if (border != null) Modifier.border(1.5.dp, border, shape) else Modifier)
            .clip(shape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, tint = content, modifier = Modifier.size(19.dp))
        }
        Text(text = text, style = AsosTextStyles.Button, color = content, maxLines = 1)
        if (trailingIcon != null) {
            Icon(trailingIcon, contentDescription = null, tint = content, modifier = Modifier.size(18.dp))
        }
    }
}

@Preview
@Composable
private fun AsosButtonPreview() {
    AsosTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AsosButton("Bilimni sinash", onClick = {}, leadingIcon = AsosIcons.GraduationCap)
            AsosButton("Ikkilamchi", onClick = {}, variant = AsosButtonVariant.Secondary)
            AsosButton("Nofaol", onClick = {}, enabled = false)
        }
    }
}
