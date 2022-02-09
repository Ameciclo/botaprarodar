package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import coil.compose.rememberImagePainter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@Composable
fun WithdrawConfirmationComponent(
    vm: WithdrawViewModel = viewModel(),
    handleClick: (Any?) -> Unit
) {
    val bike = vm.bike.observeAsState()
    val user = vm.user.observeAsState()
    val bikePhoto = rememberImagePainter(data = bike.value?.photoPath)
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
                .verticalScroll(rememberScrollState())
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
                            text = "${bike.value?.name}",
                            fontSize = 24.sp,
                            color = colorResource(id = R.color.text_gray)
                        )

                        Row(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small))) {
                            Text(
                                text = "Ordem: ${bike.value?.orderNumber}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.text_gray)
                            )

                            Text(
                                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                                text = "Série: ${bike.value?.serialNumber}",
                                fontSize = 14.sp,
                                color = colorResource(id = R.color.text_gray)
                            )
                        }
                    }

                    Divider()

                    CardCyclist(
                        user = user.value!!,
                        bikeLastWithdraw = todayFormatted,
                        handleClick = {})
                }
            }

            Spacer(modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_small)))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.height_48)),
                onClick = { handleClick(null) },
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

@ExperimentalCoroutinesApi
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
        WithdrawConfirmationComponent {}
    }
}