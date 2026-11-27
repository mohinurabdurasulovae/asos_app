package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosElevation
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow

@Composable
fun AsosBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .softShadow(RectangleShape, AsosElevation.bar)
            .background(AsosColors.Surface),
    ) {
        HorizontalDivider(thickness = 1.dp, color = AsosColors.BorderSoft)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(start = 6.dp, end = 6.dp, top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            content = content,
        )
    }
}

@Composable
fun RowScope.AsosBottomBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) AsosColors.Brand600 else AsosColors.Text2
    Column(
        modifier = modifier
            .weight(1f)
            .height(56.dp)
            .selectable(selected = selected, role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .width(52.dp)
                .height(32.dp)
                .background(if (selected) AsosColors.Brand50 else AsosColors.Surface, AsosShapes.R12),
            contentAlignment = Alignment.Center,
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        }
        Text(text = label, style = AsosTextStyles.TabLabel, color = color, maxLines = 1, softWrap = false)
    }
}

@Preview
@Composable
private fun AsosBottomBarPreview() {
    AsosTheme {
        AsosBottomBar {
            AsosBottomBarItem(false, {}, AsosIcons.Home, "Bosh sahifa")
            AsosBottomBarItem(true, {}, AsosIcons.TabConstitution, "Konstitutsiya")
            AsosBottomBarItem(false, {}, AsosIcons.TabLearn, "O‘rganish")
            AsosBottomBarItem(false, {}, AsosIcons.TabCommunity, "Hamjamiyat")
            AsosBottomBarItem(false, {}, AsosIcons.Person, "Profil")
        }
    }
}
