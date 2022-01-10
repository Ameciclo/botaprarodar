package app.igormatos.botaprarodar.presentation.main.components.bottomBar

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import app.igormatos.botaprarodar.presentation.main.activities.ActivitiesScreen
import app.igormatos.botaprarodar.presentation.main.screens.HomeScreen

@Composable
fun BottomNavGraph(navHostController: NavHostController, uiState: HomeUiState) {
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) { HomeScreen(uiState) }
        composable(BottomBarScreen.Activities.route) { ActivitiesScreen()}
        composable(BottomBarScreen.Users.route) { Text(text = "Users") }
        composable(BottomBarScreen.Bikes.route) { Text(text = "Bike") }
    }
}