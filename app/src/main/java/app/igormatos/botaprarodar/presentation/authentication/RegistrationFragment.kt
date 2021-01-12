package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentRegistrationBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationViewModel
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.snackBarMaker
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment: Fragment() {
    private lateinit var binding : FragmentRegistrationBinding
    private val registrationViewModel : RegistrationViewModel by viewModel()
    private lateinit var loadingDialog : AlertDialog

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
            when(it) {
                is RegistrationState.Success -> snackBarMaker(getString(R.string.promt_email_confirmation_title), binding.container)
                is RegistrationState.Error -> snackBarMaker(getString(R.string.promt_registration_error), binding.container)
                is RegistrationState.InvalidEmail -> binding.emailLayout.error = getString(R.string.email_format_warning)
            }
        })
        binding.save.setOnClickListener {
            registrationViewModel.createAccount(binding.email.text.toString().trim(), binding.password.text.toString().trim())
        }
    }

    private fun verifyPasswordLength() {

    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.hide()
    }
}