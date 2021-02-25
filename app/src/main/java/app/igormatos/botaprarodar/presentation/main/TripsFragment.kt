package app.igormatos.botaprarodar.presentation.main

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.databinding.FragmentTripsBinding
import app.igormatos.botaprarodar.domain.model.Withdraw
import app.igormatos.botaprarodar.presentation.adapter.BikeActionMenuAdapter
import app.igormatos.botaprarodar.presentation.adapter.WithdrawAdapter
import app.igormatos.botaprarodar.presentation.bicyclewithdrawal.choosebicycle.WithdrawActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import kotlinx.android.synthetic.main.fragment_trips.*
import kotlinx.android.synthetic.main.fragment_trips.view.*
import org.koin.android.ext.android.inject

class TripsFragment : Fragment() {

    private val preferencesModule: SharedPreferencesModule by inject()

    private lateinit var binding: FragmentTripsBinding

    val itemAdapter = WithdrawAdapter()
    val bikeActionMenuAdapter = BikeActionMenuAdapter(BikeActionsMenuType.values().toMutableList(), ::navigateToReturnBikeActivity)
    var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTripsBinding.inflate(inflater)

        val bitmap = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_directions_bike
        )

        binding.root.addItemFab.setImageDrawable(bitmap)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItemFab.setOnClickListener {
            val intent = Intent(it.context, WithdrawActivity::class.java)
            startActivity(intent)
        }

        setupTripsRecyclerView()
        setupBikeActionRecyclerView()

        binding.bikeActionMenuRecyclerView

        val selectedCommunityId = preferencesModule.getJoinedCommunity().id!!
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

    private fun setupTripsRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = itemAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
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
        bikeActionMenuRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val currentPosition = parent.getChildLayoutPosition(view)
                val listSize = bikeActionMenuAdapter.itemCount

                setMarginByChildPositionList(currentPosition, outRect, listSize)
            }
        })
    }

    private fun setMarginByChildPositionList(
        currentPosition: Int,
        outRect: Rect,
        listSize: Int) {

        when (currentPosition) {
            0 -> outRect.right = getAnIntDp(8)
            listSize - 1 -> outRect.left = getAnIntDp(8)
            else -> {
                outRect.right = getAnIntDp(8)
                outRect.left = getAnIntDp(8)
            }
        }
    }

   private fun getAnIntDp(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

  private fun navigateToReturnBikeActivity(){
       val action = TripsFragmentDirections.actionNavigationHomeToReturnBikeActivity()
        findNavController().navigate(action)
//      Navigation.findNavController(requireActivity(), R.id.action_navigationHome_to_returnBikeActivity)
    }
}