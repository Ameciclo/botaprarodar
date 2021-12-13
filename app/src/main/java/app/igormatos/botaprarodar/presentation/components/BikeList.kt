package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@Composable
fun BikeListComponent(bikeList: List<Bike>, vm: SelectBikeViewModel) {
    val bikeListRem = remember { bikeList }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(bikeListRem) { bike ->
            CardBikeComponent(bike = bike) {
                vm.setBike(bike)
                vm.navigateToNextStep()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val bikeList = listOf(
        Bike(name = "Caloi XR245", orderNumber = 354785, serialNumber = "IT127B"),
        Bike(name = "Caloi XR245", orderNumber = 354785, serialNumber = "IT127B"),
    )
    BotaprarodarTheme {
        BikeListComponent(bikeList = bikeList, viewModel())
    }
}