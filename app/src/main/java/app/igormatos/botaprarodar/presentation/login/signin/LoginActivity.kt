package app.igormatos.botaprarodar.presentation.login.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.PasswordRecoveryActivity
import app.igormatos.botaprarodar.presentation.login.registration.RegisterActivity
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityActivity
import app.igormatos.botaprarodar.presentation.login.signin.composables.*

@ExperimentalComposeUiApi
class LoginActivity : BaseAuthActivity() {

    override val snackBarview: View
        get() = View(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginScreen(
                onEvent = {
                    when(it) {
                        is SignInEvent.SignInSuccessful -> navigateToMainActivity(it.admin)
                        is SignInEvent.Loading -> showLoading(it.show)
                        is SignInEvent.Navigate -> navigate(it.route)
                        SignInEvent.EmailWarning -> showEmailWarning()
                    }
                }
            )
        }
    }

    private fun navigate(route: SignInRoute) {
        val intent = when (route) {
            SignInRoute.FORGOT_PASSWORD -> PasswordRecoveryActivity.getStartIntent(this)
            SignInRoute.REGISTER_USER -> RegisterActivity.getStartIntent(this)
        }

        startActivity(intent)
    }

    private fun showLoading(show: Boolean) {
        if (show) loadingDialog.show() else loadingDialog.hide()
    }

    private fun showEmailWarning() {
        showSuccessConfirmDialog(
            title = R.string.title_resend_email,
            message = R.string.message_resend_email
        )
    }

    private fun navigateToMainActivity(admin: Admin) {
        val intent = SelectCommunityActivity.getStartIntent(this).apply {
            putExtra("adminId", admin.id)
            putExtra("adminEmail", admin.email)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        startActivity(intent)
        finish()
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
