package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.databinding.DialogRegistrationSuccessBinding
import app.igormatos.botaprarodar.databinding.FragmentRegistrationBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationViewModel
import com.brunotmgomes.ui.extensions.createLoading
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

const val MIN_PASSWORD_LENGTH = 6

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val registrationViewModel: RegistrationViewModel by viewModel()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater)
        loadingDialog = requireContext().createLoading(R.layout.loading_dialog_animation)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
    }

    private fun bindViewModel() {
        registrationViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        })
        registrationViewModel.registrationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RegistrationState.SendSuccess -> onAdminRegistered()
                is RegistrationState.SendError -> {
                    val errorMessage = createErrorMessage(it.type)
                    showErrorMessage(errorMessage)
                }
                else -> {
                    // Do Nothing
                }
            }
        })
        registrationViewModel.emailValid.observe(viewLifecycleOwner, Observer {
            handleEmailErrorMessage(it)
        })
        registrationViewModel.passwordValid.observe(viewLifecycleOwner, Observer {
            handlePasswordErrorMessage(it)
        })
        binding.save.setOnClickListener {
            registrationViewModel.createAccount(
                binding.username.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }
    }

    private fun onAdminRegistered() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogBinding = DialogRegistrationSuccessBinding.inflate(layoutInflater)
        val dialog = dialogBuilder.let {
            it.setView(dialogBinding.root)
            it.create()
            it.show()
        }
        dialogBinding.loginButton.setOnClickListener {
            dialog.hide()
            findNavController().navigate(R.id.action_registrationFragment_to_welcomeActivity)
        }

    }


    private fun handlePasswordErrorMessage(isValidInput: Boolean) {
        binding.passwordLayout.apply {
            if (isValidInput) {
                isErrorEnabled = false
            } else {
                error = getString(R.string.prompt_password_min_length_warning)
            }
        }
    }

    private fun handleEmailErrorMessage(isValidInput: Boolean) {
        binding.emailLayout.apply {
            if (isValidInput) {
                isErrorEnabled = false
            } else {
                error = getString(R.string.email_format_warning)
            }
        }
    }

    private fun showErrorMessage(@StringRes messageId: Int) {
        Snackbar.make(
            binding.fragmentRegistrationContainer,
            messageId,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun createErrorMessage(errorType: BprErrorType): Int =
        when (errorType) {
            BprErrorType.NETWORK -> R.string.network_error_message
            else -> R.string.admin_registration_error
        }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.hide()
    }
}