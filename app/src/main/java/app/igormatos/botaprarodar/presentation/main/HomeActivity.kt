package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.main.viewModel.HomeViewModel
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
        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}