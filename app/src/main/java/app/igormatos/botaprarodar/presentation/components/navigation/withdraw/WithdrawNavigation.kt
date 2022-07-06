package app.igormatos.botaprarodar.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.*
import app.igormatos.botaprarodar.presentation.components.navigation.withdraw.WithdrawScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun WithdrawNavigationComponent(
    vm: WithdrawViewModel,
    navController: NavHostController,
    bikeList: List<Bike>,
    cyclistList: List<User>,
    handleClick: (Any?) -> Unit,
    backToHome: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = WithdrawScreen.WithdrawSelectBike.route
    ) {
        composable(WithdrawScreen.WithdrawSelectBike.route) {
            BikeList(
                bikes = bikeList,
                handleClick = handleClick
            )
        }
        composable(WithdrawScreen.WithdrawSelectUser.route) {
            UsersListWithFilter(users = cyclistList, handleClick = handleClick)
        }
        composable(WithdrawScreen.WithdrawConfirmation.route) {
            WithdrawConfirmationComponent(
                vm = vm,
                handleClick = handleClick
            )
        }
        composable(WithdrawScreen.WithdrawFinishAction.route) {
            FinishActionComponent(
                mainMessage = stringResource(id = R.string.success_withdraw_message),
                mainActionText = stringResource(
                    id = R.string.repeat_withdraw_title
                ),
                mainAction = handleClick,
                backToHome = backToHome
            )
        }
    }
}