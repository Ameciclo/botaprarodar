package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import coil.compose.rememberImagePainter
import java.util.*

@Composable
fun WithdrawConfirmationComponent(bike: Bike, user: User, handleClick: () -> Unit) {
    val bikeRemember = remember { bike }
    val bikePhotoRemember = rememberImagePainter(data = bike.photoPath)
    val today = Date()
    val todayFormatted = formattedDate("dd MMM yyyy").format(today).replace(" ", " de ")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.background_card_user_item_gray))
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                )
        ) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(2))
            ) {
                Column(
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white)),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop,
                        painter = bikePhotoRemember,
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
                            text = bikeRemember.name!!,
                            fontSize = 24.sp,
                            color = colorResource(id = R.color.text_gray)
                        )

                        Row(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))) {
                            Text(
                                text = "Ordem: ${bikeRemember.orderNumber}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.text_gray)
                            )

                            Text(
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                                text = "Série: ${bikeRemember.serialNumber}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.text_gray)
                            )
                        }
                    }

                    Divider()

                    CardCyclist(user = user, bikeLastWithdraw = todayFormatted, handleClick = {})
                }
            }

            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.height_48)),
                onClick = handleClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green_teal))
            ) {
                Text(
                    text = stringResource(id = R.string.confirm_withdraw).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.gray_1)
                )
            }
        }
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
        WithdrawConfirmationComponent(bike, user, {})
    }
}