package app.igormatos.botaprarodar.presentation.main.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.UsersListWithFilter
import app.igormatos.botaprarodar.presentation.user.UserActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun UsersScreen(users: List<User>) {
    val context = LocalContext.current

    UsersListWithFilter(users = users, handleClick = {
        val intent = UserActivity.setupActivity(
            context = context,
            user = it,
            currentCommunityUserList = arrayListOf()
        )
        context.startActivity(intent)
    })
}

@ExperimentalCoroutinesApi
@Preview
@Composable
fun UsersScreenPreview() {
    UsersScreen(users = emptyList())
}