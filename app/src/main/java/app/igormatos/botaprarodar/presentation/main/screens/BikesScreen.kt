package app.igormatos.botaprarodar.presentation.main.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.BikeList
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun BikesScreen(bikes: List<Bike>) {
    BikeList(bikes = bikes) { }
}

@ExperimentalCoroutinesApi
@Preview(showSystemUi = true)
@Composable
fun BikeScreenPreview() {
    BikesScreen(bikes = emptyList())
}