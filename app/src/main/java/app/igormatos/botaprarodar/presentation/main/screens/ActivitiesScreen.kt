package app.igormatos.botaprarodar.presentation.main.activities

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.components.WithdrawStepper
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.user.UserActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun ActivitiesScreen() {
    Column {
        CardsRow()
        SectionTitle()
        ActivitiesDate(date = "Dom, 24 de Janeiro")
    }
}

@Composable
fun SectionTitle() {
    Text(
        text = "Histórico de Atividades",
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = colorResource(id = R.color.text_gray),
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
}

@Composable
fun ActivitiesDate(date: String) {
    Text(
        text = date,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        color = colorResource(id = R.color.text_gray),
        modifier = Modifier.fillMaxWidth()
    )
}

@ExperimentalCoroutinesApi
@Composable
private fun CardsRow() {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(modifier = Modifier.padding(16.dp).clickable {
            context.startActivity(Intent(context, WithdrawStepper::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_withdraw_bike),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
        Card(modifier = Modifier.padding(16.dp).clickable {
            context.startActivity(Intent(context, ReturnBikeActivity::class.java))
        })  {
            Image(
                painter = painterResource(id = R.drawable.ic_return_bike),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
        Card(modifier = Modifier.padding(16.dp).clickable {
            context.startActivity(Intent(context, BikeFormActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_bike),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
        Card(modifier = Modifier.padding(16.dp).clickable {
            context.startActivity(Intent(context, UserActivity::class.java))
        }) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_user),
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivitiesScreenPreview() {
    ActivitiesScreen()
}