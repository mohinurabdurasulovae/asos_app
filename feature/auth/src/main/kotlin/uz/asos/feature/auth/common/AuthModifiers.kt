package uz.asos.feature.auth.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp

internal fun Modifier.overflowVertically(amount: Dp): Modifier = layout { measurable, constraints ->
    val extra = amount.roundToPx()
    val maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight + extra * 2 else Constraints.Infinity
    val placeable = measurable.measure(constraints.copy(maxHeight = maxHeight))
    layout(placeable.width, (placeable.height - extra * 2).coerceAtLeast(0)) {
        placeable.place(0, -extra)
    }
}
