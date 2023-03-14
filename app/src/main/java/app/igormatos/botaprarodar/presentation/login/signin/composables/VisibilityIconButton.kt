package app.igormatos.botaprarodar.presentation.login.signin.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import app.igormatos.botaprarodar.R

@Composable
fun VisibilityButton(
    show: Boolean,
    onSVisibilityChanged: (Boolean) -> Unit
) {
    if (show) {
        IconButton(onClick = { onSVisibilityChanged(false) }) {
            Icon(
                imageVector = Icons.Filled.VisibilityOff,
                contentDescription = stringResource(id = R.string.hide_password)
            )
        }
    } else {
        IconButton(onClick = { onSVisibilityChanged(true) }) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = stringResource(id = R.string.show_password)
            )
        }
    }
}