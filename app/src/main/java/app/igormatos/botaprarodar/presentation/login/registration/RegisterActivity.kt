package app.igormatos.botaprarodar.presentation.login.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.*
import app.igormatos.botaprarodar.presentation.login.signin.composables.InputField
import app.igormatos.botaprarodar.presentation.login.signin.composables.VisibilityButton
import org.koin.java.KoinJavaComponent

class RegisterActivity : BaseAuthActivity() {

    override val snackBarview: View
        get() = View(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RegisterScreen(
                onEvent = {
                    when(it) {
                        is RegisterEvent.Loading -> if (it.show) loadingDialog.show() else loadingDialog.hide()
                        RegisterEvent.Finish -> finish()
                        RegisterEvent.RegisterSuccessful -> {
                            showSuccessConfirmDialog(
                                title = R.string.title_register_completed,
                                message = R.string.message_register_completed,
                                click = { finish() }
                            )
                        }
                    }
                }
            )
        }
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }
}
