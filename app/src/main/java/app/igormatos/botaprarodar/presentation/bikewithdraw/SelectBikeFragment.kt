package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentSelectBikeBinding
import app.igormatos.botaprarodar.presentation.adapter.SelectBikeListAdapter
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.components.BikeListComponent
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectBikeFragment : Fragment() {
    private val binding: FragmentSelectBikeBinding by lazy {
        FragmentSelectBikeBinding.inflate(layoutInflater)
    }

    private val viewModel: SelectBikeViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()

    private val selectBikeAdapter by lazy {
        SelectBikeListAdapter {
            viewModel.setBike(it)
            viewModel.navigateToNextStep()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        val bikeCard = view.findViewById<ComposeView>(R.id.bikeListCompose)
        binding.viewModel?.availableBikes?.observe(viewLifecycleOwner, Observer { bikeList ->
//            selectBikeAdapter.submitList(bikeList)
            bikeCard?.setContent {
                BotaprarodarTheme {
                    viewModel.availableBikes.value?.let { BikeListComponent(bikeList = bikeList) }
                }
            }
        })
        viewModel.setInitialStep()
    }

    private fun initUI() {
        binding.bikeList.layoutManager = LinearLayoutManager(context)
        binding.bikeList.adapter = selectBikeAdapter

        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getBikeList(joinedCommunityId)
    }

}