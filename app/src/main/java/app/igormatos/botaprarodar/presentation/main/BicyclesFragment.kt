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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentBikesBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.BicycleAdapterListener
import app.igormatos.botaprarodar.presentation.BicyclesAdapter
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BicyclesFragment : Fragment(), BicyclesAdapter.BicycleAdapterListener {

    val bicycleAdapter = BicyclesAdapter(this)
    private lateinit var binding: FragmentBikesBinding
    private val preferencesModule: SharedPreferencesModule by inject()
    private val bikesViewModel: BikesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bikes, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        getBikes()
        observers()
    }

    private fun initUI() {
        binding.btnRegisterBikes.setOnClickListener {
            val intent = Intent(it.context, BikeFormActivity::class.java)
            startActivity(intent)
        }

        binding.rvBikes.layoutManager = LinearLayoutManager(context)
        binding.rvBikes.adapter = bicycleAdapter
    }

    private fun getBikes() {
        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        bikesViewModel.getBikes(joinedCommunityId)
    }

    private fun observers() {
        bikesViewModel.bikes.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SimpleResult.Success -> {
                    bicycleAdapter.submitList(it.data)
                }
                is SimpleResult.Error -> {}
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