/*
 * AsosTag.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Reusable component. Small coloured tag (Ta’lim, Mehnat, Uy-joy, Sud, "4 daqiqa" ...).
 */
package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.AsosTone
import uz.asos.core.designsystem.theme.AsosTones

@Composable
fun AsosTag(
    text: String,
    modifier: Modifier = Modifier,
    tone: AsosTone = AsosTones.Neutral,
) {
    Text(
        text = text,
        style = AsosTextStyles.Tag,
        color = tone.content,
        maxLines = 1,
        modifier = modifier
            .background(tone.container, AsosShapes.Pill)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Preview
@Composable
private fun AsosTagPreview() {
    AsosTheme {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AsosTag("Ta’lim", tone = AsosTones.Teal)
            AsosTag("4 daqiqa")
        }
    }
}
