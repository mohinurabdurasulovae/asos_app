package uz.asos.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AsosShapes {
    val R7 = RoundedCornerShape(7.dp)
    val R10 = RoundedCornerShape(10.dp)
    val R11 = RoundedCornerShape(11.dp)
    val R12 = RoundedCornerShape(12.dp)
    val R14 = RoundedCornerShape(14.dp)
    val R15 = RoundedCornerShape(15.dp)
    val R16 = RoundedCornerShape(16.dp)
    val R18 = RoundedCornerShape(18.dp)
    val R20 = RoundedCornerShape(20.dp)
    val R22 = RoundedCornerShape(22.dp)
    val R24 = RoundedCornerShape(24.dp)
    val R26 = RoundedCornerShape(26.dp)
    val Pill = RoundedCornerShape(99.dp)
    val Circle = CircleShape
}

internal val AsosMaterialShapes = Shapes(
    extraSmall = AsosShapes.R10,
    small = AsosShapes.R12,
    medium = AsosShapes.R16,
    large = AsosShapes.R22,
    extraLarge = AsosShapes.R26,
)
