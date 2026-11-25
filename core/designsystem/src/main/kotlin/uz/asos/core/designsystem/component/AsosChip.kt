/*
 * AsosChip.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Reusable component. Filter chip / pill (selected = Navy, unselected = white with border).
 * 38 dp tall; the touch area is padded to the 44 dp minimum.
 */
package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
fun AsosChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = AsosShapes.Pill
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(AsosColors.Navy, shape)
                } else {
                    Modifier
                        .background(AsosColors.Surface, shape)
                        .border(1.dp, AsosColors.Border, shape)
                },
            )
            .semantics { this.selected = selected }
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = AsosTextStyles.Chip,
            color = if (selected) AsosColors.Surface else AsosColors.Navy,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun AsosChipPreview() {
    AsosTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AsosChip("Barchasi", selected = true, onClick = {})
            AsosChip("Huquqlar", selected = false, onClick = {})
        }
    }
}
