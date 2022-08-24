package app.igormatos.botaprarodar.presentation.user.userquiz

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.FragmentUserQuizBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.presentation.user.UserActivity
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.hideKeyboard
import com.brunotmgomes.ui.extensions.snackBarMaker
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserQuizFragment : Fragment() {

    private val viewModel: UserQuizViewModel by viewModel()

    private lateinit var binding: FragmentUserQuizBinding

    private lateinit var loadingDialog: AlertDialog

    private val args: UserQuizFragmentArgs by navArgs()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserQuizBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = requireContext().createLoading(R.layout.loading_dialog_animation)
        viewModel.init(args.user, args.editMode, args.deleteImagePaths.toList())
        setupBackButtonListener()
        setupLgpdObserver()
        setupStatusObserver()
        addMaskOnQuizTime()
    }

    private fun setupBackButtonListener() {
        binding.backButton.setOnClickListener {
            (activity as UserActivity).navigateToPrevious()
            navController.popBackStack()
        }
    }

    private fun setupLgpdObserver() {
        viewModel.isLgpdAgreement.observe(viewLifecycleOwner, Observer {
            if (viewModel.editMode) {
                viewModel.registerUser()
            } else if (it) {
                showConfirmDialog()
            }
        })
    }

    private fun setupStatusObserver() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ViewModelStatus.Success -> {
                    onSuccess()
                }
                is ViewModelStatus.Loading -> {
                    onLoading()
                }
                is ViewModelStatus.Error -> {
                    onError(it)
                    loadingDialog.dismiss()
                }
            }
        })
    }

    private fun onSuccess() {
        loadingDialog.dismiss()
        val intent = Intent().putExtra("isEditModeAvailable", viewModel.editMode)
        activity?.let {
            (it as UserActivity).navigateToNext()
        }
        activity?.setResult(RESULT_OK, intent)
        navigateToUserSuccessfullyRegistered()
    }

    private fun navigateToUserSuccessfullyRegistered() {
        val direction =
            UserQuizFragmentDirections.actionUserQuizFragmentToUserSuccessfullyRegisteredFragment()
        navController.navigate(direction)
    }

    private fun onLoading() {
        requireActivity().window.decorView.hideKeyboard()
        loadingDialog.show()
    }

    private fun onError(it: ViewModelStatus.Error<String>) {
        snackBarMaker(it.message, binding.scrollUserQuiz).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
            show()
        }
    }

    private fun showConfirmDialog() {
        val dialogModel = CustomDialogModel(
            title = getString(R.string.warning),
            message = getString(R.string.lgpd_message),
            primaryButtonText = getString(R.string.lgpd_confirm),
            primaryButtonListener = View.OnClickListener {
                viewModel.registerUser()
            }
        )

        CustomDialog.newInstance(dialogModel).show(childFragmentManager, CustomDialog.TAG)
    }

    private fun addMaskOnQuizTime() {
        binding.userQuizTimeOnWayOpenQuestion.addMask(
            EditTextFormatMask.FORMAT_HOUR
        )
    }
}
