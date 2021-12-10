package app.igormatos.botaprarodar.presentation.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.Typography

//class CardBike : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            BotaprarodarTheme {
//                Surface(color = MaterialTheme.colors.background) {
//                    CardBikeComponent()
//                }
//            }
//        }
//    }
//}

@Composable
fun CardBikeComponent(bike: Bike) {
    val bike = remember<Bike> { bike }

    Card(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth(),
        backgroundColor = colorResource(id = R.color.white),
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Row {
            Image(
                modifier = Modifier.size(width = 156.dp, height = 96.dp),
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = stringResource(id = R.string.bicycle_image_view_descripition),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "ORDEM: ${bike.orderNumber}",
                    fontStyle = Typography.subtitle1.fontStyle,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_6)
                )

                Text(
                    text = bike.name ?: stringResource(id = R.string.bike_name),
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.gray_6)
                )

                Text(
                    text = "SÉRIE: ${bike.serialNumber}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.gray_6)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 328)
@Composable
private fun DefaultPreview() {
    val bike = Bike(name = "Caloi XR245", orderNumber = 354785, serialNumber = "IT127B")
    BotaprarodarTheme {
        CardBikeComponent(bike)
    }
}