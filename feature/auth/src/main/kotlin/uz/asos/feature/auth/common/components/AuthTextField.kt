package uz.asos.feature.auth.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme

internal val AuthFieldHeight = 56.dp

private val FieldPadding = 14.dp
private val PrefixStyle = AsosTextStyles.RowTitleRegular.copy(fontSize = 15.sp)
private val InputStyle = AsosTextStyles.Body.copy(
    fontSize = 16.sp,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = 0.02.em,
    color = AsosColors.Navy,
)

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    prefix: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    var isFocused by remember { mutableStateOf(false) }
    val isError = errorMessage != null
    val borderColor = when {
        isError -> AsosColors.Error
        isFocused -> AsosColors.Brand500
        else -> AsosColors.Border
    }
    val ringColor = if (isError) AsosColors.ErrorBg else AsosColors.Brand50

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = AuthLabelStyle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AuthFieldHeight)
                .drawBehind { if (isFocused || isError) drawFocusRing(ringColor) }
                .background(AsosColors.Surface, AsosShapes.R16)
                .border(1.5.dp, borderColor, AsosShapes.R16)
                .clip(AsosShapes.R16),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (prefix != null) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(AsosColors.Background)
                        .padding(horizontal = FieldPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = prefix, style = PrefixStyle)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(AsosColors.Border),
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = FieldPadding),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = InputStyle.copy(fontWeight = FontWeight.Medium),
                        color = AsosColors.Text2,
                        maxLines = 1,
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = InputStyle,
                    cursorBrush = SolidColor(AsosColors.Brand600),
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused }
                        .semantics { contentDescription = label },
                )
            }
        }
        if (errorMessage != null) {
            Text(text = errorMessage, style = AuthErrorStyle)
        }
    }
}

private fun DrawScope.drawFocusRing(color: Color) {
    val spread = 4.dp.toPx()
    val radius = 16.dp.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(-spread, -spread),
        size = Size(size.width + spread * 2, size.height + spread * 2),
        cornerRadius = CornerRadius(radius + spread),
    )
}

@Preview(showBackground = true)
@Composable
private fun AuthTextFieldPreview() {
    AsosTheme {
        Column(Modifier.padding(AuthGutter), verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
            AuthTextField(value = "", onValueChange = {}, label = "Ism va familiya", placeholder = "Masalan: Aziza Karimova")
            AuthTextField(
                value = "A",
                onValueChange = {},
                label = "Ism va familiya",
                placeholder = "",
                errorMessage = "Ismingizni kiriting: kamida 2 ta harf.",
            )
        }
    }
}
