package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.domain.model.community.Community

sealed class UserInfoState {
    class Admin(val communities: List<Community>) : UserInfoState()
    class NotAdmin(val communities: List<Community>) : UserInfoState()
    object NotAdminWithoutCommunities : UserInfoState()
}