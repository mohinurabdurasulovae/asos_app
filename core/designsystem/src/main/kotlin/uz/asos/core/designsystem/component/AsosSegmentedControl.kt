/*
 * AsosSegmentedControl.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Reusable component. Grey track with equal-width segments; the selected one is a white
 * pill with a soft shadow. Used on Konstitutsiya (Mavzular · Tuzilish · Saqlangan) and
 * O‘rganish (Boshlang‘ich · O‘rta · Murakkab).
 */
package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow

@Composable
fun AsosSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AsosColors.SegmentTrack, AsosShapes.R15)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEachIndexed { index, label ->
            val selected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .then(
                        if (selected) {
                            Modifier
                                .softShadow(AsosShapes.R12, elevation = AsosElevation.card / 3)
                                .background(AsosColors.Surface, AsosShapes.R12)
                        } else {
                            Modifier
                        },
                    )
                    .clip(AsosShapes.R12)
                    .semantics { this.selected = selected }
                    .clickable(role = Role.Tab) { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = AsosTextStyles.Segment.copy(
                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Bold,
                    ),
                    color = if (selected) AsosColors.Navy else AsosColors.Text2,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AsosSegmentedControlPreview() {
    AsosTheme {
        AsosSegmentedControl(
            options = listOf("Mavzular", "Tuzilish", "Saqlangan"),
            selectedIndex = 1,
            onSelect = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
