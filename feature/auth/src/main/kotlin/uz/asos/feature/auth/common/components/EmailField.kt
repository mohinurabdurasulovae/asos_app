package uz.asos.feature.auth.common.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R

@Composable
internal fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    errorMessage: String? = null,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    AuthTextField(
        value = email,
        onValueChange = onEmailChange,
        label = stringResource(R.string.auth_email_label),
        placeholder = stringResource(R.string.auth_email_placeholder),
        modifier = modifier,
        enabled = enabled,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        focusRequester = focusRequester,
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EmailFieldPreview() {
    AsosTheme {
        EmailField(
            email = "aziza@example.uz",
            onEmailChange = {},
            onDone = {},
            modifier = Modifier.padding(AuthGutter),
        )
    }
}
