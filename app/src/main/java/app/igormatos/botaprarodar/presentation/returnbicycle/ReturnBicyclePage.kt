package app.igormatos.botaprarodar.presentation.returnbicycle

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.presentation.components.ThreeStepper
import app.igormatos.botaprarodar.presentation.components.button.BackButton
import app.igormatos.botaprarodar.presentation.components.navigation.return_bike.ReturnNavigationComponent
import app.igormatos.botaprarodar.presentation.components.navigation.return_bike.ReturnScreen
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun ReturnBicyclePage(
    viewModel: ReturnBicycleViewModel = viewModel(),
    finish: () -> Unit,
) {
    val returnBicycleNavController: NavHostController = rememberNavController()

    val uiStepConfig by viewModel.uiStep.observeAsState()

    val bikes by viewModel.bikesAvailable.observeAsState()

    val user by viewModel.userHolder.observeAsState()

    val localContext = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BackButton(handleClick = {
                backAction(
                    viewModel = viewModel,
                    navController = returnBicycleNavController,
                    context = localContext,
                    finish = finish
                )
            })
            Divider()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = stringResource(id = R.string.return_bike))
                ThreeStepper(
                    uiStepConfig = uiStepConfig,
                    secondIcon = painterResource(id = R.drawable.ic_quiz)
                )
            }
            Box(modifier = Modifier.background(ColorPallet.BackgroundGray)) {
                ReturnNavigationComponent(
                    vm = viewModel,
                    bikeList = bikes ?: emptyList(),
                    navController = returnBicycleNavController,
                    handleClick = {
                        selectClickStepper(
                            viewModel = viewModel,
                            navController = returnBicycleNavController,
                            data = it
                        )
                    },
                    backToHome = finish
                )
            }
        }
    }
}

@ExperimentalCoroutinesApi
private fun selectClickStepper(
    viewModel: ReturnBicycleViewModel,
    navController: NavHostController,
    data: Any?
): Unit {
    when (viewModel.uiStep.value) {
        StepConfigType.SELECT_BIKE -> {
            navController.navigate(ReturnScreen.ReturnQuiz.route)
            val bicycle = data as Bike
            viewModel.setBike(bicycle)
            bicycle.withdrawToUser?.let { userId -> viewModel.getUserBy(userId) }
            viewModel.navigateToNextStep()
        }
        StepConfigType.QUIZ -> {
            navController.navigate(ReturnScreen.ReturnConfirmation.route)
            viewModel.setQuiz(data as Quiz)
            viewModel.navigateToNextStep()
        }

        StepConfigType.CONFIRM_DEVOLUTION -> {
            navController.navigate(ReturnScreen.ReturnFinishAction.route)
            viewModel.navigateToNextStep()
        }

        StepConfigType.FINISHED_ACTION -> {
            navController.navigate(ReturnScreen.ReturnSelectBike.route)
            //viewModel.setInitialStep()
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
private fun backAction(
    viewModel: ReturnBicycleViewModel,
    navController: NavHostController,
    context: Context,
    finish: () -> Unit
) {

    when (viewModel.uiStep.value) {
        StepConfigType.SELECT_BIKE -> {
            finish()
        }
        StepConfigType.QUIZ -> {
            navController.navigate(ReturnScreen.ReturnSelectBike.route)
            viewModel.navigateToPrevious()
        }
        StepConfigType.CONFIRM_DEVOLUTION -> {
            navController.navigate(ReturnScreen.ReturnQuiz.route)
            viewModel.navigateToPrevious()
        }
        StepConfigType.FINISHED_ACTION -> {
            finish()
            context.startActivity(Intent(context, ReturnBicycleActivity::class.java))
        }
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ReturnBicycleActivityPreview() {
    val listBikes = listOf(
        Bike(
            id = "",
            isAvailable = true,
            inUse = false,
            name = "New Bicycle",
            orderNumber = 1010,
            serialNumber = "New Serial",
            createdDate = Date().toString()
        ),
        Bike(
            id = "",
            isAvailable = true,
            inUse = false,
            name = "New Bicycle",
            orderNumber = 1010,
            serialNumber = "New Serial",
            createdDate = Date().toString()
        ),
        Bike(
            id = "",
            isAvailable = true,
            inUse = false,
            name = "New Bicycle",
            orderNumber = 1010,
            serialNumber = "New Serial",
            createdDate = Date().toString()
        )
    )

    BotaprarodarTheme {
        ReturnBicyclePage(finish = {})
    }
}