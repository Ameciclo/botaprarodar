package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.badges.BikeBadge
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.Typography
import coil.compose.rememberImagePainter

@Composable
fun CardBikeComponent(bike: Bike, showBadge: Boolean = false, handleClick: () -> Unit) {
    val rememberBike = remember<Bike> { bike }
    val rememberImage = rememberImagePainter(data = rememberBike.photoPath)

    Card(
        modifier = Modifier
            .height(112.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { handleClick() },
        backgroundColor = colorResource(id = R.color.white),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Row {

            Box {
                Image(
                    modifier = Modifier.size(width = 156.dp, height = 96.dp),
                    painter = rememberImage,
                    contentDescription = stringResource(id = R.string.bicycle_image_view_descripition),
                    contentScale = ContentScale.Crop
                )

                if (showBadge) {
                    if (rememberBike.inUse) {
                        BikeBadge(
                            stringResource(R.string.bike_in_use),
                            colorResource(id = R.color.badge_yellow),
                            colorResource(id = R.color.text_gray)
                        )
                    } else if (!rememberBike.inUse && rememberBike.devolutions?.size!! > 0) {
                        BikeBadge(
                            stringResource(R.string.bike_returned),
                            colorResource(id = R.color.badge_green),
                            colorResource(id = R.color.white)
                        )
                    }
                }
            }


            Box(Modifier.padding(horizontal = 16.dp)) {
                Column(
                    Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "ORDEM: ${rememberBike.orderNumber}",
                        fontStyle = Typography.subtitle1.fontStyle,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.gray_6)
                    )

                    Text(
                        text = rememberBike.name ?: stringResource(id = R.string.bike_name),
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.gray_6)
                    )

                    Text(
                        text = "SÉRIE: ${rememberBike.serialNumber}",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.gray_6)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 328)
@Composable
private fun DefaultPreview() {
    val bike = Bike(
        name = "Caloi XR245",
        orderNumber = 354785,
        serialNumber = "IT127B",
        photoPath = "https://cdn.pixabay.com/photo/2013/07/13/13/43/racing-bicycle-161449_1280.png",
        inUse = false,
        devolutions = mutableListOf()
    )
    BotaprarodarTheme {
        CardBikeComponent(bike = bike, showBadge = true, handleClick = {})
    }
}