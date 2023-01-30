package app.igormatos.botaprarodar.presentation.login.passwordRecovery

sealed class RecoveryPasswordState(open val data: RecoveryPasswordData) {
    class Success(override val data: RecoveryPasswordData) : RecoveryPasswordState(data)
    class Loading(override val data: RecoveryPasswordData) : RecoveryPasswordState(data)
    class Error(
        override val data: RecoveryPasswordData,
        val message: Int
    ) : RecoveryPasswordState(data)
}