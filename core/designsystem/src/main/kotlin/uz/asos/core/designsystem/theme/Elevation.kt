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

fun Modifier.softShadow(shape: Shape, elevation: Dp = AsosElevation.card): Modifier =
    shadow(
        elevation = elevation,
        shape = shape,
        clip = false,
        ambientColor = AsosColors.Navy.copy(alpha = 0.06f),
        spotColor = AsosColors.Navy.copy(alpha = 0.10f),
    )
