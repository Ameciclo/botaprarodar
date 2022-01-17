package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.main.viewModel.HomeViewModel
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
            val users by viewModel.users.observeAsState(emptyList())
            val bikes by viewModel.bikes.observeAsState(emptyList())
            var uiState by remember { mutableStateOf(HomeUiState()) }

            uiState = HomeUiState.fromBikes(bikes)

            MainScreen(
                homeUiState = uiState,
                users = users,
                bikes = bikes
            )
        }
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.getBikes(joinedCommunityId)
        viewModel.getUsers(joinedCommunityId)
    }

    companion object {
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}