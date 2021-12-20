package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.Typography
import coil.compose.rememberImagePainter

@Composable
fun WithdrawConfirmationComponent(bike: Bike, user: User) {
    val bikeRemember = remember { bike }
    val bikePhotoRemember = rememberImagePainter(data = bike.photoPath)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            painter = bikePhotoRemember,
            contentDescription = "Bike Image",
        )

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = bikeRemember.name!!,
                fontFamily = Typography.subtitle1.fontFamily,
                fontSize = 24.sp,
            )

            Row(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))) {
                Text(
                    text = "Ordem: ${bikeRemember.orderNumber}",
                    fontFamily = Typography.subtitle1.fontFamily,
                )

                Text(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                    text = "Série: ${bikeRemember.serialNumber}",
                )
            }
        }

        Divider()

        CardCyclist(user = user, {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WithdrawConfirmationPreview() {
    val bike = Bike(
        name = "Caloi XR245",
        orderNumber = 354785,
        serialNumber = "IT127B",
        photoPath = "https://cdn.pixabay.com/photo/2013/07/13/13/43/racing-bicycle-161449_1280.png"
    )

    val user = User(name = "Daniel Ferreira", telephone = "11 3333-1234", hasActiveWithdraw = false)
    BotaprarodarTheme {
        WithdrawConfirmationComponent(bike, user)
    }
}