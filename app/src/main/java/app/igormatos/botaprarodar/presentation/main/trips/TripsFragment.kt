package app.igormatos.botaprarodar.presentation.main.trips

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.databinding.FragmentTripsBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Withdraw
import app.igormatos.botaprarodar.presentation.adapter.BikeActionMenuAdapter
import app.igormatos.botaprarodar.presentation.adapter.TripsAdapter
import app.igormatos.botaprarodar.presentation.adapter.WithdrawAdapter
import app.igormatos.botaprarodar.presentation.decoration.BikeActionDecoration
import kotlinx.android.synthetic.main.fragment_trips.*
import kotlinx.android.synthetic.main.fragment_trips.view.*
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class TripsFragment : Fragment() {

    private val preferencesModule: SharedPreferencesModule by inject()

    private lateinit var binding: FragmentTripsBinding
    private val tripsViewModel: TripsViewModel by viewModel()

    val itemAdapter = WithdrawAdapter()
    private val bikeActionMenuAdapter = BikeActionMenuAdapter(
        BikeActionsMenuType.values().toMutableList(),
        ::navigateToReturnBikeActivity,
        ::navigateToBikeWithdrawActivity
    )
    var loadingDialog: AlertDialog? = null

    val tripsAdapter by lazy { TripsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTripsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBikeActionRecyclerView()
        setupObserves()

        val selectedCommunityId = preferencesModule.getJoinedCommunity().id!!
        getWithdrawals(selectedCommunityId)

        tripsViewModel.loadBikeActions()

        val list: List<TripsAdapter.TripsLayoutType> = mutableListOf(
            TripsAdapter.TripsLayoutType.TitleType("Dom, 24 de janeiro"),
            TripsAdapter.TripsLayoutType.BikeType(
                Bike(
                    name = "Monark",
                    orderNumber = 1234,
                    serialNumber = "dsf1234234"
                )
            )
        )
        binding.apply {
            rvActivities.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setRecyclerViewItemMarginRight()
            rvActivities.adapter = tripsAdapter
        }
        tripsAdapter.submitList(list)
    }

    private fun navigateToBikeWithdraw() {
        val directions = TripsFragmentDirections.navigateFromHomeToBikeWithDraw()
        findNavController().navigate(directions)
    }

    private fun getWithdrawals(selectedCommunityId: String) {
        FirebaseHelper.getWithdrawals(
            selectedCommunityId,
            { loadingDialog?.dismiss() },
            object : RequestListener<Withdraw> {
                override fun onChildChanged(result: Withdraw) {
                    itemAdapter.updateItem(result)
                }

                override fun onChildAdded(result: Withdraw) {
                    loadingDialog?.dismiss()
                    itemAdapter.addItem(result)
                    preferencesModule.incrementTripCount()
                }

                override fun onChildRemoved(result: Withdraw) {
                    itemAdapter.removeItem(result)
                }
            })
    }

    private fun setupObserves() {
        tripsViewModel.getBikeActions().observe(viewLifecycleOwner, Observer {
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
}
