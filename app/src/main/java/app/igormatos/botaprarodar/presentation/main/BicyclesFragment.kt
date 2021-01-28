package app.igormatos.botaprarodar.presentation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.BicycleAdapterListener
import app.igormatos.botaprarodar.presentation.BicyclesAdapter
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormActivity
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject

class BicyclesFragment : Fragment(), BicycleAdapterListener {

    lateinit var bicycleAdapter: BicyclesAdapter
    private val preferencesModule: SharedPreferencesModule by inject()
    private val firebaseHelperModule: FirebaseHelperModule by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bicycleAdapter = BicyclesAdapter(bicycleAdapterListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addItemFab.setOnClickListener {
            val intent = BikeFormActivity.setupActivity(requireContext(), null)
            startForResult.launch(intent)
        }

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = bicycleAdapter

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        firebaseHelperModule.getBicycles(
            joinedCommunityId,
            listener = object : RequestListener<Bike> {
                override fun onChildChanged(result: Bike) {
                    bicycleAdapter.updateItem(result)
                }

                override fun onChildAdded(result: Bike) {
                    bicycleAdapter.addItem(result)
                }

                override fun onChildRemoved(result: Bike) {
                    bicycleAdapter.removeItem(result)
                }

            })
    }

    override fun onBicycleClicked(bike: Bike) {
        val intent = BikeFormActivity.setupActivity(requireContext(), bike)
        startForResult.launch(intent)
    }

    override fun onBicycleLongClicked(bike: Bike): Boolean {
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
        return true
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showSnackBar(result.data)
            }
        }

    private fun showSnackBar(intent: Intent?) {
        intent?.getBooleanExtra("isEditModeAvailable", false)?.let {
            val message = getSuccessMessage(it)
            snackBarMaker(message, requireView()).apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
                show()
            }
        }
    }

    private fun getSuccessMessage(isEditModeAvailable: Boolean) =
        if (isEditModeAvailable)
            getString(R.string.bicycle_update_success)
        else
            getString(R.string.bicycle_add_success)
}