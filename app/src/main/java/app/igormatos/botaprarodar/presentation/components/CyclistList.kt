package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun CyclistListComponent(
    vm: WithdrawViewModel = viewModel(),
    joinedCommunityId: String,
    handleClick: () -> Unit = {}
) {
    val cyclistList = vm.userList.observeAsState()
    Column {
        SearchTextField(vm, joinedCommunityId)

        cyclistList.value?.let {
            LazyColumn(
                contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_minimun))
            ) {
                items(it) { cyclist ->
                    CardCyclist(user = cyclist) {
                        handleClick()
                        vm.setUser(cyclist)
                        vm.navigateToNextStep()
                    }
                    Box(contentAlignment = Alignment.BottomCenter) {
                        Divider(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
                            color = colorResource(id = R.color.auxiliar_text_gray),
                        )
                    }
                }
            }
        }
    }

}

@ExperimentalCoroutinesApi
@Composable
private fun SearchTextField(
    vm: WithdrawViewModel,
    joinedCommunityId: String,
) {
    var cyclistName by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
        value = cyclistName,
        onValueChange = { newText ->
            cyclistName = newText
            vm.filterBy(cyclistName, joinedCommunityId)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Image search"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),

        label = {
            BasicText(
                text = stringResource(id = R.string.search_cyclist_name)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorResource(id = R.color.background_input_search),
            disabledLabelColor = colorResource(id = R.color.text_gray),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}


@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        CyclistListComponent(joinedCommunityId = "")
    }
}