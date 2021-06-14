package app.igormatos.botaprarodar.presentation.login.selectCommunity.admin

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class AdminState {
    object IsAdmin : AdminState()
    object NotIsAdmin : AdminState()
    class Error(val type: BprErrorType) : AdminState()
}
