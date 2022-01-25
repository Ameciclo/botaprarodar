package app.igormatos.botaprarodar.presentation.returnbicycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ReturnBicycleActivity : ComponentActivity() {
    private val returnBicycleViewModel: ReturnBicycleViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    private var _availableBikes: MutableState<List<Bike>> = mutableStateOf(mutableListOf())
    private lateinit var joinedCommunityId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Observables()
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ReturnBicyclePage(
                        bikes = _availableBikes.value,
                        handleClick = {})
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initUI()
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        returnBicycleViewModel.getBikesInUseToReturn(joinedCommunityId)
        returnBicycleViewModel.setInitialStep()
    }

    @Composable
    private fun Observables() {
        val simpleResult = returnBicycleViewModel.bikesAvailableToReturn.observeAsState()
        when (simpleResult.value) {
            is SimpleResult.Success -> {
                _availableBikes.value = mutableListOf()
                _availableBikes.value = (simpleResult.value as SimpleResult.Success).data
            }
            is SimpleResult.Error -> {}
        }
    }
}
