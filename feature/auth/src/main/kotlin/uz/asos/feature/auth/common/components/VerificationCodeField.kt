package uz.asos.feature.auth.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R
import uz.asos.feature.auth.common.OtpState

private val DigitStyle = AsosTextStyles.ScreenTitle.copy(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.ExtraBold)

@Composable
internal fun VerificationCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    length: Int = OtpState.CODE_LENGTH,
) {
    val focusRequester = remember { FocusRequester() }
    val label = stringResource(R.string.auth_code_label)
    val isError = errorMessage != null

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = AuthLabelStyle)
        BasicTextField(
            value = code,
            onValueChange = { onCodeChange(it.filter(Char::isDigit).take(length)) },
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .semantics { contentDescription = label },
            decorationBox = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(length) { index ->
                        CodeCell(
                            digit = code.getOrNull(index),
                            isActive = enabled && index == code.length.coerceAtMost(length - 1),
                            isError = isError,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            },
        )
        if (errorMessage != null) {
            Text(text = errorMessage, style = AuthErrorStyle)
        }
    }
}

@Composable
private fun CodeCell(
    digit: Char?,
    isActive: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val borderColor = when {
        isError -> AsosColors.Error
        isActive -> AsosColors.Brand500
        else -> AsosColors.Border
    }
    Box(
        modifier = modifier
            .height(AuthFieldHeight)
            .background(AsosColors.Surface, AsosShapes.R14)
            .border(1.5.dp, borderColor, AsosShapes.R14),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = digit?.toString().orEmpty(), style = DigitStyle)
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationCodeFieldPreview() {
    AsosTheme {
        Column(Modifier.padding(AuthGutter), verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
            VerificationCodeField(code = "123", onCodeChange = {})
            VerificationCodeField(code = "", onCodeChange = {}, errorMessage = "Kod noto‘g‘ri. Yana 3 ta urinish qoldi.")
        }
    }
}
