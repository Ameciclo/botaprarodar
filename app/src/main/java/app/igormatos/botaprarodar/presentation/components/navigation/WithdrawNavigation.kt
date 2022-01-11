package app.igormatos.botaprarodar.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.WithdrawViewModel
import app.igormatos.botaprarodar.presentation.components.BikeListComponent
import app.igormatos.botaprarodar.presentation.components.CyclistListComponent
import app.igormatos.botaprarodar.presentation.components.FinishActionComponent
import app.igormatos.botaprarodar.presentation.components.WithdrawConfirmationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun WithdrawNaviationComponent(
    vm: WithdrawViewModel,
    joinedCommunityId: String,
    navController: NavHostController,
    handleClick: () -> Unit,
    backToHome: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = WithdrawScreen.WithdrawSelectBike.route
    ) {
        composable(WithdrawScreen.WithdrawSelectBike.route) {
            BikeListComponent(
                vm = vm,
                handleClick = handleClick
            )
        }
        composable(WithdrawScreen.WithdrawSelectUser.route) {
            CyclistListComponent(
                vm = vm,
                joinedCommunityId = joinedCommunityId,
                handleClick = handleClick
            )
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