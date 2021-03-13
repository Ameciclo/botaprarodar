package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentSelectBikeBinding
import app.igormatos.botaprarodar.presentation.adapter.SelectBikeListAdapter
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
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
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
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
        binding.viewModel?.availableBikes?.observe(viewLifecycleOwner, { bikeList ->
            selectBikeAdapter.submitList(bikeList)
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