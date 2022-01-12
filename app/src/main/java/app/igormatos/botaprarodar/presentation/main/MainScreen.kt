package app.igormatos.botaprarodar.presentation.main

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.CyclistActions
import app.igormatos.botaprarodar.presentation.main.components.TopBar
import app.igormatos.botaprarodar.presentation.main.components.bottomBar.BottomBar
import app.igormatos.botaprarodar.presentation.main.components.bottomBar.BottomNavGraph

@Composable
fun MainScreen(homeUiState: HomeUiState, cyclistActions: CyclistActions) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBar() },
        bottomBar = { BottomBar(navHostController = navController) },
    ) {
        BottomNavGraph(
            navHostController = navController,
            uiState = homeUiState,
            cyclistActions = cyclistActions,

            )
    }
}