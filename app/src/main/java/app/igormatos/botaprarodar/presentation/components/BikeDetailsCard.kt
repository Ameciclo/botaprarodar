package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import coil.compose.rememberImagePainter

@Composable
fun BikeDetailsCard(bike: Bike) {
    val bikePhoto = rememberImagePainter(data = bike.photoPath)

    Column(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            painter = bikePhoto,
            contentDescription = "Bike Image",
        )

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                .padding(
                    top = dimensionResource(
                        id = R.dimen.padding_small
                    )
                )
        ) {
            Text(
                text = "${bike.name}",
                fontSize = 24.sp,
                color = colorResource(id = R.color.text_gray)
            )

            Row(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))) {
                Text(
                    text = "Ordem: ${bike.orderNumber}",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_gray)
                )

                Text(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                    text = "Série: ${bike.serialNumber}",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_gray)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BikeDetailsCardPreview() {
    val bike = Bike(
        name = "Caloi XR245",
        orderNumber = 354785,
        serialNumber = "IT127B",
        photoPath = "https://cdn.pixabay.com/photo/2013/07/13/13/43/racing-bicycle-161449_1280.png"
    )

    BotaprarodarTheme {
        BikeDetailsCard(bike)
    }
}