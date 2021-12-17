package app.igormatos.botaprarodar.presentation.bikewithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.databinding.FragmentSelectUserBinding
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.components.CyclistListComponent
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectUserFragment : Fragment() {
    private val binding: FragmentSelectUserBinding by lazy {
        FragmentSelectUserBinding.inflate(layoutInflater)
    }

    private val viewModel: SelectUserViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getUserList(joinedCommunityId)

        val userListCompose = view?.findViewById<ComposeView>(R.id.userList)
        userListCompose?.setContent {
            BotaprarodarTheme {
                CyclistListComponent(joinedCommunityId = joinedCommunityId)
            }
        }
    }
}