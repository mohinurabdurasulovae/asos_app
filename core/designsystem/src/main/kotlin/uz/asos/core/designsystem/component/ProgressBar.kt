/*
 * ProgressBar.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Reusable component. Linear progress (Brand500 on a light track, 7 dp, pill ends).
 */
package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTheme

/** @param progress 0f..1f */
@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 7.dp,
    color: Color = AsosColors.Brand500,
    trackColor: Color = AsosColors.Track,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(trackColor, AsosShapes.Pill),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(color, AsosShapes.Pill),
        )
    }
}

@Preview
@Composable
private fun ProgressBarPreview() {
    AsosTheme { ProgressBar(progress = 0.62f, modifier = Modifier.padding(16.dp)) }
}
