package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.data.network.RequestError

sealed class SelectCommunityState {
    class Success(val userInfoState: UserInfoState) : SelectCommunityState()
    class Error(val error: RequestError) : SelectCommunityState()
    object Loading : SelectCommunityState()
}