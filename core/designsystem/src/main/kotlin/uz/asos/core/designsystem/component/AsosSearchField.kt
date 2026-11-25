/*
 * AsosSearchField.kt
 * Module: :core:designsystem   ·   Work package: WP-01
 *
 * Reusable component. Rounded search input with a clear button and an optional voice button.
 * Flat (bordered) on list screens, elevated (soft shadow) on Bosh sahifa.
 */
package uz.asos.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.designsystem.theme.softShadow

@Composable
fun AsosSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    accessibilityLabel: String = "Qidirish",
    elevated: Boolean = false,
    onVoiceClick: (() -> Unit)? = null,
) {
    val shape = if (elevated) AsosShapes.R18 else AsosShapes.R16
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(if (elevated) 54.dp else 52.dp)
            .then(
                if (elevated) {
                    Modifier.softShadow(shape)
                } else {
                    Modifier.border(1.dp, AsosColors.Border, shape)
                },
            )
            .background(AsosColors.Surface, shape)
            .padding(start = 16.dp, end = if (onVoiceClick != null) 6.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            imageVector = AsosIcons.Search,
            contentDescription = null,
            tint = AsosColors.Text2,
            modifier = Modifier.size(20.dp),
        )
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = AsosTextStyles.Body,
                    color = AsosColors.Text2,
                    maxLines = 1,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = AsosTextStyles.Body.copy(color = AsosColors.Navy),
                cursorBrush = SolidColor(AsosColors.Brand600),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = accessibilityLabel },
            )
        }
        if (value.isNotEmpty()) {
            IconButton(onClick = { onValueChange("") }, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = AsosIcons.Close,
                    contentDescription = "Tozalash",
                    tint = AsosColors.Text2,
                    modifier = Modifier.size(18.dp),
                )
            }
        } else if (onVoiceClick != null) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AsosColors.Brand50, AsosShapes.R12),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = onVoiceClick) {
                    Icon(
                        imageVector = AsosIcons.Microphone,
                        contentDescription = "Ovozli qidiruv",
                        tint = AsosColors.Brand600,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AsosSearchFieldPreview() {
    AsosTheme {
        AsosSearchField(
            value = "",
            onValueChange = {},
            placeholder = "Modda raqami yoki so‘z, masalan: ta’lim",
            modifier = Modifier.padding(16.dp),
        )
    }
}
