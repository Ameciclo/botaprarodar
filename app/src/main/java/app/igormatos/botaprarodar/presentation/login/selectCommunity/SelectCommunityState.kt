package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.common.enumType.BprErrorType

sealed class SelectCommunityState {
    class Success(val userInfoState: UserInfoState) : SelectCommunityState()
    class Error(val error: BprErrorType) : SelectCommunityState()
    object Loading : SelectCommunityState()
}