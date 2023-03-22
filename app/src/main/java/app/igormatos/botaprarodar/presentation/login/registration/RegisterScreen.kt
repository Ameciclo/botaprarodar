package app.igormatos.botaprarodar.presentation.login.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.DefaultTopBar
import app.igormatos.botaprarodar.presentation.login.signin.composables.InputField
import app.igormatos.botaprarodar.presentation.login.signin.composables.VisibilityButton
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.get

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = get(RegisterViewModel::class.java),
    onEvent: (RegisterEvent) -> Unit = {},
) {
    val state = viewModel.state.collectAsState(RegisterState.Empty)
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    onEvent(RegisterEvent.Loading(false))

    when (state.value) {
        is RegisterState.Loading -> onEvent(RegisterEvent.Loading(true))
        is RegisterState.Error -> RegisterErrorContent(state, scaffoldState)
        is RegisterState.Success, RegisterState.Empty -> Unit
    }

    ScreenContent(
        state = state.value.data,
        scaffoldState = scaffoldState,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onShowPasswordChanged = viewModel::onShowPasswordChanged,
        onSubmitClick = {
            viewModel.register {
                onEvent(RegisterEvent.RegisterSuccessful)
            }
        },
        backAction = { onEvent(RegisterEvent.Finish) }
    )

}

@Composable
private fun ScreenContent(
    state: RegisterData,
    scaffoldState: ScaffoldState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onShowPasswordChanged: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    backAction: () -> Unit,
) {

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopBar(R.string.registration_toolbar_title, backAction)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InputField(
                state = state.email,
                modifier = Modifier.fillMaxWidth().testTag("email"),
                label = { Text(text = stringResource(id = R.string.prompt_email)) },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                errorMessage = state.emailError?.let { stringResource(id = it) },
                onValueChange = onEmailChange
            )

            InputField(
                state = state.password,
                modifier = Modifier.fillMaxWidth().testTag("password"),
                label = { Text(text = stringResource(id = R.string.prompt_password_registration)) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    VisibilityButton(state.showPassword) {
                        onShowPasswordChanged(it)
                    }
                },
                errorMessage = null,
                onValueChange = onPasswordChange,
            )

            InputField(
                state = state.confirmPassword,
                modifier = Modifier.fillMaxWidth().testTag("confirmPassword"),
                label = { Text(text = stringResource(id = R.string.confirm_password)) },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    VisibilityButton(state.showPassword) {
                        onShowPasswordChanged(it)
                    }
                },
                errorMessage = null,//state.passwordError?.let { stringResource(id = it) },
                onValueChange = onConfirmPasswordChange,
            )

            Button(
                onClick = onSubmitClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    contentColor = Color.White
                ),
                enabled = state.isButtonEnable()
            ) {
                Text(text = stringResource(id = R.string.btn_register).uppercase())
            }
        }
    }

}

@Composable
private fun RegisterErrorContent(
    state: State<RegisterState>,
    scaffoldState: ScaffoldState,
) {
    val message = stringResource(id = (state.value as RegisterState.Error).message)
    LaunchedEffect(key1 = true) {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}