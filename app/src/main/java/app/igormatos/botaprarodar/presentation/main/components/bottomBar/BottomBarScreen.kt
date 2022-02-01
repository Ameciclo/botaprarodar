package app.igormatos.botaprarodar.presentation.main.components.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: BottomBarScreen(
        route = "home",
        title = "Início",
        icon = Icons.Default.Home
    )

    object Activities: BottomBarScreen(
        route = "activities",
        title = "Atividades",
        icon = Icons.Default.List
    )

    object Users: BottomBarScreen(
        route = "users",
        title = "Usuárias",
        icon = Icons.Default.PermIdentity
    )

    object Bikes: BottomBarScreen(
        route = "bikes",
        title = "Bicicletas",
        icon = Icons.Default.PedalBike
    )
}