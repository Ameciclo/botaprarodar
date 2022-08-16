package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun BikeList(bikes: List<Bike>, handleClick: (Bike) -> Unit) {
    Column(
        modifier = Modifier.padding(
            bottom = dimensionResource(id = R.dimen.padding_xxlarge_plus
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.padding_medium
                )
            ),
            text = stringResource(id = R.string.select_bike)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(bikes) { bike ->
                CardBikeComponent(bike = bike) {
                    handleClick(bike)
                }
            }
        }
    }
}

@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        BikeList(bikes = emptyList(), handleClick = {})
    }
}