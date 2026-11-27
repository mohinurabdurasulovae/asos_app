package uz.asos.feature.auth.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uz.asos.core.designsystem.theme.AsosColors
import uz.asos.core.designsystem.theme.AsosTheme
import uz.asos.core.ui.preview.AsosPreviews
import uz.asos.feature.auth.R
import uz.asos.feature.auth.common.OtpState
import uz.asos.feature.auth.common.OtpStep
import uz.asos.feature.auth.common.components.AuthHeader
import uz.asos.feature.auth.register.components.RegisterSheet

@Composable
internal fun RegisterRoute(
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnRegistered by rememberUpdatedState(onRegistered)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.Registered -> currentOnRegistered()
            }
        }
    }

    BackHandler(enabled = uiState.otp.step == OtpStep.CODE) {
        viewModel.onAction(RegisterAction.OnChangeEmailClick)
    }

    RegisterScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick, RegisterAction.OnSignInClick -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun RegisterScreen(
    uiState: RegisterUiState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Surface)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(bottom = 24.dp),
    ) {
        AuthHeader(
            titleStart = stringResource(R.string.auth_register_tagline_start),
            titleAccent = stringResource(R.string.auth_register_tagline_accent),
            caption = stringResource(R.string.auth_register_caption),
            onBack = { onAction(RegisterAction.OnBackClick) },
        )
        RegisterSheet(uiState = uiState, onAction = onAction)
    }
}

@AsosPreviews
@Composable
private fun RegisterScreenPreview() {
    AsosTheme {
        RegisterScreen(
            uiState = RegisterUiState(name = "Aziza Karimova", otp = OtpState(email = "aziza@example.uz")),
            onAction = {},
        )
    }
}

@AsosPreviews
@Composable
private fun RegisterScreenCodePreview() {
    AsosTheme {
        RegisterScreen(
            uiState = RegisterUiState(
                name = "Aziza Karimova",
                acceptedTerms = true,
                otp = OtpState(step = OtpStep.CODE, email = "aziza@example.uz", resendSeconds = 42),
            ),
            onAction = {},
        )
    }
}
