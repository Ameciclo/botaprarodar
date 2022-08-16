package app.igormatos.botaprarodar.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.ui.ExperimentalComposeUiApi
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.databinding.ActivityLoginBinding
import app.igormatos.botaprarodar.presentation.login.passwordRecovery.PasswordRecoveryActivity
import app.igormatos.botaprarodar.presentation.login.registration.RegisterActivity
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityActivity
import com.brunotmgomes.ui.extensions.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
class LoginActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override val snackBarview: View
        get() = binding.btnLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupEventListeners()
        observeEvents()
    }

    fun setupEventListeners() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.tvForgotPassword.setOnClickListener {
            navigateToPasswordRecoveryActivity()
        }

        binding.tvSignUp.setOnClickListener {
            navigateToRegistrationActivity()
        }
    }

    private fun observeEvents() {
        observeLoginState()
        observeResendEmailState()
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this, { loginState ->
            when (loginState) {
                is LoginState.Error -> {
                    loadingDialog.hide()
                    notifyErrorEvents(loginState.type)
                }
                LoginState.Loading -> loadingDialog.show()
                is LoginState.Success -> {
                    loadingDialog.hide()
                    navigateToMainActivity(loginState.admin)
                }
            }
        })
    }

    private fun observeResendEmailState() {
        viewModel.resendEmailState.observe(this, { resendEmailState ->
            when (resendEmailState) {
                is ResendEmailState.Error -> {
                    loadingDialog.hide()
                    notifyErrorEvents(resendEmailState.type)
                }
                ResendEmailState.Loading -> loadingDialog.show()
                ResendEmailState.Success -> {
                    loadingDialog.hide()
                    showSuccessConfirmDialog(
                        title = R.string.title_resend_email,
                        message = R.string.message_resend_email
                    )
                }
            }
        })
    }

    private fun notifyErrorEvents(errorType: BprErrorType) {
        when (errorType) {
            BprErrorType.NETWORK -> showMessage(R.string.network_error_message)
            BprErrorType.UNKNOWN -> showMessage(R.string.login_error)
            BprErrorType.INVALID_ACCOUNT, BprErrorType.INVALID_PASSWORD -> {
                binding.apply {
                    ilEmail.error = getString(R.string.sign_in_incorrect_email_password_error)
                    ilPassword.error = getString(R.string.sign_in_incorrect_email_password_error)
                }
            }

            BprErrorType.EMAIL_NOT_VERIFIED -> showMessageEmailNotVerified()
        }
    }

    private fun showMessageEmailNotVerified() {
        makeSnackBar(R.string.login_confirm_email_error)
            .setAction(R.string.resend_email) {
                viewModel.sendEmailVerification()
            }.show()
    }

    private fun navigateToMainActivity(admin: Admin) {
        val intent = SelectCommunityActivity.getStartIntent(this)
        intent.putExtra("adminId", admin.id)
        intent.putExtra("adminEmail", admin.email)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)
        finish()
    }

    private fun doLogin() {
        resetFormErrors()
        binding.ilPassword.hideKeyboard()
        val email: String = binding.ietEmail.text.toString()
        val password: String = binding.ietPassword.text.toString()
        viewModel.login(email, password)
    }

    private fun resetFormErrors() {
        binding.apply {
            ilEmail.error = null
            ilPassword.error = null
        }
    }

    private fun navigateToPasswordRecoveryActivity() {
        val intent = PasswordRecoveryActivity.getStartIntent(this)
        startActivity(intent)
    }

    private fun navigateToRegistrationActivity() {
        val intent = RegisterActivity.getStartIntent(this)
        startActivity(intent)
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
