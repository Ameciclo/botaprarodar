package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.databinding.FragmentBikeConfirmationBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeConfirmationViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawUiState
import app.igormatos.botaprarodar.presentation.components.FinishWithdraw
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.snackBarMaker
import org.koin.androidx.viewmodel.ext.android.viewModel

class BikeConfirmationFragment : Fragment() {
    private val binding: FragmentBikeConfirmationBinding by lazy {
        FragmentBikeConfirmationBinding.inflate(layoutInflater)
    }

    private val viewModel: BikeConfirmationViewModel by viewModel()
    private val loadingDialog: AlertDialog by lazy {
        requireContext().createLoading(R.layout.loading_dialog_animation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel?.uiState?.observe(viewLifecycleOwner, Observer {
            loadingDialog.dismiss()
            when (it) {
                is BikeWithdrawUiState.Error -> {
                    snackBarMaker(it.message, view).apply {
                        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                        show()
                    }
                }
                BikeWithdrawUiState.Loading -> loadingDialog.show()

                is BikeWithdrawUiState.Success -> {
                    val intent = Intent(context, FinishWithdraw::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    private fun showConfirmDialog() {
        val dialogModel = CustomDialogModel(
            icon = R.drawable.ic_success,
            title = getString(R.string.success_withdraw_message),
            primaryButtonText = getString(R.string.back_to_init_title),
            primaryButtonListener = View.OnClickListener {
                requireActivity().finish()
            }
        )

        CustomDialog.newInstance(dialogModel).apply {
            isCancelable = false
        }.show(requireActivity().supportFragmentManager, CustomDialog.TAG)

    }

}