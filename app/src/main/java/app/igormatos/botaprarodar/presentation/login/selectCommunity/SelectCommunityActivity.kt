package app.igormatos.botaprarodar.presentation.login.selectCommunity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.databinding.ActivitySelectCommunityBinding
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.createcommunity.AddCommunityActivity
import app.igormatos.botaprarodar.presentation.login.BaseAuthActivity
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import app.igormatos.botaprarodar.presentation.welcome.CommunityAdapter
import com.brunotmgomes.ui.extensions.visible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class SelectCommunityActivity : BaseAuthActivity() {

    private lateinit var binding: ActivitySelectCommunityBinding
    private lateinit var chooseCommunityAdapter: CommunityAdapter

    private val viewModel: SelectCommunityViewModel by viewModel()

    override val snackBarview: View
        get() = binding.btnAddCommunity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.selectCommunityToolbar)

        setupAdapter()
        setupRecyclerView()

        observeEvents()
        setupEventListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCommunities()
    }

    private fun setupAdapter() {
        chooseCommunityAdapter = CommunityAdapter(arrayListOf()) { community ->
            clickCommunity(community)
        }
    }

    private fun clickCommunity(community: Community) {
        viewModel.saveJoinedCommmunity(community)
        toMainActivity()
    }

    private fun toMainActivity() {
        val intent = HomeActivity.getStartIntent(this)
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView() {
        binding.recyclerviewCommunity.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chooseCommunityAdapter
        }
    }

    private fun observeEvents() {
        viewModel.selectCommunityState.observe(this, { selectCommunityState ->
            when (selectCommunityState) {
                is SelectCommunityState.Error -> {
                    loadingDialog.hide()
                    notifyErrorEvents(selectCommunityState.error)
                }
                SelectCommunityState.Loading -> loadingDialog.show()
                is SelectCommunityState.Success -> {
                    loadingDialog.hide()
                    notifySuccessEvents(selectCommunityState)
                }
            }
        })
    }

    private fun notifyErrorEvents(errorType: BprErrorType) {
        when (errorType) {
            BprErrorType.NETWORK -> showMessage(R.string.network_error_message)
            else -> showMessage(R.string.unkown_error)
        }
    }

    private fun notifySuccessEvents(selectCommunityState: SelectCommunityState.Success) {
        selectCommunityState.apply {
            when (userInfoState) {
                is UserInfoState.Admin -> {
                    binding.btnAddCommunity.visible()
                    chooseCommunityAdapter.updateList(userInfoState.communities)
                }
                is UserInfoState.NotAdmin ->
                    chooseCommunityAdapter.updateList(userInfoState.communities)
                UserInfoState.NotAdminWithoutCommunities -> showAlertMessage()
            }
        }
    }

    private fun showAlertMessage() {
        showAlertConfirmDialog(
            title = R.string.warning,
            message = R.string.login_no_communities_allowed,
            click = {
                viewModel.forceUserLogout()
                navigateToLoginActivity()
            }
        )
    }

    private fun navigateToLoginActivity() {
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }

    fun setupEventListeners() {
        binding.btnAddCommunity.setOnClickListener {
            navigateToAddCommunityActivity()
        }
    }

    private fun navigateToAddCommunityActivity() {
        val intent = Intent(this, AddCommunityActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SelectCommunityActivity::class.java)
        }
    }
}