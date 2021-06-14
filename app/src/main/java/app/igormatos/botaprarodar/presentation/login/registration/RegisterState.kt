package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class RegisterState() {
    object Success : RegisterState()
    class Error(val type: BprErrorType) : RegisterState()
    object Loading : RegisterState()
}
