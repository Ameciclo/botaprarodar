package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentBikeConfirmationBinding
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeConfirmationViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawUiState
import app.igormatos.botaprarodar.presentation.components.WithdrawConfirmationComponent
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
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
        val jetpackView = binding.confirmationComposeView
        jetpackView.setContent {
            BotaprarodarTheme {
                WithdrawConfirmationComponent(
                    handleClick = { viewModel.confirmBikeWithdraw() }
                )
            }
        }
        binding.viewModel?.uiState?.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            when (it) {
                is BikeWithdrawUiState.Error -> {
                    snackBarMaker(it.message, view).apply {
                        setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
                        show()
                    }
                }
                is BikeWithdrawUiState.Loading -> loadingDialog.show()
                is BikeWithdrawUiState.Success -> viewModel.navigateToNextStep()
            }
        }
    }
}