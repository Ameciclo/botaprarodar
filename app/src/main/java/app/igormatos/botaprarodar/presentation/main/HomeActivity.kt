package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.main.homeViewModel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBikes(preferencesModule.getJoinedCommunity().id)
        setContent {
            val uiState by viewModel.uiState.observeAsState(HomeUiState())
            MainScreen(uiState)
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}

data class HomeUiState(
    var totalBikes: Int = 0,
    var totalBikesAvailable: Int = 0,
    var totalBikesWithdraw: Int = 0
) {
    companion object {
        fun fromBikes(bikes: List<Bike>): HomeUiState {
            val totalBikes = bikes.count()
            val totalBikesWithdraw = bikes.filter { it.inUse }.count()
            val totalBikesAvailable = totalBikes.minus(totalBikesWithdraw)

            return HomeUiState(totalBikes, totalBikesAvailable, totalBikesWithdraw)
        }
    }
}