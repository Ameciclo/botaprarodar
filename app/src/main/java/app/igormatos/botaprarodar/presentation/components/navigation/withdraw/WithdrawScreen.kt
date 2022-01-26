package app.igormatos.botaprarodar.presentation.components.navigation.withdraw

sealed class WithdrawScreen(val route: String) {
    object WithdrawSelectBike : WithdrawScreen("withdraw_select_bike")
    object WithdrawSelectUser : WithdrawScreen("withdraw_select_user")
    object WithdrawConfirmation : WithdrawScreen("confirmation_withdraw")
    object WithdrawFinishAction : WithdrawScreen("withdraw_finish_action")
}