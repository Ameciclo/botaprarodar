package app.igormatos.botaprarodar.presentation.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.main.components.TopBar
import app.igormatos.botaprarodar.presentation.main.components.bottomBar.BottomBar
import app.igormatos.botaprarodar.presentation.main.components.bottomBar.BottomNavGraph
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun MainScreen(homeUiState: HomeUiState, users: List<User>, bikes: List<Bike>) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navHostController = navController) },
    ) {
        BottomNavGraph(
            navHostController = navController,
            uiState = homeUiState,
            users = users,
            bikes = bikes
        )
    }
}