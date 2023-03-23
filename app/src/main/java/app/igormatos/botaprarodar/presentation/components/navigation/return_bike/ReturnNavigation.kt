package app.igormatos.botaprarodar.presentation.components.navigation.return_bike

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.components.BikeList
import app.igormatos.botaprarodar.presentation.components.FinishActionComponent
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleQuizPage
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleResume
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
@Composable()
fun ReturnNavigationComponent(
    viewModel: ReturnBicycleViewModel,
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
            ReturnBicycleQuizPage(handleClick = handleClick, viewModel = viewModel)
        }

        composable(ReturnScreen.ReturnConfirmation.route) {
            val bike = viewModel.bikeHolder.value
            bike?.let {
                ReturnBicycleResume(viewModel.loadingState.observeAsState(initial = false), it) {
                    viewModel.addDevolution {
                        viewModel.navigateToFinishedStep()
                        navController.navigate(ReturnScreen.ReturnFinishAction.route)
                    }
                }
            }
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
