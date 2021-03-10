package app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentStepOneReturnBikeBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.StepOneBikesAdapter
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class StepOneReturnBikeFragment : Fragment(), StepOneBikesAdapter.ReturnBikesAdapterClickListener {

    private val stepOneBikesAdapter = StepOneBikesAdapter(this)
    private lateinit var binding: FragmentStepOneReturnBikeBinding
    private val preferencesModule: SharedPreferencesModule by inject()
    private val viewModel: StepOneReturnBikeViewModel by viewModel()

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_one_return_bike, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObservable()
        viewModel.setInitialStep()
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
//        val direction =
//            StepOneBikesFragmentDirections.actionReturnBikeFragmentToStepFinalReturnBikeFragment()
//        navController.navigate(direction)
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
    }
}