package app.igormatos.botaprarodar.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.ui.theme.BotaprarodarTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CyclistActions(
    val cyclistList: List<User> = listOf(),
    val handleClick: () -> Unit = {},
    val handleFilter: (cyclistName: String) -> List<User> = { listOf<User>() }
)

@ExperimentalCoroutinesApi
@Composable
fun CyclistListComponent(
    cyclistList: List<User> = listOf()
) {
    var auxiliarCyclistList by remember { mutableStateOf(cyclistList) }

    Column {
        SearchTextField { cyclistName ->
            auxiliarCyclistList =
                cyclistList.filter { it.name?.lowercase() == cyclistName.lowercase() }
        }

        UsersList(auxiliarCyclistList)
    }

}

@Composable
fun UsersList(
    users: List<User>
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_minimun))
    ) {
        items(users) { cyclist ->
            CardCyclist(user = cyclist)
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

@ExperimentalCoroutinesApi
@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BotaprarodarTheme {
        CyclistListComponent()
    }
}