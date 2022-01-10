package app.igormatos.botaprarodar.presentation.main.bikes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.NoConnectionInterceptor
import app.igormatos.botaprarodar.databinding.FragmentBikesBinding
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.adapter.BicyclesAdapter
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.main.viewModel.BikesViewModel
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BikesFragment : Fragment(), BicyclesAdapter.BicycleAdapterListener {

    val bicycleAdapter = BicyclesAdapter(this)
    private lateinit var binding: FragmentBikesBinding
    private val preferencesModule: SharedPreferencesModule by inject()
    private val bikesViewModel: BikesViewModel by viewModel()
    private var currentCommunityBikeList: ArrayList<Bike> = arrayListOf()

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
        observerBikes()
    }

    private fun initUI() {
        setupBtnRegisterClickEvent()
        setupRecyclerView()
        setupSearchBikesEvent()
    }

    private fun setupBtnRegisterClickEvent() {
        binding.btnRegisterBikes.setOnClickListener {
            val intent = BikeFormActivity.setupActivity(requireContext(), null,
                currentCommunityBikeList)
            startForResult.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvBikes.layoutManager = LinearLayoutManager(context)
        binding.rvBikes.adapter = bicycleAdapter
    }

    private fun setupSearchBikesEvent() {
        binding.tieSearchBikes.addTextChangedListener {
            bicycleAdapter.filter.filter(it.toString())
        }
    }

    private fun getBikes() {
        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        bikesViewModel.getBikes(joinedCommunityId)
    }

    private fun observerBikes() {
        bikesViewModel.bikes.observe(viewLifecycleOwner, {
            when (it) {
                is SimpleResult.Success -> {
                    currentCommunityBikeList = ArrayList(it.data)
                    bicycleAdapter.submitList(currentCommunityBikeList)
                }
                is SimpleResult.Error -> {
                    if (it.exception is NoConnectionInterceptor.NoConnectivityException) {
                        showErrorMessage(getString(R.string.connection_error))
                    } else {
                        showErrorMessage(getString(R.string.unkown_error))
                    }
                }
            }
        })
    }

    private fun showErrorMessage(errorMessage: String) {
        snackBarMaker(errorMessage, binding.btnRegisterBikes).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
            show()
        }
    }

    override fun onBicycleClicked(bike: Bike) {
        val intent = BikeFormActivity.setupActivity(requireContext(), bike, currentCommunityBikeList)
        startForResult.launch(intent)
    }

    override fun onBicycleLongClicked(bike: Bike): Boolean {
        Toast.makeText(requireContext(), bike.name, Toast.LENGTH_SHORT).show()
        return true
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                getBikes()
                showSnackBar(result.data)
            }
        }

    private fun showSnackBar(intent: Intent?) {
        intent?.getBooleanExtra("isEditModeAvailable", false)?.let {
            val message = getSuccessMessage(it)
            snackBarMaker(message, binding.clBikes).apply {
                setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.green))
//                anchorView = activity?.findViewById(R.id.navigation)
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