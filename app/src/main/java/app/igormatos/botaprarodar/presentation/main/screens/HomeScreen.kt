package app.igormatos.botaprarodar.presentation.main.screens

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.components.ActionCard
import app.igormatos.botaprarodar.presentation.components.WithdrawStepper
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.user.UserActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun HomeScreen(homeUiState: HomeUiState) {
    val columnAttributes: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    Column(modifier = columnAttributes) {
        Cards()
        Spacer(modifier = Modifier.height(32.dp))
        BikesCounter(
            homeUiState.totalBikes,
            homeUiState.totalBikesWithdraw,
            homeUiState.totalBikesAvailable
        )
    }
}

@Composable
private fun BikesCounter(totalBikes: Int, totalWithdrawBikes: Int, totalAvailableBikes: Int) {
    val modifier = Modifier
        .border(1.dp, Color(0xFF3C3C3C), RoundedCornerShape(4.dp))
        .fillMaxWidth()
        .padding(16.dp)

    Surface(modifier) {
        Column(Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Total de bicicletas".uppercase(),
                    fontSize = 16.sp
                )
                Text(
                    text = "$totalBikes",
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
                    text = "$totalWithdrawBikes",
                    fontSize = 16.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
    Card(
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
                text = "$totalAvailableBikes",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun Cards() {
    val context = LocalContext.current
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionCard(
            title = "Emprestar\nBicicleta",
            painterResource(id = R.drawable.ic_withdraw_bike)
        ) {
            context.startActivity(Intent(context, WithdrawStepper::class.java))
        }
        ActionCard(title = "Devolver\nBicicleta", painterResource(id = R.drawable.ic_return_bike)) {
            context.startActivity(Intent(context, ReturnBikeActivity::class.java))
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        ActionCard(title = "Cadastrar\nBicicleta", painterResource(id = R.drawable.ic_add_bike)) {
            context.startActivity(Intent(context, BikeFormActivity::class.java))
        }
        ActionCard(title = "Cadastrar\nUsuária", painterResource(id = R.drawable.ic_add_user)) {
            context.startActivity(Intent(context, UserActivity::class.java))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(HomeUiState(50, 30, 20, bikes = emptyList()))
}

@Preview(showBackground = true)
@Composable
fun BikesCounterPreview() {
    BikesCounter(50, 20, 30)
}