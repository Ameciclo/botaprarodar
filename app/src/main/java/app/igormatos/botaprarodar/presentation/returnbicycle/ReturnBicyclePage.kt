package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.BikeList
import app.igormatos.botaprarodar.presentation.components.ThreeStepper
import app.igormatos.botaprarodar.presentation.components.button.BackButton
import app.igormatos.botaprarodar.presentation.components.navigation.return_bike.ReturnNavigationComponent
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@Composable
fun ReturnBicyclePage(
    viewModel: ReturnBicycleViewModel,
    navHostController: NavHostController,
    bikes: List<Bike>,
    handleClick: (Any?) -> Unit,
    backAction: () -> Unit
) {
    val uiStepConfig by viewModel.uiStep.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BackButton(handleClick = { backAction() })
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
                    bikeList = bikes,
                    navController = navHostController,
                    handleClick = handleClick,
                    backToHome = backAction
                )
            }
        }
    }
}

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
//        ReturnBicyclePage(bikes = listBikes, handleClick = {}, {})
    }
}