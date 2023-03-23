package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R

@Composable
fun ForgotPasswordButton(click :() -> Unit) {
    TextButton(
        onClick = click,
        modifier = Modifier.wrapContentSize(),
        contentPadding = PaddingValues(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.forgot_my_password),
            style = TextStyle(color = colorResource(R.color.green_teal))
        )
    }
}