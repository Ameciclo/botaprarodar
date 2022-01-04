package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme

@Composable
fun BikeListComponent(vm: SelectBikeViewModel = viewModel()) {
    val bikeList = vm.availableBikes.observeAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
            text = stringResource(id = R.string.select_bike)
        )

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
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        BikeListComponent()
    }
}