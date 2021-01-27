package app.igormatos.botaprarodar.presentation.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.databinding.FragmentSignInBinding
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import app.igormatos.botaprarodar.presentation.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val args: SignInFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val signInViewModel: SignInViewModel by koinViewModel()
        binding = FragmentSignInBinding.inflate(inflater)
        binding.viewmodel = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner?.let { it ->
            binding.viewmodel?.viewState?.observe(it) { viewState ->
                when (viewState) {
                    is SignInViewState.SendError -> {
                        hideKeyboard(requireContext(), binding.password)
                        updatePasswordError(viewState.type)
                        val errorMessage = createErrorMessage(viewState.type)
                        showErrorMessage(errorMessage)
                    }
                    is SignInViewState.SendSuccess -> {
                        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        startActivity(intent)
                    }
                    SignInViewState.SendLoading -> {
                        //Nothing to do
                    }
                }
            }
        }

        binding.signIn.setOnClickListener {
            binding.viewmodel?.sendForm(args.email)
        }

        binding.forgottenPassword.setOnClickListener {
            val navDirections =
                SignInFragmentDirections.actionSignInFragmentToPasswordRecoveryFragment()
            findNavController().navigate(navDirections)
        }

        binding.welcomeMessage.text = HtmlCompat.fromHtml(
            resources.getString(R.string.prompt_welcome_message, args.email),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun createErrorMessage(errorType: BprErrorType): Int =
        when (errorType) {
            BprErrorType.NETWORK -> R.string.network_error_message
            BprErrorType.UNKNOWN -> R.string.login_error
            BprErrorType.UNAUTHORIZED -> R.string.sign_in_password_error
        }

    private fun showErrorMessage(@StringRes messageId: Int) {
        Snackbar.make(
            binding.fragmentSignInContainer,
            messageId,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun updatePasswordError(errorType: BprErrorType) {
        when (errorType) {
            BprErrorType.UNAUTHORIZED -> {
                binding.password.error =
                    getString(R.string.sign_in_password_error)
                binding.password.requestFocus()
            }
            else -> {
                //nothing to do
            }
        }
    }
}
