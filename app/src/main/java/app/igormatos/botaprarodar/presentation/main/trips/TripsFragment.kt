package app.igormatos.botaprarodar.presentation.main.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentTripsBinding
import app.igormatos.botaprarodar.presentation.adapter.BikeActionMenuAdapter
import app.igormatos.botaprarodar.presentation.adapter.TripsAdapter
import app.igormatos.botaprarodar.presentation.decoration.BikeActionDecoration
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.createLoading
import kotlinx.android.synthetic.main.fragment_trips.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class TripsFragment : Fragment(), TripsAdapter.TripsAdapterClickListener {

    private val preferencesModule: SharedPreferencesModule by inject()

    private lateinit var binding: FragmentTripsBinding
    private val tripsViewModel: TripsViewModel by viewModel()

    private val bikeActionMenuAdapter = BikeActionMenuAdapter(
        BikeActionsMenuType.values().toMutableList(),
        ::navigateToReturnBikeActivity,
        ::navigateToBikeWithdrawActivity
    )

    private val loadingDialog: AlertDialog by lazy {
        requireActivity().createLoading(R.layout.loading_dialog_animation)
    }

    private val tripsAdapter by lazy { TripsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBikeActionRecyclerView()
        setupObserves()

        val selectedCommunityId = preferencesModule.getJoinedCommunity().id

        tripsViewModel.loadBikeActions()

        setupTripsRecyclerView()

        tripsViewModel.getBikes(selectedCommunityId)
        observerTrips()
    }

    private fun setupTripsRecyclerView() {
        binding.apply {
            rvActivities.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setRecyclerViewItemMarginRight()
            rvActivities.adapter = tripsAdapter
        }
    }

    private fun observerTrips() {
        tripsViewModel.trips.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SimpleResult.Success -> {
                    tripsAdapter.submitList(it.data)
                }
                is SimpleResult.Error -> {
                    Toast.makeText(requireContext(), "ERRO", Toast.LENGTH_SHORT).show()
                }
            }
        })

        tripsViewModel.uiState.observe(viewLifecycleOwner, { uiState ->
            when (uiState) {
                is UiState.Error -> loadingDialog.hide()
                UiState.Loading -> loadingDialog.show()
                UiState.Success -> loadingDialog.hide()
            }
        })
    }

    private fun setupObserves() {
        tripsViewModel.getBikeActions().observe(viewLifecycleOwner, {
            bikeActionMenuAdapter.updateItems(it)
        })
    }

    private fun setupBikeActionRecyclerView() {
        binding.apply {
            bikeActionMenuRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setRecyclerViewItemMarginRight()
            bikeActionMenuRecyclerView.adapter = bikeActionMenuAdapter
            bikeActionMenuAdapter.notifyDataSetChanged()
        }
    }

    private fun setRecyclerViewItemMarginRight() {
        bikeActionMenuRecyclerView.addItemDecoration(
            BikeActionDecoration(
                4,
                bikeActionMenuAdapter.itemCount
            )
        )
    }

    private fun navigateToReturnBikeActivity() {
        val action = TripsFragmentDirections.actionNavigationHomeToReturnBikeActivity()
        findNavController().navigate(action)
    }

    private fun navigateToBikeWithdrawActivity() {
        val action = TripsFragmentDirections.navigateFromHomeToBikeWithDraw()
        findNavController().navigate(action)
    }

    override fun tripOnClickListener(id: String?, bikeId: String?, bikeStatus: String?) {
        val action = TripsFragmentDirections.actionNavigationHomeToTripDetailActivity(
            id.orEmpty(),
            bikeId.orEmpty(),
            bikeStatus.orEmpty()
        )
        findNavController().navigate(action)
    }
}
