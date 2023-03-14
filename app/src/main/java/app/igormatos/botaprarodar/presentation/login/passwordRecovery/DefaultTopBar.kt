package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import app.igormatos.botaprarodar.R

@Composable
fun DefaultTopBar(backAction: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = backAction) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back),
                    tint = colorResource(id = R.color.dark_gray)
                )
            }

            Text(
                stringResource(id = R.string.forgot_my_password),
                color = colorResource(id = R.color.dark_gray),
                style = MaterialTheme.typography.body1
            )
        }
    }
}