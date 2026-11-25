/*
 * Elevation.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Soft card shadows: 0 1 2 navy@5% + 0 6–10 18–28 navy@5–7%. Compose has one shadow per
 * layer, so this uses a soft navy-tinted elevation that reads the same on device.
 */
package uz.asos.core.designsystem.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object AsosElevation {
    val card: Dp = 6.dp
    val raised: Dp = 10.dp
    val bar: Dp = 12.dp
}

/** White cards: "0 1px 2px navy@5%, 0 6px 18px navy@5%". */
fun Modifier.softShadow(shape: Shape, elevation: Dp = AsosElevation.card): Modifier =
    shadow(
        elevation = elevation,
        shape = shape,
        clip = false,
        ambientColor = AsosColors.Navy.copy(alpha = 0.06f),
        spotColor = AsosColors.Navy.copy(alpha = 0.10f),
    )
