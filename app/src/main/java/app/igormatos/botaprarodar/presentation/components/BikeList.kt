package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme

@Composable
fun BikeListComponent(vm: SelectBikeViewModel = viewModel()) {
    val bikeList = vm.availableBikes.observeAsState()
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (bikeList.value != null) {
            items(bikeList.value!!) { bike ->
                CardBikeComponent(bike = bike) {
                    vm.setBike(bike)
                    vm.navigateToNextStep()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        BikeListComponent()
    }
}