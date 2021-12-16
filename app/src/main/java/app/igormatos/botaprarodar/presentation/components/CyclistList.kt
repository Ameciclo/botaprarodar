package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme


@Composable
fun CyclistListComponent(vm: SelectUserViewModel = viewModel()) {
    val cyclistList = vm.userList.observeAsState()

    cyclistList.value?.let {
        LazyColumn(
            contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_minimun))) {
            items(it) { cyclist ->
                CardCyclist(user = cyclist) {
                    vm.setUser(cyclist)
                    vm.navigateToNextStep()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BotaprarodarTheme {
        CyclistListComponent()
    }
}