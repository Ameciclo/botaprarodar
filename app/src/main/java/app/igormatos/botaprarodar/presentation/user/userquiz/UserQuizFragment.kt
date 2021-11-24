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
import androidx.navigation.fragment.navArgs
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.common.utils.EditTextFormatMask
import app.igormatos.botaprarodar.databinding.FragmentUserQuizBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.hideKeyboard
import com.brunotmgomes.ui.extensions.snackBarMaker
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserQuizFragment : Fragment() {

    private val viewModel: UserQuizViewModel by viewModel()

    private lateinit var binding: FragmentUserQuizBinding

    private lateinit var loadingDialog: AlertDialog

    private val args: UserQuizFragmentArgs by navArgs()

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
        setupObservables()
    }

    private fun setupObservables() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ViewModelStatus.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent().putExtra(
                        "isEditModeAvailable",
                        viewModel.editMode
                    )
                    activity?.setResult(RESULT_OK, intent)
                    activity?.finish()
                }
                is ViewModelStatus.Loading -> {
                    requireActivity().window.decorView.hideKeyboard()
                    loadingDialog.show()
                }
                is ViewModelStatus.Error -> {
                    snackBarMaker(it.message, binding.scrollUserQuiz).apply {
                        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                        show()
                    }
                    loadingDialog.dismiss()
                }
            }
        })

        viewModel.lgpd.observe(viewLifecycleOwner, Observer {
            if (viewModel.editMode) {
                viewModel.registerUser()
            } else if (it) {
                showConfirmDialog()
            }
        })

        binding.userQuizTimeOnWayOpenQuestion.addMask(
            EditTextFormatMask.FORMAT_HOUR
        )
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
}