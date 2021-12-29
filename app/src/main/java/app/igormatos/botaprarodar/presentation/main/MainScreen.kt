package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun MainScreen(homeUiState: HomeUiState) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        topBar = { TopBar(onClickMenuHandler = suspend { scaffoldState.drawerState.open() }) },
        bottomBar = { BottomBar(navHostController = navController) },
        scaffoldState = scaffoldState,
        drawerContent = { Text("Menu") }
    ) { BottomNavGraph(navHostController = navController, uiState = homeUiState) }
}

@Composable
fun TopBar(onClickMenuHandler: suspend () -> Unit) {
    TopAppBar(
        title = { Text("Bota Pra Rodar") },
        navigationIcon = { MenuIcon(onClickMenuHandler) },
        actions = { LogoutButton() },
        backgroundColor = Color.White,
        modifier = Modifier.border(
            border = BorderStroke(0.dp, Color.Transparent),
            shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
        )
    )
}

@Composable
private fun LogoutButton() {
    val context = LocalContext.current
    IconButton(onClick = { logout(context = context) }) {
        Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
    }
}

private fun logout(context: Context) {
    FirebaseAuth.getInstance().signOut()

    val preferencesModule = SharedPreferencesModule(context)
    preferencesModule.clear()

    val intent = Intent(context, LoginActivity::class.java)
    context.startActivity(intent)
}

@Composable
private fun MenuIcon(onClickMenuHandler: suspend () -> Unit) {
    val scope = rememberCoroutineScope()
    IconButton(onClick = {
        scope.launch { onClickMenuHandler() }
    }) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "")
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

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    val rememberScaffoldState = rememberScaffoldState()
    TopBar(suspend { rememberScaffoldState.drawerState.close() })
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navHostController = rememberNavController())
}