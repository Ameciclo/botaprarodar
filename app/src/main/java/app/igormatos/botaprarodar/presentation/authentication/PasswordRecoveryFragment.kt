package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.databinding.FragmentPasswordRecoveryBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SendPasswordRecoveryViewState
import com.brunotmgomes.ui.extensions.createLoading
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class PasswordRecoveryFragment : Fragment() {
    private lateinit var binding: FragmentPasswordRecoveryBinding
    private val passwordRecoveryViewModel: PasswordRecoveryViewModel by koinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordRecoveryBinding.inflate(inflater)
        binding.viewmodel = passwordRecoveryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel?.viewState?.observe(viewLifecycleOwner, { viewState ->
            when (viewState) {
                SendPasswordRecoveryViewState.SendSuccess -> showSuccessToast()
                is SendPasswordRecoveryViewState.SendError -> showErrorSnackbar(viewState.type)
                else -> {
                    // nothing to do
                }
            }
        })
    }

    private fun showErrorSnackbar(type: BprErrorType) {
        val errorMessageId = when (type) {
            BprErrorType.NETWORK -> R.string.network_error_message
            else -> R.string.reset_password_email_sent_error
        }
        Snackbar.make(binding.root, getString(errorMessageId), Snackbar.LENGTH_SHORT).show()
    }

    private fun showSuccessToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.reset_password_email_sent),
            Toast.LENGTH_SHORT
        ).show()
    }
}