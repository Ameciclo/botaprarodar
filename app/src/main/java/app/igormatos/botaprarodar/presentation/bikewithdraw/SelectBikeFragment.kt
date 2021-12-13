package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentSelectBikeBinding
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
        binding.viewModel?.availableBikes?.observe(viewLifecycleOwner) { bikeList ->
            bikeCard?.setContent {
                BotaprarodarTheme {
                    viewModel.availableBikes.value?.let {
                        BikeListComponent(
                            bikeList = bikeList, vm = viewModel
                        )
                    }
                }
            }
        }
        viewModel.setInitialStep()
    }

    private fun initUI() {
        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getBikeList(joinedCommunityId)
    }

}