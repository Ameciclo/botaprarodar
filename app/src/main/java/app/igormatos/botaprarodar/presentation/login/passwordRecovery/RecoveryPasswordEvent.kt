package app.igormatos.botaprarodar.presentation.login.passwordRecovery

sealed class RecoveryPasswordEvent {
    object RecoveryPasswordSuccessful : RecoveryPasswordEvent()
    class Loading(val show: Boolean): RecoveryPasswordEvent()
    object Finish : RecoveryPasswordEvent()
}