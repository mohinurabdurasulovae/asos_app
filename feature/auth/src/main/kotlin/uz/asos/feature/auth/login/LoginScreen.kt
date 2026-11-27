package uz.asos.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import uz.asos.feature.auth.login.components.WelcomeSheet

@Composable
internal fun LoginRoute(
    onSignedIn: () -> Unit,
    onOpenRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val currentOnSignedIn by rememberUpdatedState(onSignedIn)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.SignedIn -> currentOnSignedIn()
                LoginEvent.GoogleUnavailable ->
                    snackbarHostState.showSnackbar(context.getString(R.string.auth_google_unavailable))
            }
        }
    }

    LoginScreen(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                LoginAction.OnSignUpClick -> onOpenRegister()
                else -> viewModel.onAction(action)
            }
        },
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}

@Composable
internal fun LoginScreen(
    uiState: LoginUiState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AsosColors.Surface),
    ) {
        if (!uiState.isCheckingSession) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(bottom = 24.dp),
            ) {
                AuthHeader(
                    titleStart = stringResource(R.string.auth_tagline_start),
                    titleAccent = stringResource(R.string.auth_tagline_accent),
                    caption = stringResource(R.string.auth_header_caption),
                    showSteps = true,
                )
                WelcomeSheet(otp = uiState.otp, onAction = onAction)
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@AsosPreviews
@Composable
private fun LoginScreenPreview() {
    AsosTheme {
        LoginScreen(uiState = LoginUiState(isCheckingSession = false, otp = OtpState(email = "aziza@example.uz")), onAction = {})
    }
}

@AsosPreviews
@Composable
private fun LoginScreenCodePreview() {
    AsosTheme {
        LoginScreen(
            uiState = LoginUiState(
                isCheckingSession = false,
                otp = OtpState(step = OtpStep.CODE, email = "aziza@example.uz", resendSeconds = 42),
            ),
            onAction = {},
        )
    }
}
