package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow

enum class AsosCardStyle { Elevated, Outlined, Flat }

@Composable
fun AsosCard(
    modifier: Modifier = Modifier,
    shape: Shape = AsosShapes.R20,
    style: AsosCardStyle = AsosCardStyle.Elevated,
    containerColor: Color = AsosColors.Surface,
    borderColor: Color = AsosColors.BorderSoft,
    elevation: Dp = AsosElevation.card,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .then(
                when (style) {
                    AsosCardStyle.Elevated -> Modifier.softShadow(shape, elevation)
                    AsosCardStyle.Outlined -> Modifier.border(1.dp, borderColor, shape)
                    AsosCardStyle.Flat -> Modifier
                },
            )
            .background(containerColor, shape)
            .clip(shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(role = Role.Button, onClickLabel = onClickLabel, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        content = content,
    )
}

@Preview
@Composable
private fun AsosCardPreview() {
    AsosTheme {
        AsosCard(modifier = Modifier.padding(16.dp)) {
            Text("Oddiy karta", style = AsosTextStyles.RowTitle, modifier = Modifier.padding(16.dp))
        }
    }
}
