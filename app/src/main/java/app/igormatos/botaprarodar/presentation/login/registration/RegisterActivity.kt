package app.igormatos.botaprarodar.presentation.login.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.databinding.ActivityRegistrationBinding
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseAuthActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: RegisterViewModel by viewModel()

    override val snackBarview: View
        get() = binding.btnSend

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar(binding.registrationToolbar)
        setupEventListeners()
        observeEvents()
    }

    private fun setupEventListeners() {
        binding.btnSend.setOnClickListener {
            resetFormErrors()
            viewModel.register()
        }
    }

    private fun resetFormErrors() {
        binding.ilEmail.error = null
    }

    private fun observeEvents() {
        viewModel.registerState.observe(this, { registerState ->
            when (registerState) {
                is RegisterState.Error -> {
                    loadingDialog.hide()
                    notifyErrorEvents(registerState.type)
                }
                RegisterState.Loading -> loadingDialog.show()
                RegisterState.Success -> {
                    loadingDialog.hide()
                    showSuccessConfirmDialog(
                        title = R.string.title_register_completed,
                        message = R.string.message_register_completed,
                        click = { finish() }
                    )
                }
            }
        })
    }

    private fun notifyErrorEvents(errorType: BprErrorType) {
        when (errorType) {
            BprErrorType.NETWORK -> showMessage(R.string.network_error_message)
            BprErrorType.INVALID_ACCOUNT -> binding.ilEmail.error =
                getString(R.string.email_already_registered_error)
            else -> showMessage(R.string.unkown_error)
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}