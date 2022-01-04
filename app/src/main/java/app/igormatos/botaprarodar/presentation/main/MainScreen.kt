package app.igormatos.botaprarodar.presentation.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.bikewithdraw.BikeWithdrawActivity
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeActivity
import app.igormatos.botaprarodar.presentation.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(homeUiState: HomeUiState) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    Scaffold(
        topBar = { TopBar(onClickMenuHandler = suspend { scaffoldState.drawerState.open() }) },
        bottomBar = { BottomBar(navHostController = navController) },
        scaffoldState = scaffoldState,
        drawerContent = {
            DrawerContent(
                navController,
                onClickCloseHandler = suspend { scaffoldState.drawerState.close() })
        }
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

@Composable
fun DrawerContent(navHostController: NavHostController, onClickCloseHandler: suspend () -> Unit) {
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    Column {
        Row(
            horizontalArrangement = Arrangement.End, modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White
                )
        ) {
            IconButton(onClick = {
                scope.launch {
                    onClickCloseHandler()
                }
            }) {
                Icon(imageVector = Icons.Default.Close, null)
            }
        }
        DrawerItemsToBottomBarNavigation(navHostController, scope, onClickCloseHandler)

        DrawerItem(
            painter = painterResource(id = R.drawable.ic_withdraw_bike),
            "Emprestar bicicleta"
        ) {
            localContext.startActivity(Intent(localContext, BikeWithdrawActivity::class.java))
        }
        DrawerItem(
            painter = painterResource(id = R.drawable.ic_return_bike),
            "Devolver bicicleta"
        ) {
            localContext.startActivity(Intent(localContext, ReturnBikeActivity::class.java))
        }
        DrawerItem(painter = painterResource(id = R.drawable.ic_add_bike), "Cadastrar bicicleta") {
            localContext.startActivity(Intent(localContext, BikeFormActivity::class.java))
        }
        DrawerItem(painter = painterResource(id = R.drawable.ic_add_user), "Cadastrar usuária") {
            localContext.startActivity(Intent(localContext, UserActivity::class.java))
        }
        DrawerItem(imageVector = Icons.Filled.Logout, "Sair") { logout(localContext) }
    }
}

@Composable
private fun DrawerItemsToBottomBarNavigation(
    navHostController: NavHostController,
    scope: CoroutineScope,
    onClickCloseHandler: suspend () -> Unit
) {
    DrawerItem(imageVector = Icons.Filled.Home, "Inicio") {
        navHostController.navigate(BottomBarScreen.Home.route)
        scope.launch {
            onClickCloseHandler()
        }
    }
    DrawerItem(imageVector = Icons.Filled.List, "Atividades") {
        navHostController.navigate(BottomBarScreen.Activities.route)
        scope.launch {
            onClickCloseHandler()
        }
    }
    DrawerItem(painter = painterResource(id = R.drawable.ic_add_user), "Usuárias") {
        navHostController.navigate(BottomBarScreen.Users.route)
        scope.launch {
            onClickCloseHandler()
        }
    }
    DrawerItem(imageVector = Icons.Filled.PedalBike, "Bicicletas") {
        navHostController.navigate(BottomBarScreen.Bikes.route)
        scope.launch {
            onClickCloseHandler()
        }
    }
}

@Composable
fun DrawerItem(painter: Painter, title: String, onClickHandler: () -> Unit = {}) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClickHandler() }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painter,
                contentDescription = null,
                Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun DrawerItem(imageVector: ImageVector, title: String, onClickHandler: () -> Unit = {}) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClickHandler()

        }) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                imageVector = imageVector,
                contentDescription = null,
                Modifier.padding(end = 16.dp)
            )
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

@Preview
@Composable
fun DrawerItemPreview() {
    val navController = rememberNavController()
    DrawerItem(imageVector = Icons.Filled.Home, "Inicio") {
        navController.navigate(BottomBarScreen.Home.route)
    }
}

@Preview
@Composable
fun DrawerContentPreview() {
    val rememberScaffoldState = rememberScaffoldState()
    DrawerContent(
        navHostController = rememberNavController(),
        onClickCloseHandler = suspend { rememberScaffoldState.drawerState.close() }
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