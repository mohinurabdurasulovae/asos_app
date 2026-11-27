package uz.asos.huquqiyyordam

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp

internal enum class AsosIcon(
    pathData: String,
    val strokeWidth: Float = 2f,
    fillData: String? = null,
) {
    Back("M15 6L9 12l6 6"),
    Sparkles("M12 3l1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8z M19 15l.8 2.2L22 18l-2.2.8L19 21l-.8-2.2L16 18l2.2-.8z", 1.8f),
    Microphone("M12 3a3 3 0 0 1 3 3v5a3 3 0 0 1-6 0V6a3 3 0 0 1 3-3z M5 11a7 7 0 0 0 14 0M12 18v3"),
    Send("M12 19V5M5 12l7-7 7 7", 2.2f),
    Phone("M5 3h3l2 5-2.5 1.5a11 11 0 0 0 5 5L14 12l5 2v3a2 2 0 0 1-2 2A15 15 0 0 1 3 5a2 2 0 0 1 2-2z", 1.9f),
    Verified("M8.5 12l2.5 2.5 4.5-5", fillData = "M12 2l2.4 1.8 3-.2.9 2.9 2.5 1.7-1 2.8 1 2.8-2.5 1.7-.9 2.9-3-.2L12 22l-2.4-1.8-3 .2-.9-2.9-2.5-1.7 1-2.8-1-2.8 2.5-1.7.9-2.9 3 .2z"),
    Star("", fillData = "M12 3l2.7 5.6 6.1.9-4.4 4.3 1 6.1L12 17l-5.4 2.9 1-6.1L3.2 9.5l6.1-.9z"),
    Chat("M21 12a8 8 0 0 1-11.6 7.1L4 20l1-4.6A8 8 0 1 1 21 12z"),
    Plane("M22 2L15 22l-4-9-9-4z M22 2L11 13"),
    Clip("M21 11.5l-9.2 9.2a5 5 0 0 1-7.1-7.1l9.9-9.9a3.5 3.5 0 0 1 4.9 4.9l-9.9 9.9a2 2 0 0 1-2.8-2.8l9.2-9.2"),
    File("M6 3h8l4 4v14H6z M14 3v5h4 M9 12h6M9 16h6"),
    Calendar("M5 5h14a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V7a2 2 0 0 1 2-2z M3 10h18M8 3v4M16 3v4", 1.9f);

    val outline: Path? = pathData.takeIf(String::isNotEmpty)?.let(::parsePath)
    val fill: Path? = fillData?.let(::parsePath)
}

private fun parsePath(data: String): Path = PathParser().parsePathString(data).toPath()

@Composable
internal fun LegalIcon(
    icon: AsosIcon,
    color: Color,
    size: Dp,
    label: String? = null,
) {
    val semantics = if (label == null) Modifier else Modifier.semantics {
        contentDescription = label
        role = Role.Image
    }
    Canvas(Modifier.size(size).then(semantics)) {
        val factor = this.size.width / 24f
        scale(factor, factor, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            icon.fill?.let { drawPath(it, color) }
            icon.outline?.let {
                drawPath(
                    it,
                    color = if (icon == AsosIcon.Verified) Color.White else color,
                    style = Stroke(icon.strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round),
                )
            }
        }
    }
}
