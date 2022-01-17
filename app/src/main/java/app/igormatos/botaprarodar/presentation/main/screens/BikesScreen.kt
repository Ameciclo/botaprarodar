package app.igormatos.botaprarodar.presentation.main.screens

import android.content.Intent
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.components.BikeList
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun BikesScreen(bikes: List<Bike>) {
    val context = LocalContext.current
    BikeList(bikes = bikes) {
        val intent = Intent(context, BikeFormActivity::class.java)
        intent.putExtra(BikeFormActivity.BIKE_EXTRA, it)
        context.startActivity(intent)
    }
}

@ExperimentalCoroutinesApi
@Preview(showSystemUi = true)
@Composable
fun BikeScreenPreview() {
    BikesScreen(bikes = emptyList())
}