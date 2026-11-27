package uz.asos.feature.legalhelp.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosSpacing
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.legalhelp.R

private val ControlSize = 48.dp

@Composable
internal fun ChatComposer(
    value: String,
    onValueChange: (String) -> Unit,
    canSend: Boolean,
    onSend: () -> Unit,
    onAttach: () -> Unit,
    onVoice: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AsosSpacing.l, vertical = AsosSpacing.s),
        horizontalArrangement = Arrangement.spacedBy(AsosSpacing.s),
        verticalAlignment = Alignment.Bottom,
    ) {
        ComposerButton(
            icon = AsosIcons.Attach,
            contentDescription = stringResource(R.string.legalhelp_chat_attach),
            container = AsosColors.SurfaceMuted,
            tint = AsosColors.Navy,
            onClick = onAttach,
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = ControlSize)
                .clip(AsosShapes.R16)
                .background(AsosColors.Background)
                .border(1.dp, AsosColors.Border, AsosShapes.R16)
                .padding(start = AsosSpacing.l),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = AsosTextStyles.Body,
                cursorBrush = SolidColor(AsosColors.Brand600),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }),
                maxLines = 4,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = AsosSpacing.m),
                decorationBox = { inner ->
                    Box {
                        if (value.isEmpty()) {
                            Text(
                                text = stringResource(R.string.legalhelp_chat_placeholder),
                                style = AsosTextStyles.Body,
                                color = AsosColors.SoftGray,
                            )
                        }
                        inner()
                    }
                },
            )
            Box(
                modifier = Modifier
                    .size(AsosSpacing.minTouch)
                    .clip(AsosShapes.Circle)
                    .clickable(role = Role.Button, onClick = onVoice),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    AsosIcons.Microphone,
                    contentDescription = stringResource(R.string.legalhelp_chat_voice),
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
        ComposerButton(
            icon = AsosIcons.Send,
            contentDescription = stringResource(R.string.legalhelp_chat_send),
            container = if (canSend) AsosColors.Brand600 else AsosColors.Brand300,
            tint = AsosColors.Surface,
            onClick = onSend,
            enabled = canSend,
        )
    }
}

@Composable
private fun ComposerButton(
    icon: ImageVector,
    contentDescription: String,
    container: Color,
    tint: Color,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .size(ControlSize)
            .clip(AsosShapes.R16)
            .background(container)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun ChatComposerPreview() {
    AsosTheme {
        ChatComposer(value = "", onValueChange = {}, canSend = false, onSend = {}, onAttach = {}, onVoice = {})
    }
}
