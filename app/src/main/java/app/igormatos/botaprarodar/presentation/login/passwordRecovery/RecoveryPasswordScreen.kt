package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import app.igormatos.botaprarodar.presentation.login.signin.composables.InputField
import org.koin.java.KoinJavaComponent.get

@Composable
fun RecoveryPasswordScreen(
    viewModel: RecoveryPasswordViewModel = get(RecoveryPasswordViewModel::class.java),
    onEvent: (RecoveryPasswordEvent) -> Unit = {},
) {
    val state = viewModel.state.collectAsState(RecoveryPasswordState.Success(RecoveryPasswordData()))
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    onEvent(RecoveryPasswordEvent.Loading(false))

    when (state.value) {
        is RecoveryPasswordState.Loading -> onEvent(RecoveryPasswordEvent.Loading(true))
        is RecoveryPasswordState.Error -> RecoveryPasswordErrorContent(state, scaffoldState)
        is RecoveryPasswordState.Success -> Unit
    }

    ScreenContent(
        state = state.value.data,
        scaffoldState = scaffoldState,
        onEmailChange = viewModel::onEmailChanged,
        onRecoverClick = {
            viewModel.recoverPassword {
                onEvent(RecoveryPasswordEvent.RecoveryPasswordSuccessful)
            }
        },
        backAction = { onEvent(RecoveryPasswordEvent.Finish) }
    )
}

@Composable
private fun ScreenContent(
    state: RecoveryPasswordData,
    scaffoldState: ScaffoldState,
    onEmailChange: (String) -> Unit,
    onRecoverClick: () -> Unit,
    backAction: () -> Unit,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopBar(backAction)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.enter_email_to_recover_password),
                fontSize = 14.sp,
                color = ColorPallet.BlackAlpha39
            )

            InputField(
                state = state.email,
                label = { Text(text = stringResource(id = R.string.prompt_email)) },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
                errorMessage = state.emailError?.let { stringResource(id = it) },
                onValueChange = onEmailChange
            )

            Button(
                onClick = onRecoverClick,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    contentColor = Color.White
                ),
                enabled = state.isRecoverButtonEnable()
            ) {
                Text(text = stringResource(id = R.string.btn_send).uppercase())
            }
        }
    }
}