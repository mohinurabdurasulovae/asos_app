package uz.asos.feature.auth.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R
import uz.asos.feature.auth.common.OtpState
import uz.asos.feature.auth.common.OtpStep
import uz.asos.feature.auth.common.components.AuthFieldHeight
import uz.asos.feature.auth.common.components.AuthGutter
import uz.asos.feature.auth.common.components.AuthSheetGap
import uz.asos.feature.auth.common.components.AuthSmallPrintStyle
import uz.asos.feature.auth.common.components.CodeStep
import uz.asos.feature.auth.common.components.EmailField
import uz.asos.feature.auth.common.components.LinkPrompt
import uz.asos.feature.auth.common.components.SheetTitle
import uz.asos.feature.auth.login.LoginAction

private val LinkSpan = SpanStyle(color = AsosColors.Brand600, fontWeight = FontWeight.Bold)

@Composable
internal fun WelcomeSheet(
    otp: OtpState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentModifier = modifier
        .fillMaxWidth()
        .padding(start = AuthGutter, end = AuthGutter, top = 28.dp)
    when (otp.step) {
        OtpStep.EMAIL -> EmailStep(otp = otp, onAction = onAction, modifier = contentModifier)
        OtpStep.CODE -> CodeStep(
            otp = otp,
            onCodeChange = { onAction(LoginAction.OnCodeChange(it)) },
            onVerifyClick = { onAction(LoginAction.OnVerifyClick) },
            onResendClick = { onAction(LoginAction.OnResendClick) },
            onChangeEmailClick = { onAction(LoginAction.OnChangeEmailClick) },
            modifier = contentModifier,
        )
    }
}

@Composable
private fun EmailStep(
    otp: OtpState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
        SheetTitle(
            title = stringResource(R.string.auth_welcome_title),
            message = stringResource(R.string.auth_welcome_message),
        )
        GoogleSignInButton(
            onClick = { onAction(LoginAction.OnGoogleClick) },
            enabled = !otp.isSubmitting,
        )
        OrDivider()
        EmailField(
            email = otp.email,
            onEmailChange = { onAction(LoginAction.OnEmailChange(it)) },
            onDone = { onAction(LoginAction.OnRequestCodeClick) },
            enabled = !otp.isSubmitting,
            errorMessage = otp.errorMessage,
        )
        AsosButton(
            text = stringResource(if (otp.isSubmitting) R.string.auth_sending else R.string.auth_request_code),
            onClick = { onAction(LoginAction.OnRequestCodeClick) },
            enabled = otp.isEmailValid && !otp.isSubmitting,
            height = AuthFieldHeight,
            trailingIcon = AsosIcons.ArrowRight.takeUnless { otp.isSubmitting },
            modifier = Modifier.fillMaxWidth(),
        )
        LinkPrompt(
            question = stringResource(R.string.auth_no_account),
            link = stringResource(R.string.auth_sign_up),
            onClick = { onAction(LoginAction.OnSignUpClick) },
            enabled = !otp.isSubmitting,
        )
        TermsNotice()
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DividerLine(Modifier.weight(1f))
        Text(text = stringResource(R.string.auth_or), style = AsosTextStyles.Subtitle)
        DividerLine(Modifier.weight(1f))
    }
}

@Composable
private fun DividerLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(1.dp)
            .background(AsosColors.Border),
    )
}

@Composable
private fun TermsNotice() {
    val prefix = stringResource(R.string.auth_terms_prefix)
    val terms = stringResource(R.string.auth_terms_link)
    val middle = stringResource(R.string.auth_terms_middle)
    val privacy = stringResource(R.string.auth_privacy_link)
    val suffix = stringResource(R.string.auth_terms_suffix)
    Text(
        text = buildAnnotatedString {
            append(prefix)
            withStyle(LinkSpan) { append(terms) }
            append(middle)
            withStyle(LinkSpan) { append(privacy) }
            append(suffix)
        },
        style = AuthSmallPrintStyle,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun WelcomeSheetEmailPreview() {
    AsosTheme {
        WelcomeSheet(otp = OtpState(email = "aziza@example.uz"), onAction = {})
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun WelcomeSheetCodePreview() {
    AsosTheme {
        WelcomeSheet(
            otp = OtpState(
                step = OtpStep.CODE,
                email = "aziza@example.uz",
                code = "12",
                resendSeconds = 42,
            ),
            onAction = {},
        )
    }
}
