package app.igormatos.botaprarodar.presentation.login.selectCommunity.community

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.domain.model.community.Community

sealed class CommunityState {
    class Success(val communities: List<Community>) : CommunityState()
    class Error(val type: BprErrorType) : CommunityState()
}