package uz.asos.feature.auth.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R
import uz.asos.feature.auth.common.OtpState
import uz.asos.feature.auth.common.OtpStep
import uz.asos.feature.auth.common.components.AuthFieldHeight
import uz.asos.feature.auth.common.components.AuthGutter
import uz.asos.feature.auth.common.components.AuthSheetGap
import uz.asos.feature.auth.common.components.AuthTextField
import uz.asos.feature.auth.common.components.CodeStep
import uz.asos.feature.auth.common.components.LinkPrompt
import uz.asos.feature.auth.common.components.EmailField
import uz.asos.feature.auth.common.components.SheetTitle
import uz.asos.feature.auth.register.RegisterAction
import uz.asos.feature.auth.register.RegisterUiState

@Composable
internal fun RegisterSheet(
    uiState: RegisterUiState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentModifier = modifier
        .fillMaxWidth()
        .padding(start = AuthGutter, end = AuthGutter, top = 28.dp)
    when (uiState.otp.step) {
        OtpStep.EMAIL -> DetailsStep(uiState = uiState, onAction = onAction, modifier = contentModifier)
        OtpStep.CODE -> CodeStep(
            otp = uiState.otp,
            onCodeChange = { onAction(RegisterAction.OnCodeChange(it)) },
            onVerifyClick = { onAction(RegisterAction.OnVerifyClick) },
            onResendClick = { onAction(RegisterAction.OnResendClick) },
            onChangeEmailClick = { onAction(RegisterAction.OnChangeEmailClick) },
            modifier = contentModifier,
        )
    }
}

@Composable
private fun DetailsStep(
    uiState: RegisterUiState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val otp = uiState.otp
    val emailFocus = remember { FocusRequester() }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
        SheetTitle(
            title = stringResource(R.string.auth_register_title),
            message = stringResource(R.string.auth_register_message),
        )
        AuthTextField(
            value = uiState.name,
            onValueChange = { onAction(RegisterAction.OnNameChange(it)) },
            label = stringResource(R.string.auth_name_label),
            placeholder = stringResource(R.string.auth_name_placeholder),
            enabled = !otp.isSubmitting,
            errorMessage = stringResource(R.string.auth_name_error).takeIf { uiState.showNameError },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() }),
        )
        EmailField(
            email = otp.email,
            onEmailChange = { onAction(RegisterAction.OnEmailChange(it)) },
            onDone = { onAction(RegisterAction.OnRequestCodeClick) },
            enabled = !otp.isSubmitting,
            errorMessage = otp.errorMessage,
            focusRequester = emailFocus,
        )
        TermsAgreement(
            checked = uiState.acceptedTerms,
            onToggle = { onAction(RegisterAction.OnTermsToggle) },
            enabled = !otp.isSubmitting,
        )
        AsosButton(
            text = stringResource(if (otp.isSubmitting) R.string.auth_sending else R.string.auth_request_code),
            onClick = { onAction(RegisterAction.OnRequestCodeClick) },
            enabled = uiState.canRequestCode,
            height = AuthFieldHeight,
            trailingIcon = AsosIcons.ArrowRight.takeUnless { otp.isSubmitting },
            modifier = Modifier.fillMaxWidth(),
        )
        LinkPrompt(
            question = stringResource(
                if (otp.needsSignIn) R.string.auth_account_exists else R.string.auth_have_account,
            ),
            link = stringResource(R.string.auth_sign_in),
            onClick = { onAction(RegisterAction.OnSignInClick) },
            enabled = !otp.isSubmitting,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun RegisterSheetPreview() {
    AsosTheme {
        RegisterSheet(
            uiState = RegisterUiState(
                name = "Aziza Karimova",
                acceptedTerms = true,
                otp = OtpState(email = "aziza@example.uz"),
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun RegisterSheetErrorPreview() {
    AsosTheme {
        RegisterSheet(uiState = RegisterUiState(name = "A", showNameError = true), onAction = {})
    }
}
