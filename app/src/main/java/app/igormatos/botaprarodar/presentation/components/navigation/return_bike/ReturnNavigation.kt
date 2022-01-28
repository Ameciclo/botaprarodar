package app.igormatos.botaprarodar.presentation.components.navigation.return_bike

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.BikeList
import app.igormatos.botaprarodar.presentation.components.FinishAction
import app.igormatos.botaprarodar.presentation.components.FinishActionComponent
import app.igormatos.botaprarodar.presentation.components.WithdrawConfirmationComponent
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleQuizPage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext

@ExperimentalCoroutinesApi
@Composable
fun ReturnNavigationComponent(
    bikeList: List<Bike>,
    navController: NavHostController,
    handleClick: (Any?) -> Unit,
    backToHome: () -> Unit,
) {
    NavHost(navController = navController, startDestination = ReturnScreen.ReturnSelectBike.route) {
        composable(ReturnScreen.ReturnSelectBike.route) {
            BikeList(bikes = bikeList, handleClick = handleClick)
        }

        composable(ReturnScreen.ReturnQuiz.route) {
            ReturnBicycleQuizPage()
        }

        composable(ReturnScreen.ReturnConfirmation.route) {
            // TODO - Refatorar e transformar em um componente generico
            WithdrawConfirmationComponent(handleClick = handleClick)
        }

        composable(ReturnScreen.ReturnFinishAction.route) {
            FinishActionComponent(
                mainMessage = stringResource(
                    id = R.string.success_devolution_message
                ),
                mainActionText = stringResource(
                    id = R.string.repeat_devolution_title
                ),
                mainAction = handleClick,
                backToHome = backToHome
            )
        }
    }
}