package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.utils.formattedDate
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import coil.compose.rememberImagePainter
import java.util.*

@Composable
fun CardCyclist(user: User?, bikeLastWithdraw: String = "", handleClick: () -> Unit) {
    val rememberUserPhoto = rememberImagePainter(data = user?.profilePictureThumbnail)

    Box(
        modifier = Modifier
            .height(92.dp)
            .fillMaxWidth()
            .clickable(
                enabled = clickIsEnable(user, bikeLastWithdraw)
            ) {
                handleClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(50)),
                    painter = rememberUserPhoto,
                    contentDescription = "User Image",
                    contentScale = ContentScale.FillBounds,
                )

                Column(
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium)),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_small)),
                        text = "${user?.name}",
                        fontSize = 20.sp,
                    )
                    if (bikeLastWithdraw.trim().isNotEmpty()) {
                        TextCyclistSubtitle(
                            stringResource(
                                id = R.string.bike_withdraw_date,
                                bikeLastWithdraw
                            )
                        )
                    } else if (user != null && user.hasActiveWithdraw) {
                        TextCyclistSubtitle(
                            text = stringResource(id = R.string.active_withdraw),
                            textColor = colorResource(
                                id = R.color.information_blue_color
                            )
                        )
                    } else {
                        TextCyclistSubtitle(text = "${user?.telephoneHide4Chars()}")
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = dimensionResource(id = R.dimen.padding_small)),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user_menu),
                        contentDescription = "User Menu"
                    )
                }
            }
        }
    }
}

@Composable
private fun clickIsEnable(
    user: User?,
    bikeLastWithdraw: String
) = (user != null &&
        (!user.hasActiveWithdraw && !user.isBlocked)
        && bikeLastWithdraw.isNullOrEmpty())

@Composable
private fun TextCyclistSubtitle(
    text: String,
    textColor: Color = colorResource(id = R.color.gray_3)
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = textColor
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val user = User(name = "Daniel Ferreira", telephone = "11 3333-1234", hasActiveWithdraw = false)
    val today = Date()
    val todayFormatted = formattedDate("dd MMM yyyy").format(today).replace(" ", " de ")

    BotaprarodarTheme {
        CardCyclist(user = user, bikeLastWithdraw = todayFormatted, handleClick = {})
    }
}