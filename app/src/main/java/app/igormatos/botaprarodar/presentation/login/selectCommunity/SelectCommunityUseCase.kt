package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.admin.AdminUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.community.CommunityUseCase

class SelectCommunityUseCase(
    private val adminUseCase: AdminUseCase,
    private val communityUseCase: CommunityUseCase
) {

    suspend fun loadCommunitiesByAdmin(userId: String, userEmail: String?): SelectCommunityState {
        val adminState: AdminState = adminUseCase.isAdmin(userId)
        val communityState: CommunityState = communityUseCase.getCommunitiesPreview()

        if (communityState is CommunityState.Error) {
            return SelectCommunityState.Error(communityState.type)
        }

        val communityStateSuccess = communityState as CommunityState.Success
        val communities = communityStateSuccess.communities

        return when (adminState) {
            is AdminState.IsAdmin -> {
                SelectCommunityState.Success(UserInfoState.Admin(communities))
            }
            is AdminState.NotIsAdmin -> {
                val filteredCommunities = communities.filter { it.org_email == userEmail }
                if (filteredCommunities.isEmpty())
                    SelectCommunityState.Success(UserInfoState.NotAdminWithoutCommunities)
                else
                    SelectCommunityState.Success(UserInfoState.NotAdmin(filteredCommunities))
            }
            is AdminState.Error -> {
                SelectCommunityState.Error(adminState.type)
            }
        }
    }
}