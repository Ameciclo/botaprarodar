package app.igormatos.botaprarodar.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.databinding.FragmentEmailValidationBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationState.SendSuccess
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class EmailValidationFragment : Fragment() {
    private lateinit var binding: FragmentEmailValidationBinding
    private val emailValidationViewModel: EmailValidationViewModel by koinViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailValidationBinding.inflate(inflater)
        binding.viewmodel = emailValidationViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner?.let { it ->
            binding.viewmodel?.viewState?.observe(it) { viewState ->
                when (viewState) {
                    is EmailValidationState.SendError -> {
                        val errorMessage = createErrorMessage(viewState.type)
                        showErrorMessage(errorMessage)
                    }
                    is SendSuccess -> {
                        val navDirections = createNavDirection(viewState.isAdminRegisted)
                        findNavController().navigate(navDirections)
                    }
                    else -> {
                        // Nothing to do
                    }
                }
            }
        }
    }

    private fun createNavDirection(newUser: Boolean): NavDirections {
        return if (newUser) {
            EmailValidationFragmentDirections.actionEmailValidationFragmentToRegistrationFragment()
        } else {
            EmailValidationFragmentDirections.actionEmailValidationFragmentToSignInFragment(
                binding.viewmodel?.emailField?.value ?: ""
            )
        }
    }

    private fun showErrorMessage(@StringRes messageId: Int) {
        Snackbar.make(
            binding.container,
            messageId,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun createErrorMessage(errorType: BprErrorType): Int =
        when (errorType) {
            BprErrorType.NETWORK -> R.string.network_error_message
            else -> R.string.login_error
        }
}
