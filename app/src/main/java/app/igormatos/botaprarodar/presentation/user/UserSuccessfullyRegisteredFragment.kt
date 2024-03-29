package app.igormatos.botaprarodar.presentation.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentUserSuccessfullyRegisteredBinding
import app.igormatos.botaprarodar.presentation.components.WithdrawStepperActivity
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

class UserSuccessfullyRegisteredFragment : Fragment() {

    private lateinit var binding: FragmentUserSuccessfullyRegisteredBinding
    private val args: UserSuccessfullyRegisteredFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSuccessfullyRegisteredBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @ExperimentalComposeUiApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onEditMode()
        setupGoHomeButtonListener()
        setupLendButtonListener()
    }

    private fun onEditMode() {
        if (args.editMode) {
            binding.userSuccessfullyRegisteredTxt.text =
                getString(R.string.user_successfully_edited_label)
        }
    }

    @ExperimentalComposeUiApi
    private fun setupGoHomeButtonListener() {
        binding.goHomeButton.setOnClickListener {
            activity?.let {
                val intent = HomeActivity.getStartIntent(it)
                navigateToNextActivity(intent)
            }
        }

    }

    @ExperimentalCoroutinesApi
    private fun setupLendButtonListener() {
        binding.lendButton.setOnClickListener {
            activity?.let {
                val intent = WithdrawStepperActivity.startIntent(it)
                navigateToNextActivity(intent)
            }
        }
    }

    private fun navigateToNextActivity(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().startActivity(intent)
        activity?.finish()
    }
}
