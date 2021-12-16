package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectUserViewModel
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import app.igormatos.botaprarodar.presentation.components.ui.theme.Typography


@Composable
fun CyclistListComponent(vm: SelectUserViewModel = viewModel()) {
    val cyclistList = vm.userList.observeAsState()
//    stringResource(id = R.string.search_cyclist_name)
    var cyclistName by remember { mutableStateOf("") }


    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            value = cyclistName,
            onValueChange = { newText ->
                cyclistName = newText
            },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Image search")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search),
//            activeColor = Color.Transparent,
            label = {
                BasicText(
                    text = stringResource(id = R.string.search_cyclist_name),
//                    style = TextStyle(color = colorResource(id = R.color.green_teal))
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledBorderColor = colorResource(
                    id = R.color.text_gray),
                backgroundColor = Color.Cyan,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,

                )
        )

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

}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        CyclistListComponent()
    }
}