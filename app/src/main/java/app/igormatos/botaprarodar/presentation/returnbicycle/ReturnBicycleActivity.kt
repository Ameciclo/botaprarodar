package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.ThreeStepper
import app.igormatos.botaprarodar.presentation.components.button.BackButton
import app.igormatos.botaprarodar.presentation.components.navigation.return_bike.ReturnNavigationComponent
import app.igormatos.botaprarodar.presentation.components.navigation.return_bike.ReturnScreen
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class ReturnBicycleActivity : ComponentActivity() {
    private lateinit var returnBicycleNavController: NavHostController
    private val returnBicycleViewModel: ReturnBicycleViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    private var _availableBikes: MutableState<List<Bike>> = mutableStateOf(mutableListOf())
    private var currentStep: MutableState<StepConfigType> =
        mutableStateOf(StepConfigType.SELECT_BIKE)
    private lateinit var joinedCommunityId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                returnBicycleNavController = rememberNavController()
                Surface(color = MaterialTheme.colors.background) {
                    ReturnBicyclePage(
                        viewModel = returnBicycleViewModel,
                        navHostController = returnBicycleNavController,
                        bikes = _availableBikes.value,
                        handleClick = selectClickSteper,
                        backAction = { backAction() }
                    )
                }
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        initUI()
        observables()
        return super.onCreateView(name, context, attrs)
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        returnBicycleViewModel.setInitialStep()
        returnBicycleViewModel.getBikesInUseToReturn(joinedCommunityId)
    }

    private fun observables() {
        returnBicycleViewModel.bikesAvailableToReturn.observe(this, { simpleResult ->
            when (simpleResult) {
                is SimpleResult.Success -> {
                    _availableBikes.value = mutableListOf()
                    _availableBikes.value = simpleResult.data
                }
                is SimpleResult.Error -> {}
            }
        })
        returnBicycleViewModel.uiStep.observe(this, {
            currentStep.value = it
        })
    }

    private fun backAction() {
        when (currentStep.value) {
            StepConfigType.SELECT_BIKE -> {
                finish()
            }
            StepConfigType.QUIZ -> {
                returnBicycleNavController.navigate(ReturnScreen.ReturnSelectBike.route)
                returnBicycleViewModel.navigateToPrevious()
            }
            StepConfigType.CONFIRM_DEVOLUTION -> {
                returnBicycleNavController.navigate(ReturnScreen.ReturnQuiz.route)
                returnBicycleViewModel.navigateToPrevious()
            }
            StepConfigType.FINISHED_ACTION -> {
                finish()
                startActivity(Intent(this, ReturnBicycleActivity::class.java))
            }
        }
    }

    private val selectClickSteper: (data: Any?) -> Unit = { data: Any? ->
        when (returnBicycleViewModel.uiStep.value) {
            StepConfigType.SELECT_BIKE -> {
                returnBicycleNavController.navigate(ReturnScreen.ReturnQuiz.route)
//                val bike= data as Bike
                returnBicycleViewModel.navigateToNextStep()
            }
        }
    }
}
