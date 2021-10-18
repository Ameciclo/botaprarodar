package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.databinding.FragmentPasswordRecoveryBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SendPasswordRecoveryViewState
import com.brunotmgomes.ui.extensions.snackBarMaker
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

@Deprecated("use PasswordRecoveryActivity instead")
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
                SendPasswordRecoveryViewState.SendSuccess -> {
                    showSuccessSnackbar()
                    findNavController().navigateUp()
                }
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
        val color = getColor(requireContext(), R.color.red)
        createSnackbar(errorMessageId, color).show()
    }

    private fun showSuccessSnackbar() {
        val color = getColor(requireContext(), R.color.green)
        createSnackbar(R.string.reset_password_email_sent, color).show()
    }

    private fun createSnackbar(errorMessageId: Int, color: Int): Snackbar {
        return snackBarMaker(
            message = getString(errorMessageId),
            container = binding.root,
            actionButtonText = getString(R.string.confirm),
            backgroundTint = color
        )
    }
}
