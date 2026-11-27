package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R
import uz.asos.feature.legalhelp.chat.ChatFileUi

@Composable
internal fun FileAttachmentMessage(
    file: ChatFileUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AsosShapes.R14)
            .background(AsosColors.Background)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.legalhelp_chat_open_file),
                onClick = onClick,
            )
            .padding(AsosSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(AsosColors.Brand50, AsosShapes.R12),
            contentAlignment = Alignment.Center,
        ) {
            Icon(AsosIcons.DocumentLines, contentDescription = null, tint = AsosColors.Brand600, modifier = Modifier.size(20.dp))
        }
        Column(Modifier.padding(start = AsosSpacing.m)) {
            Text(text = file.name, style = AsosTextStyles.RowTitle, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = file.meta, style = AsosTextStyles.Meta)
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
private fun FileAttachmentMessagePreview() {
    AsosTheme {
        FileAttachmentMessage(
            file = ChatFileUi(name = "Ogohlantirish_xati.docx", meta = "24 KB · Word", url = null),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
