package app.igormatos.botaprarodar.presentation.components

import android.app.Activity
import android.content.Context
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeConfirmationViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.BikeWithdrawViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPalet
import com.brunotmgomes.ui.extensions.createLoading
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WithdrawStepper : ComponentActivity() {
    private lateinit var joinedCommunityId: String
    private val selectBikeViewModel: SelectBikeViewModel by viewModel()
    private val selectUserViewModel: SelectUserViewModel by viewModel()
    private val bikeConfirmationViewModel: BikeConfirmationViewModel by viewModel()
    private val stepperViewModel: BikeWithdrawViewModel by viewModel()
    private val preferencesModule: SharedPreferencesModule by inject()
    private val loadingDialog: AlertDialog by lazy {
        createLoading(R.layout.loading_dialog_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotaprarodarTheme {
                Surface(color = MaterialTheme.colors.background) {
                    WithdrawStepperComponent()
                }
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        initUI()
        return super.onCreateView(name, context, attrs)
    }

    private fun initUI() {
        joinedCommunityId = preferencesModule.getJoinedCommunity().id
        selectBikeViewModel.setInitialStep()
        selectBikeViewModel.getBikeList(joinedCommunityId)
        selectUserViewModel.getUserList(joinedCommunityId)
    }

    @Composable
    fun WithdrawStepperComponent() {
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .height(152.dp)
                    .padding(top = dimensionResource(id = R.dimen.padding_minimun))
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
                        stepperComponents()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(ColorPalet.BackgroundGray)
            ) {
                SelectedView()
            }
        }
    }

    @Composable
    private fun stepperComponents(
    ) {
        var iconStyleFirst: IconStyle = IconStyle(
            icon = painterResource(id = R.drawable.ic_bike),
            iconColor = ColorPalet.GreenTeal,
            lineColor = ColorPalet.GreenTeal,
        )
        var iconStyleSecond: IconStyle =
            IconStyle(icon = painterResource(id = R.drawable.ic_user_step_icon))
        var iconStyleThird: IconStyle =
            IconStyle(icon = painterResource(id = R.drawable.ic_confirm))
        val selectedView = stepperViewModel.uiState.observeAsState()
        when (selectedView.value) {
            StepConfigType.SELECT_USER -> {
                iconStyleFirst = iconStyleFirst.copy(
                    backgroundColor = ColorPalet.GreenTeal,
                    iconColor = Color.White
                )

                iconStyleSecond = iconStyleSecond.copy(
                    iconColor = ColorPalet.GreenTeal,
                    lineColor = ColorPalet.GreenTeal
                )

                iconStyleThird = iconStyleThird
            }
            StepConfigType.CONFIRM_WITHDRAW -> {
                iconStyleFirst = iconStyleFirst.copy(
                    backgroundColor = ColorPalet.GreenTeal,
                    iconColor = Color.White
                )

                iconStyleSecond = iconStyleSecond.copy(
                    iconColor = Color.White,
                    lineColor = ColorPalet.GreenTeal,
                    backgroundColor = ColorPalet.GreenTeal,
                )

                iconStyleThird = iconStyleThird.copy(
                    iconColor = ColorPalet.GreenTeal,
                    lineColor = ColorPalet.GreenTeal
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
        when (stepperViewModel.uiState.value) {
            StepConfigType.SELECT_BIKE -> finish()
            else -> stepperViewModel.navigateToPrevious()
        }
    }

    @Composable
    fun SelectedView() {
        val activity = (LocalContext.current as? Activity)
        val selectedView = stepperViewModel.uiState.observeAsState()
        when (selectedView.value) {
            StepConfigType.SELECT_BIKE -> BikeListComponent()
            StepConfigType.SELECT_USER -> CyclistListComponent(joinedCommunityId = joinedCommunityId)
            StepConfigType.CONFIRM_WITHDRAW -> WithdrawConfirmationComponent(
                bike = bikeConfirmationViewModel.bike,
                user = bikeConfirmationViewModel.user,
                handleClick = {
                    bikeConfirmationViewModel.confirmBikeWithdraw()
                    activity?.finish()
                }
            )
            else -> BikeListComponent()
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun WithdrawStepperPreview() {
        BotaprarodarTheme {
            WithdrawStepperComponent()
        }
    }
}

