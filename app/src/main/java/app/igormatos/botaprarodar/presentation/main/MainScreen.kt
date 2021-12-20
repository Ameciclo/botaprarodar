package app.igormatos.botaprarodar.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@ExperimentalMaterialApi
@Composable
fun MainScreen(homeUiState: HomeUiState) {
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomBar(navHostController = navController)}) {
        BottomNavGraph(navHostController = navController, uiState = homeUiState)
    }
}

@Composable
fun BottomBar(navHostController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Activities,
        BottomBarScreen.Users,
        BottomBarScreen.Bikes
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigation(backgroundColor = Color.White) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    val backgroundColor = if (selected) {
        Color(0xFF036867)
    } else {
        Color.White
    }
    BottomNavigationItem(
        modifier = Modifier.background(backgroundColor),
        label = { Text(text = screen.title) },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "Navigation icon")
        },
        selected = selected,
        onClick = { navHostController.navigate(screen.route) },
        selectedContentColor = Color.White,
        unselectedContentColor = Color(0xFF515151)
    )
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navHostController = rememberNavController())
}