package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.components.CustomDialog
import app.igormatos.botaprarodar.databinding.FragmentStepFinalReturnBikeBinding
import app.igormatos.botaprarodar.domain.model.CustomDialogModel
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import com.brunotmgomes.ui.extensions.createLoading
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                BikeDevolutionUiState.Loading -> {
                    loadingDialog.show()
                }
                is BikeDevolutionUiState.Success -> {
                    showConfirmDialog()
                }
                is BikeDevolutionUiState.Error -> {
                    Toast.makeText(requireContext(), "ERRO", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.restartDevolutionFlow.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    navController.popBackStack(R.id.returnBikeFragment, false)
                }
            }
        })

    }

    @ExperimentalCoroutinesApi
    private fun showConfirmDialog() {
        val dialogModel = CustomDialogModel(
            icon = R.drawable.ic_success,
            title = getString(R.string.success_devolution_message),
            primaryButtonText = getString(R.string.back_to_init_title),
            primaryButtonListener = View.OnClickListener {

                val originFlow = (activity as ReturnBikeActivity).intent.getStringExtra(
                    ReturnBikeActivity.ORIGIN_FLOW
                )
                if (originFlow.equals(TripDetailActivity.TRIP_DETAIL_FLOW)) {
                    requireActivity().setResult(RESULT_OK)
                }

                requireActivity().finish()
            }
        )

        CustomDialog.newInstance(dialogModel).apply {
            isCancelable = false
        }.show(requireActivity().supportFragmentManager, CustomDialog.TAG)

    }

}