package app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentBikesBinding
import app.igormatos.botaprarodar.databinding.FragmentStepOneBikesBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.BicyclesAdapter
import app.igormatos.botaprarodar.presentation.adapter.StepOneBikesAdapter
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.StepFinalReturnBikeFragment
import com.brunotmgomes.ui.SimpleResult
import kotlinx.android.synthetic.main.fragment_step_one_bikes.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class StepOneBikesFragment : Fragment(), StepOneBikesAdapter.ReturnBikesAdapterClickListener {

    private val stepOneBikesAdapter = StepOneBikesAdapter(this)
    private lateinit var binding: FragmentStepOneBikesBinding
    private val preferencesModule: SharedPreferencesModule by inject()
    private val viewModel: StepOneBikesViewModel by viewModel()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_one_bikes, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObservable()
//        viewModel.setInitialStep()
    }

    fun initUI() {
        binding.rvBikesReturn.layoutManager = LinearLayoutManager(context)
        binding.rvBikesReturn.adapter = stepOneBikesAdapter

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getBikesInUseToReturn(joinedCommunityId)
    }

    fun initObservable() {
        viewModel.bikesAvailableToReturn.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SimpleResult.Success -> {
                    stepOneBikesAdapter.submitList(it.data)
                }
                is SimpleResult.Error -> {
                }
            }
        })
    }

    override fun bikeOnClickListener(bike: Bike) {
        viewModel.setReturnBike(bike)
        viewModel.navigateToNextStep()
        val direction =
            StepOneBikesFragmentDirections.actionReturnBikeFragmentToStepFinalReturnBikeFragment()
        navController.navigate(direction)
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
    }
}