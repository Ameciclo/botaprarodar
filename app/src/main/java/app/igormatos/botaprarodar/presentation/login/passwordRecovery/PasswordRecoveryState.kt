package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class PasswordRecoveryState {
    object Success : PasswordRecoveryState()
    class Error(val type: BprErrorType) : PasswordRecoveryState()
    object Loading : PasswordRecoveryState()
}