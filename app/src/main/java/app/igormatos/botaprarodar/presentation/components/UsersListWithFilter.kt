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

@ExperimentalCoroutinesApi
@Composable
fun UsersListWithFilter(users: List<User>, handleClick: (User) -> Unit) {
    var filteredList by remember { mutableStateOf(users) }
    Column {
        SearchTextField(handleFilter = { name ->
            filteredList =
                users.filter { user ->
                    user.name!!.lowercase().contains(name.lowercase())
                }
        })

        UsersList(filteredList, handleClick = handleClick)
    }
}


@Composable
fun UsersList(users: List<User>, handleClick: (User) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_minimun))
    ) {
        items(users) { user ->
            CardCyclist(user = user, handleClick = handleClick)
            Box(contentAlignment = Alignment.BottomCenter) {
                Divider(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(id = R.dimen.padding_small)
                    ),
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
        UsersListWithFilter(users = emptyList(), handleClick = {})
    }
}