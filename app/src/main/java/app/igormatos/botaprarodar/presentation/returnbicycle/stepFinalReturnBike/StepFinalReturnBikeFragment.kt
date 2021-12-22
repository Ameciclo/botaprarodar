package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.databinding.FragmentStepFinalReturnBikeBinding
import com.brunotmgomes.ui.extensions.createLoading
import org.koin.androidx.viewmodel.ext.android.viewModel

class StepFinalReturnBikeFragment : Fragment() {

    private lateinit var binding: FragmentStepFinalReturnBikeBinding
    private val viewModel: StepFinalReturnBikeViewModel by viewModel()

    private val loadingDialog: AlertDialog by lazy {
        requireContext().createLoading(R.layout.loading_dialog_animation)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_step_final_return_bike,
                container,
                false
            )
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> {
                    loadingDialog.show()
                }
                is UiState.Success -> {
                    viewModel.navigateToNextStep()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), "ERRO", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.restartDevolutionFlow.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    navController.popBackStack(R.id.returnBikeFragment, false)
                }
            }
        })

    }
}