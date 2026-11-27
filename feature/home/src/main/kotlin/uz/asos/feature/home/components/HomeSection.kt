package uz.asos.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.SectionHeader
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

internal val SectionContentGap = 14.dp

private val ActionTouchOverflow = 9.dp

@Composable
internal fun HomeSection(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    headerPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SectionContentGap)) {
        SectionHeader(
            title = title,
            actionLabel = actionLabel,
            onAction = onAction,
            modifier = Modifier
                .padding(headerPadding)
                .then(if (actionLabel != null && onAction != null) Modifier.overflowVertically(ActionTouchOverflow) else Modifier),
        )
        content()
    }
}

private fun Modifier.overflowVertically(amount: Dp): Modifier = layout { measurable, constraints ->
    val extra = amount.roundToPx()
    val maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight + extra * 2 else Constraints.Infinity
    val placeable = measurable.measure(constraints.copy(maxHeight = maxHeight))
    layout(placeable.width, (placeable.height - extra * 2).coerceAtLeast(0)) {
        placeable.place(0, -extra)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC, widthDp = 390)
@Composable
private fun HomeSectionPreview() {
    AsosTheme {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(28.dp)) {
            HomeSection(title = "Mavzular", actionLabel = "Barchasi", onAction = {}) {
                Text("Kontent", style = AsosTextStyles.Body)
            }
            HomeSection(title = "Yordam kerakmi?") {
                Text("Kontent", style = AsosTextStyles.Body)
            }
        }
    }
}
