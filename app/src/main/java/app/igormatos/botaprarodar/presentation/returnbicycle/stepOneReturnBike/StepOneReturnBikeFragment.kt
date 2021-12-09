package app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentStepOneReturnBikeBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.StepOneBikesAdapter
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailActivity.Companion.TRIP_DETAIL_FLOW
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity.Companion.BIKE
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity.Companion.ORIGIN_FLOW
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_step_one_return_bike,
                container,
                false
            )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObservable()
        viewModel.setInitialStep()

        val originFlow = (activity as ReturnBikeActivity).intent.getStringExtra(ORIGIN_FLOW)
        val bike = (activity as ReturnBikeActivity).intent.extras?.getParcelable<Bike>(BIKE)
        if (originFlow.equals(TRIP_DETAIL_FLOW)) {
            bike?.let { bikeOnClickListener(it) }
        }

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
        viewModel.setBike(bike)
        viewModel.navigateToNextStep()
    }
}