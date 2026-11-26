package uz.asos.huquqiyyordam

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

internal object AsosColors {
    val Background = Color(0xFFF5F8FC)
    val Navy = Color(0xFF172B4D)
    val Blue = Color(0xFF2478DE)
    val ActionBlue = Color(0xFF1C64C0)
    val Border = Color(0xFFE3EAF4)
    val ButtonBorder = Color(0xFFDCE3EE)
    val Secondary = Color(0xFF5E6A80)
    val LawyerSecondary = Color(0xFF3A4A66)
    val OnNavy = Color(0xFFC9D6EA)
    val OnNavyMuted = Color(0xFFA9B8CF)
    val Green = Color(0xFF146B45)
    val GreenDot = Color(0xFF1E9E63)
    val GreenLight = Color(0xFFEAF7F0)
    val Gold = Color(0xFFF2A51A)
    val Amber = Color(0xFF8A4B00)
    val AmberLight = Color(0xFFFDF1E1)
    val BlueLight = Color(0xFFDCE9FB)
}

private val Jakarta = FontFamily(
    Font(R.font.jakarta_400, FontWeight.Normal),
    Font(R.font.jakarta_600, FontWeight.SemiBold),
    Font(R.font.jakarta_700, FontWeight.Bold),
    Font(R.font.jakarta_800, FontWeight.ExtraBold),
)

private val SourceSerif = FontFamily(
    Font(R.font.source_serif_400, FontWeight.Normal),
    Font(R.font.source_serif_700, FontWeight.Bold),
)

internal fun asosTextStyle(
    size: Double,
    line: Double,
    weight: Int,
    color: Color = AsosColors.Navy,
    serif: Boolean = false,
    spacing: Double = 0.0,
): TextStyle = TextStyle(
    color = color,
    fontFamily = if (serif) SourceSerif else Jakarta,
    fontWeight = FontWeight(weight),
    fontSize = size.sp,
    lineHeight = line.sp,
    letterSpacing = spacing.sp,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None),
)
