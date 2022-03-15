package app.igormatos.botaprarodar.presentation.components.navigation.return_bike

sealed class ReturnScreen(val route: String) {
    object ReturnSelectBike : ReturnScreen("return_select_bike")
    object ReturnQuiz: ReturnScreen("return_quiz")
    object ReturnConfirmation:ReturnScreen("return_confirmation")
    object ReturnFinishAction:ReturnScreen("return_finish_action")
}