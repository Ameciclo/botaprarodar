package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import app.igormatos.botaprarodar.presentation.login.signin.LoginState

@Composable
fun LoginErrorContent(
    state: State<LoginState>,
    scaffoldState: ScaffoldState,
) {
    val message = stringResource(id = (state.value as LoginState.Error).message)
    LaunchedEffect(key1 = true) {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}