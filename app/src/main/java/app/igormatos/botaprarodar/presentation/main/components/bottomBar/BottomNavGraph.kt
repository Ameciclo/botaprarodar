package app.igormatos.botaprarodar.presentation.main.components.bottomBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import app.igormatos.botaprarodar.presentation.main.activities.ActivitiesScreen
import app.igormatos.botaprarodar.presentation.main.screens.BikesScreen
import app.igormatos.botaprarodar.presentation.main.screens.HomeScreen
import app.igormatos.botaprarodar.presentation.main.screens.UsersScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable
fun BottomNavGraph(
    navHostController: NavHostController,
    uiState: HomeUiState,
    users: List<User>,
    bikes: List<Bike>,
) {
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) { HomeScreen(uiState) }
        composable(BottomBarScreen.Activities.route) { ActivitiesScreen() }
        composable(BottomBarScreen.Users.route) { UsersScreen(users = users) }
        composable(BottomBarScreen.Bikes.route) { BikesScreen(bikes = bikes) }
    }
}