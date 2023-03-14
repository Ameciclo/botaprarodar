package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import app.igormatos.botaprarodar.R

@Composable
fun RetryEmailContent(scaffoldState: ScaffoldState, sendEmailAction: () -> Unit) {
    val message = stringResource(id = R.string.login_confirm_email_error)
    val action = stringResource(id = R.string.resend_email)

    LaunchedEffect(key1 = true) {
        val result: SnackbarResult = scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            actionLabel = action,
        )

        if (result == SnackbarResult.ActionPerformed) {
            sendEmailAction()
        }
    }
}