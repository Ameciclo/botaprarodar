package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.BikeList
import app.igormatos.botaprarodar.presentation.components.ThreeStepper
import app.igormatos.botaprarodar.presentation.components.button.BackButton
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPallet
import app.igormatos.botaprarodar.presentation.returnbicycle.ui.theme.BotaprarodarTheme
import java.util.*

@Composable
fun ReturnBicyclePage(bikes: List<Bike>, handleClick: (Any?) -> Unit, backAction: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            BackButton(handleClick = backAction)
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
                    uiStepConfig = StepConfigType.SELECT_BIKE,
                    secondIcon = painterResource(id = R.drawable.ic_quiz)
                )
            }
            Box(modifier = Modifier.background(ColorPallet.BackgroundGray)) {
                BikeList(bikes = bikes, handleClick = handleClick)
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
        ReturnBicyclePage(bikes = listBikes, handleClick = {}, {})
    }
}