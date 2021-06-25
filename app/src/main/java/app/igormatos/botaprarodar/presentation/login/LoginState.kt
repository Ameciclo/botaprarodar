package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin

sealed class LoginState {
    class Success(val admin: Admin) : LoginState()
    class Error(val type: BprErrorType) : LoginState()
    object Loading : LoginState()
}