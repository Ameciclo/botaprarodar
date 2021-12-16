package app.igormatos.botaprarodar.presentation.main.home

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.bikewithdraw.BikeWithdrawActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.user.UserActivity

@ExperimentalMaterialApi
@Composable
fun HomeScreen(name: String) {
    val columnAttributes: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    Column(modifier = columnAttributes) {
        Text(text = "Olá $name", modifier = Modifier.padding(bottom = 16.dp))
        Cards()
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            modifier = Modifier
                .border(1.dp, Color(0xFF3C3C3C), RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Total de bicicletas".uppercase(),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "50",
                        fontSize = 16.sp
                    )
                }
                Divider(Modifier.padding(vertical = 8.dp))
                Row(horizontalArrangement = SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Emprestadas".uppercase(),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "30",
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            onClick = {},
            elevation = 4.dp,
            backgroundColor = colorResource(id = R.color.colorPrimary)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Bicicletas disponíveis".uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Text(
                    text = "20",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Cards() {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionCard(
            title = "Emprestar\nBicleta",
            painterResource(id = R.drawable.ic_withdraw_bike)
        ) {
            context.startActivity(Intent(context, BikeWithdrawActivity::class.java))
        }
        ActionCard(title = "Devolver\nBicleta", painterResource(id = R.drawable.ic_return_bike)) {
            context.startActivity(Intent(context, ReturnBikeActivity::class.java))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionCard(title = "Cadastrar\nBicleta", painterResource(id = R.drawable.ic_add_bike)) {
            context.startActivity(Intent(context, BikeFormActivity::class.java))
        }
        ActionCard(title = "Cadastrar\nUsuária", painterResource(id = R.drawable.ic_add_user)) {
            context.startActivity(Intent(context, UserActivity::class.java))
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ActionCard(title: String, icon: Painter, onClickHandler: () -> Unit = {}) {
    Card(
        elevation = 3.dp,
        modifier = Modifier.width(170.dp),
        onClick = onClickHandler
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = icon,
                contentDescription = "",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(40.dp)
            )
            Text(
                text = title.uppercase(),
                fontSize = 18.sp,
                color = colorResource(id = R.color.text_gray),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(name = "João")
}