package app.igormatos.botaprarodar.presentation.bikewithdraw

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
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentBikeConfirmationBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeConfirmationViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawUiState
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.android.synthetic.main.activity_fullscreen_image.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BikeConfirmationFragment : Fragment() {
    private val binding: FragmentBikeConfirmationBinding by lazy {
        FragmentBikeConfirmationBinding.inflate(layoutInflater)
    }

    private val viewModel: BikeConfirmationViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
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

                is BikeWithdrawUiState.Success -> showConfirmDialog()
            }
        })
    }

    private fun showConfirmDialog() {
        val dialogModel = CustomDialogModel(
            icon = R.drawable.ic_confirm,
            title = getString(R.string.warning),
            message = getString(R.string.lgpd_message),
            primaryButtonText = getString(R.string.repeat_withdraw_title),
            secondaryButtonText = getString(R.string.back_to_init_title),
            primaryButtonListener = View.OnClickListener {
                binding.viewModel?.restartWithdraw()
            },
            secondaryButtonListener = View.OnClickListener {
                requireActivity().finish()
            }
        )

        CustomDialog.newInstance(dialogModel).apply {
            isCancelable = false
        }.show(requireActivity().supportFragmentManager, CustomDialog.TAG)

    }

}