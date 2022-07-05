package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.databinding.ActivityPasswordRecoveryBinding
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordRecoveryActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityPasswordRecoveryBinding
    private val viewModel: RecoveryPasswordViewModel by viewModel()
    override val snackBarview: View
        get() = binding.btnSend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(binding.passwordRecoveryToolbar)

        observeEvents()
        setupEventListeners()
    }

    private fun observeEvents() {
        viewModel.passwordRecoveryState.observe(this, { passwordRecoveryState ->
            when (passwordRecoveryState) {
                is PasswordRecoveryState.Error -> {
                    loadingDialog.hide()
                    notifyErrorEvents(passwordRecoveryState.type)
                }
                PasswordRecoveryState.Loading -> loadingDialog.show()
                PasswordRecoveryState.Success -> {
                    loadingDialog.hide()
                    showSuccessConfirmDialog(
                        title = R.string.title_recovery_link_send,
                        message = R.string.message_password_recovery_link_send,
                        click = { finish() }
                    )
                }
            }
        })
    }

    private fun setupEventListeners() {
        binding.btnSend.setOnClickListener {
            resetFormErrors()
            val email = binding.ietEmail.text.toString().trim()
            viewModel.recoveryPassword(email)
        }
    }

    private fun resetFormErrors() {
        binding.ilEmail.error = null
    }

    private fun notifyErrorEvents(errorType: BprErrorType) {
        when (errorType) {
            BprErrorType.NETWORK -> showMessage(R.string.network_error_message)
            BprErrorType.INVALID_ACCOUNT -> binding.ilEmail.error =
                getString(R.string.sign_in_email_error)
            else -> showMessage(R.string.unkown_error)
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, PasswordRecoveryActivity::class.java)
        }
    }
}