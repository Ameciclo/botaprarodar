package app.igormatos.botaprarodar.presentation.main.components.bottomBar

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.components.CyclistActions
import app.igormatos.botaprarodar.presentation.components.CyclistListComponent
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import app.igormatos.botaprarodar.presentation.main.activities.ActivitiesScreen
import app.igormatos.botaprarodar.presentation.main.screens.HomeScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun BottomNavGraph(
    navHostController: NavHostController, uiState: HomeUiState, cyclistActions: CyclistActions
) {
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) { HomeScreen(uiState) }
        composable(BottomBarScreen.Activities.route) { ActivitiesScreen() }
        composable(BottomBarScreen.Users.route) {
            CyclistListComponent(
                cyclistList = cyclistActions.cyclistList,
                handleFilter = cyclistActions.handleFilter
            )
        }
        composable(BottomBarScreen.Bikes.route) { Text(text = "Bike") }
    }
}