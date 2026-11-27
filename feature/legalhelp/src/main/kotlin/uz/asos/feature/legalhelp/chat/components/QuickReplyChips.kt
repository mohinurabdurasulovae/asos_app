package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

@Composable
internal fun QuickReplyChips(
    replies: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = AsosSpacing.l),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
    ) {
        replies.forEach { reply ->
            Box(
                modifier = Modifier
                    .heightIn(min = AsosSpacing.minTouch)
                    .clip(AsosShapes.Pill)
                    .background(AsosColors.Brand25)
                    .border(1.dp, AsosColors.Brand200, AsosShapes.Pill)
                    .clickable(enabled = enabled, role = Role.Button, onClick = { onClick(reply) })
                    .padding(horizontal = AsosSpacing.mm),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = reply, style = AsosTextStyles.Tag.copy(fontSize = AsosTextStyles.Chip.fontSize), color = AsosColors.Brand600)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun QuickReplyChipsPreview() {
    AsosTheme {
        QuickReplyChips(
            replies = listOf("Rahmat!", "Keyingi qadam qanday?", "Qo‘ng‘iroq qilsam bo‘ladimi?"),
            onClick = {},
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}
