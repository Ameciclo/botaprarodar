package app.igormatos.botaprarodar.presentation.components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawUiState
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.navigation.WithdrawNaviationComponent
import app.igormatos.botaprarodar.presentation.components.navigation.WithdrawScreen
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import com.brunotmgomes.ui.extensions.createLoading
import com.brunotmgomes.ui.extensions.snackBarMaker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class WithdrawStepper : ComponentActivity() {
    private lateinit var joinedCommunityId: String
    private val viewModel: WithdrawViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    lateinit var withdrawNavController: NavHostController
    private val loadingDialog: AlertDialog by lazy {
        createLoading(R.layout.loading_dialog_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withdrawNavController = rememberNavController()
            Surface(color = MaterialTheme.colors.background) {
                WithdrawStepperComponent()
            }
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        loadData()
        showLoading(parent)
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun loadData() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        viewModel.setInitialStep()
        viewModel.getBikeList(joinedCommunityId)
        viewModel.getUserList(joinedCommunityId)
    }

    private fun showLoading(view: View?) {
        if (view != null) {
            viewModel.uiState.observe(this) { uiState ->
                loadingDialog.dismiss()
                when (uiState) {
                    is BikeWithdrawUiState.Error -> {
                        snackBarMaker(
                            uiState.message,
                            view.rootView
                        ).apply {
                            setBackgroundTint(
                                ContextCompat.getColor(
                                    applicationContext,
                                    R.color.red
                                )
                            )
                            show()
                        }
                    }
                    is BikeWithdrawUiState.Loading -> loadingDialog.show()
                    is BikeWithdrawUiState.Success -> loadingDialog.dismiss()
                }
            }
        }
    }

    @Composable
    fun WithdrawStepperComponent() {
        val cyclistList by viewModel.userList.observeAsState()
        val bikeList by viewModel.availableBikes.observeAsState()
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .height(152.dp)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_minimun))
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        modifier = Modifier.width(112.dp),
                        onClick = { backAction() },
                        content = {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp),
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = "Voltar"
                                )
                                Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
                                Text(
                                    text = "VOLTAR",
                                    textAlign = TextAlign.Left
                                )
                            }
                        }
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Divider()
                        Text(
                            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
                            text = stringResource(id = R.string.borrow_bike)
                        )
                        StepperComponents()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(ColorPallet.BackgroundGray)
                    .padding(top = dimensionResource(id = R.dimen.padding_medium))
            ) {
                WithdrawNaviationComponent(
                    vm = viewModel,
                    navController = withdrawNavController,
                    bikeList = bikeList ?: listOf(),
                    cyclistList = cyclistList ?: listOf(),
                    handleClick = selectStepperClick,
                    backToHome = { finish() }
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.navigateToPrevious()
    }

    private val selectStepperClick: (data: Any?) -> Unit = { data: Any? ->
        when (viewModel.uiStepConfig.value) {
            StepConfigType.SELECT_BIKE -> {
                withdrawNavController.navigate(WithdrawScreen.WithdrawSelectUser.route)
                val bike = data as Bike
                viewModel.setBike(bike)
                viewModel.navigateToNextStep()
            }
            StepConfigType.SELECT_USER -> {
                val user = data as User
                if (clickIsEnable(user)) {
                    withdrawNavController.navigate(WithdrawScreen.WithdrawConfirmation.route)
                    viewModel.setUser(user)
                    viewModel.navigateToNextStep()
                }
            }
            StepConfigType.CONFIRM_WITHDRAW -> {
                viewModel.confirmBikeWithdraw {
                    viewModel.setFinishAction()
                    withdrawNavController.navigate(WithdrawScreen.WithdrawFinishAction.route)
                }
            }
            StepConfigType.FINISHED_ACTION -> {
                loadData()
                viewModel.backToInitialState()
                withdrawNavController.navigate(WithdrawScreen.WithdrawSelectBike.route)
            }
            else -> {
                withdrawNavController.navigate(WithdrawScreen.WithdrawSelectBike.route)
            }
        }
    }

    @Composable
    private fun StepperComponents(
    ) {
        var iconStyleFirst = IconStyle(
            icon = painterResource(id = R.drawable.ic_bike),
            iconColor = ColorPallet.GreenTeal,
            lineColor = ColorPallet.GreenTeal,
        )
        var iconStyleSecond = IconStyle(icon = painterResource(id = R.drawable.ic_user_step_icon))
        var iconStyleThird = IconStyle(icon = painterResource(id = R.drawable.ic_confirm))
        val selectedView = viewModel.uiStepConfig.observeAsState()

        when (selectedView.value) {
            StepConfigType.SELECT_USER -> {
                iconStyleFirst = iconStyleFirst.copy(
                    backgroundColor = ColorPallet.GreenTeal,
                    iconColor = Color.White
                )
                iconStyleSecond = iconStyleSecond.copy(
                    iconColor = ColorPallet.GreenTeal,
                    lineColor = ColorPallet.GreenTeal
                )
            }
            StepConfigType.CONFIRM_WITHDRAW -> {
                iconStyleFirst = iconStyleFirst.copy(
                    backgroundColor = ColorPallet.GreenTeal,
                    iconColor = Color.White
                )

                iconStyleSecond = iconStyleSecond.copy(
                    iconColor = Color.White,
                    lineColor = ColorPallet.GreenTeal,
                    backgroundColor = ColorPallet.GreenTeal,
                )

                iconStyleThird = iconStyleThird.copy(
                    iconColor = ColorPallet.GreenTeal,
                    lineColor = ColorPallet.GreenTeal
                )
            }
            StepConfigType.FINISHED_ACTION -> {
                iconStyleFirst = iconStyleFirst.copy(
                    backgroundColor = ColorPallet.GreenTeal,
                    iconColor = Color.White
                )

                iconStyleSecond = iconStyleSecond.copy(
                    iconColor = Color.White,
                    lineColor = ColorPallet.GreenTeal,
                    backgroundColor = ColorPallet.GreenTeal,
                )

                iconStyleThird = iconStyleThird.copy(
                    iconColor = Color.White,
                    lineColor = ColorPallet.GreenTeal,
                    backgroundColor = ColorPallet.GreenTeal,
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            ItemLineSetStepperComponent(
                iconStyleFirst
            )
            ItemLineSetStepperComponent(
                iconStyleSecond
            )
            ItemStepperComponent(iconStyleThird)
        }
    }

    private fun backAction() {
        when (viewModel.uiStepConfig.value) {
            StepConfigType.SELECT_BIKE -> {
                finish()
            }
            StepConfigType.SELECT_USER -> {
                withdrawNavController.navigate(WithdrawScreen.WithdrawSelectBike.route)
                viewModel.navigateToPrevious()
            }
            StepConfigType.CONFIRM_WITHDRAW -> {
                withdrawNavController.navigate(WithdrawScreen.WithdrawSelectUser.route)
                viewModel.navigateToPrevious()
            }
            StepConfigType.FINISHED_ACTION -> {
                finish()
                startActivity(Intent(this, WithdrawStepper::class.java))
            }
        }
    }

    private fun clickIsEnable(
        user: User
    ) = (!user.hasActiveWithdraw && !user.isBlocked)

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun WithdrawStepperPreview() {
        BotaprarodarTheme {
            WithdrawStepperComponent()
        }
    }
}