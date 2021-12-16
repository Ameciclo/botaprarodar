package app.igormatos.botaprarodar.presentation.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.presentation.main.home.HomeScreen

@ExperimentalMaterialApi
@Composable
fun BottomNavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) { HomeScreen("João") }
        composable(BottomBarScreen.Activities.route) { Text(text = "Activities") }
        composable(BottomBarScreen.Users.route) { Text(text = "Users") }
        composable(BottomBarScreen.Bikes.route) { Text(text = "Bike") }
    }
}