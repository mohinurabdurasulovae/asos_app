/*
 * ConstitutionHeader.kt
 * Module: :feature:constitution   ·   Work package: WP-05
 * Screen: Konstitutsiya (design artboard: Konstitutsiya.dc.html)
 *
 * Title "Konstitutsiya", edition subtitle and the saved-items button (46 dp, outlined).
 */
package uz.asos.feature.constitution.browse.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.constitution.R

@Composable
internal fun ConstitutionHeader(
    subtitle: String,
    onSavedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.constitution_title),
                style = AsosTextStyles.ScreenTitle,
                modifier = Modifier.semantics { heading() },
            )
            if (subtitle.isNotEmpty()) {
                Text(text = subtitle, style = AsosTextStyles.Subtitle)
            }
        }
        Box(
            modifier = Modifier
                .size(46.dp)
                .border(1.dp, AsosColors.Border, AsosShapes.R16)
                .background(AsosColors.Surface, AsosShapes.R16)
                .clip(AsosShapes.R16)
                .clickable(role = Role.Button, onClick = onSavedClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = AsosIcons.Bookmark,
                contentDescription = stringResource(R.string.constitution_saved_button),
                tint = AsosColors.Navy,
                modifier = Modifier.size(21.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F8FC)
@Composable
private fun ConstitutionHeaderPreview() {
    AsosTheme {
        ConstitutionHeader(
            subtitle = "2023-yil tahriri · 6 bo‘lim · 27 bob · 155 modda",
            onSavedClick = {},
            modifier = Modifier.padding(20.dp),
        )
    }
}
