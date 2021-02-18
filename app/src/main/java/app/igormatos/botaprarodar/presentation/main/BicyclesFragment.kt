package app.igormatos.botaprarodar.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelper
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.databinding.FragmentBikesBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.BicycleAdapterListener
import app.igormatos.botaprarodar.presentation.BicyclesAdapter
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormActivity
import kotlinx.android.synthetic.main.fragment_bikes.*
import org.koin.android.ext.android.inject

class BicyclesFragment : Fragment(), BicycleAdapterListener {

    lateinit var bicycleAdapter: BicyclesAdapter
    private val preferencesModule: SharedPreferencesModule by inject()
    private val firebaseHelper: FirebaseHelperModule by inject()

    private lateinit var binding: FragmentBikesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bikes, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bicycleAdapter = BicyclesAdapter(bicycleAdapterListener = this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterBikes.setOnClickListener {
            val intent = Intent(it.context, BikeFormActivity::class.java)
            startActivity(intent)
        }

        binding.rvBikes.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.rvBikes.adapter = bicycleAdapter

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id!!
        firebaseHelper.getBicycles(joinedCommunityId, listener = object : RequestListener<Bike> {
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
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
    }

    override fun onBicycleLongClicked(bike: Bike): Boolean {
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
        return true
    }
}