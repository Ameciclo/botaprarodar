package app.igormatos.botaprarodar.presentation.login.selectCommunity.community

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.CommunityRepository

class CommunityUseCase(
    private val communityRepository: CommunityRepository
) {

    suspend fun getCommunitiesPreview(): CommunityState {
        return try {
            val communities = communityRepository.getCommunitiesPreview()
            CommunityState.Success(communities)
        } catch (e: UserAdminErrorException.AdminNetwork) {
            CommunityState.Error(BprErrorType.NETWORK)
        } catch (e: Exception) {
            CommunityState.Error(BprErrorType.UNKNOWN)
        }
    }
}