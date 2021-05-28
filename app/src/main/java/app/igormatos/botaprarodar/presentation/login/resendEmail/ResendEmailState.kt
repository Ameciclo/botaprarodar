package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class ResendEmailState {
    object Success : ResendEmailState()
    class Error(val type: BprErrorType) : ResendEmailState()
    object Loading : ResendEmailState()
}