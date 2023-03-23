package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.signin.*
import org.koin.java.KoinJavaComponent.get

@Preview(showSystemUi = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = get(LoginViewModel::class.java),
    onEvent: (SignInEvent) -> Unit = {},
) {
    val state = viewModel.state.collectAsState(LoginState.Success(SignInData()))
    val scaffoldState = rememberScaffoldState()

    onEvent(SignInEvent.Loading(false))

//    LaunchedEffect(key1 = true) {
    when (state.value) {
        is LoginState.Loading -> onEvent(SignInEvent.Loading(true))
        is LoginState.Error -> LoginErrorContent(state, scaffoldState)
        is LoginState.RetryVerifyEmail -> RetryEmailContent(scaffoldState, viewModel::sendEmailVerification)
        is LoginState.EmailSent -> onEvent(SignInEvent.EmailWarning)
        is LoginState.Success -> Unit
    }
//    }

    Scaffold(scaffoldState = scaffoldState) {
        ScreenContent(
            state.value.data,
            viewModel::onEmailChanged,
            viewModel::onPasswordChanged,
            viewModel::onShowPasswordChanged,
            { viewModel.login { onEvent(SignInEvent.SignInSuccessful(it)) } },
            onEvent
        )
    }
}

@Composable
private fun ScreenContent(
    state: SignInData,
    onEmailChange: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onShowPasswordChanged: (Boolean) -> Unit,
    onSignInSuccessful: () -> Unit,
    onEvent: (SignInEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .width(162.dp)
                .height(172.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.app_name)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(34.dp)
        ) {
            InputFiled(
                state = state.email,
                label = { Text(text = stringResource(id = R.string.prompt_email)) },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                errorMessage = state.emailError?.let { stringResource(id = it) },
                onValueChange = onEmailChange
            )

            InputFiled(
                state = state.password,
                label = { Text(text = stringResource(id = R.string.prompt_password)) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    VisibilityButton(state.showPassword) {
                        onShowPasswordChanged(it)
                    }
                },
                errorMessage = state.passwordError?.let { stringResource(id = it) },
                onValueChange = onPasswordChanged,
            )
        }

        SignInButton(state.isSignInButtonEnable()) {
            onSignInSuccessful()
        }

        ForgotPasswordButton {
            onEvent(SignInEvent.Navigate(SignInRoute.FORGOT_PASSWORD))
        }

        RegisterUserButton {
            onEvent(SignInEvent.Navigate(SignInRoute.REGISTER_USER))
        }
    }
}