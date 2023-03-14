package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource

@Composable
fun RecoveryPasswordErrorContent(
    state: State<RecoveryPasswordState>,
    scaffoldState: ScaffoldState,
) {
    val message = stringResource(id = (state.value as RecoveryPasswordState.Error).message)
    LaunchedEffect(key1 = true) {
        scaffoldState.snackbarHostState.showSnackbar(message)
    }
}