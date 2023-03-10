package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity

class PasswordRecoveryActivity : BaseAuthActivity() {

    override val snackBarview: View
        get() = View(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecoveryPasswordScreen(
                onEvent = {
                    when (it) {
                        is RecoveryPasswordEvent.Loading -> showOrHideLoading(it)
                        RecoveryPasswordEvent.RecoveryPasswordSuccessful -> showSuccessDialog()
                        RecoveryPasswordEvent.Finish -> finish()
                    }
                }
            )
        }
    }

    private fun showSuccessDialog() {
        showSuccessConfirmDialog(
            title = R.string.title_recovery_link_send,
            message = R.string.message_password_recovery_link_send,
            click = { finish() }
        )
    }

    private fun showOrHideLoading(state: RecoveryPasswordEvent.Loading) =
        if (state.show) loadingDialog.show() else loadingDialog.hide()

    companion object {
        fun getStartIntent(context: Context) = Intent(context, PasswordRecoveryActivity::class.java)
    }
}