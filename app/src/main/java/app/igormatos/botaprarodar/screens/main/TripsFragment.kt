package app.igormatos.botaprarodar.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.model.Withdraw
import app.igormatos.botaprarodar.local.SharedPreferencesModule
import app.igormatos.botaprarodar.network.FirebaseHelper
import app.igormatos.botaprarodar.network.RequestListener
import app.igormatos.botaprarodar.screens.WithdrawAdapter
import app.igormatos.botaprarodar.screens.bicyclewithdrawal.choosebicycle.WithdrawActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import org.koin.android.ext.android.inject


class TripsFragment : androidx.fragment.app.Fragment() {

    private val preferencesModule: SharedPreferencesModule by inject()

    val itemAdapter = WithdrawAdapter()
    var loadingDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        val bitmap = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_directions_bike
        )
        rootView.addItemFab.setImageDrawable(bitmap)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = Intent(it.context, WithdrawActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                recyclerView.context,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )

//        loadingDialog = context?.showLoadingDialog()

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

}