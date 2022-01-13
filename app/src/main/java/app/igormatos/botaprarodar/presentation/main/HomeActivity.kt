package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.CyclistActions
import app.igormatos.botaprarodar.presentation.main.viewModel.HomeViewModel
import app.igormatos.botaprarodar.presentation.user.UserActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    private lateinit var joinedCommunityId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.uiState.observeAsState(HomeUiState(bikes = emptyList()))
            MainScreen(uiState, loadCyclistActions())
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        initUI()
        return super.onCreateView(name, context, attrs)
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getBikes(joinedCommunityId)
        viewModel.getUserList(joinedCommunityId)
    }

    @Composable
    private fun loadCyclistActions(): CyclistActions {
        val users by viewModel.userList.observeAsState()
        return CyclistActions(cyclistList = users ?: listOf(), handleClick = clickCyclist)
    }

    private val clickCyclist: (user: User) -> Unit = { user: User ->
        val intent =
            UserActivity.setupActivity(this, user, currentCommunityUserList = arrayListOf())
        startActivity(intent)
    }
}