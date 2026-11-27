package uz.asos.feature.auth.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.asos.core.designsystem.component.AsosButton
import uz.asos.core.designsystem.icon.AsosIcons
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosShapes
import uz.asos.core.designsystem.theme.AsosTextStyles
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.feature.auth.R
import uz.asos.feature.auth.common.OtpState
import uz.asos.feature.auth.common.OtpStep
import uz.asos.feature.auth.data.model.CodeRequest

@Composable
internal fun CodeStep(
    otp: OtpState,
    onCodeChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    onChangeEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AuthSheetGap)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SheetTitle(
                title = stringResource(R.string.auth_code_title),
                message = stringResource(R.string.auth_code_message, otp.maskedEmail),
            )
            LinkText(
                text = stringResource(R.string.auth_change_email),
                onClick = onChangeEmailClick,
                enabled = !otp.isSubmitting,
            )
        }

        SpamNotice()

        VerificationCodeField(
            code = otp.code,
            onCodeChange = onCodeChange,
            enabled = !otp.isSubmitting,
            errorMessage = otp.errorMessage,
        )
        otp.devCode?.let { devCode ->
            Text(text = stringResource(R.string.auth_dev_code, devCode), style = AsosTextStyles.Meta)
        }
        AsosButton(
            text = stringResource(if (otp.isSubmitting) R.string.auth_checking else R.string.auth_verify),
            onClick = onVerifyClick,
            enabled = otp.isCodeComplete && !otp.isSubmitting,
            height = AuthFieldHeight,
            modifier = Modifier.fillMaxWidth(),
        )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (otp.resendSeconds > 0) {
                Text(
                    text = stringResource(R.string.auth_resend_in, otp.resendSeconds / 60, otp.resendSeconds % 60),
                    style = AuthMessageStyle,
                    textAlign = TextAlign.Center,
                )
            } else {
                LinkText(text = stringResource(R.string.auth_resend), onClick = onResendClick, enabled = otp.canResend)
            }
        }
    }
}

@Composable
private fun SpamNotice(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = AsosShapes.R14,
        color = AsosColors.WarningBg,
        border = BorderStroke(1.dp, AsosColors.StreakBorder),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = AsosIcons.Bell,
                contentDescription = null,
                tint = AsosColors.WarningOnLight,
                modifier = Modifier
                    .padding(top = 1.dp)
                    .size(16.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.auth_spam_title),
                    style = AsosTextStyles.Tag,
                    color = AsosColors.WarningOnLight,
                )
                Text(
                    text = stringResource(R.string.auth_spam_message, CodeRequest.CODE_SENDER),
                    style = AsosTextStyles.Meta,
                    color = AsosColors.Text3,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun CodeStepPreview() {
    AsosTheme {
        CodeStep(
            otp = OtpState(
                step = OtpStep.CODE,
                email = "aziza@example.uz",
                code = "12",
                resendSeconds = 42,
            ),
            onCodeChange = {},
            onVerifyClick = {},
            onResendClick = {},
            onChangeEmailClick = {},
            modifier = Modifier.padding(AuthGutter),
        )
    }
}
