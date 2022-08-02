package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.components.BikeDetailsCard
import app.igormatos.botaprarodar.presentation.components.CardCyclist
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme

@Composable
fun ReturnBicycleResume(
    isLoading: State<Boolean>,
    bicycle: Bike,
    onConfirmDevolution: () -> Unit
) {
    val lastWithdraw = bicycle.getLastWithdraw()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_card_user_item_gray))
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                )
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium))
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.confirm_return)
            )

            Box(
                modifier = Modifier.clip(RoundedCornerShape(2))
            ) {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white)),
                ) {
                    BikeDetailsCard(bike = bicycle)

                    Divider()

                    lastWithdraw?.user?.let {
                        CardCyclist(
                            user = it,
                            bikeLastWithdraw = lastWithdraw.date?.formattedDate() ?: "",
                            handleClick = {})
                    }
                }
            }

            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

            Button(
                enabled = !isLoading.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.height_48)),
                onClick = {
                    onConfirmDevolution()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green_teal))
            ) {
                Text(
                    text = stringResource(id = R.string.return_bike_confirm_devolution).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.gray_1)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ReturnBicycleResumePreview() {
    val bicycle = Bike(
        name = "Caloi XR245",
        orderNumber = 354785,
        serialNumber = "IT127B",
        photoPath = "https://cdn.pixabay.com/photo/2013/07/13/13/43/racing-bicycle-161449_1280.png"
    )

    val user = User(name = "Daniel Ferreira", telephone = "11 3333-1234", hasActiveWithdraw = false)

    val lastWithdraw = Withdraws("1", "01/08/2022 16:09:28", user)

    bicycle.withdraws = mutableListOf(lastWithdraw)

    BotaprarodarTheme {
        ReturnBicycleResume(remember { mutableStateOf(false) }, bicycle) {}
    }
}