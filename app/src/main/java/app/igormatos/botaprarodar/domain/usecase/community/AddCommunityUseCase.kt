package app.igormatos.botaprarodar.domain.usecase.community

import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import com.brunotmgomes.ui.SimpleResult

class AddCommunityUseCase(
    private val communityRepository: CommunityRepository
) {

    suspend fun addNewCommunity(community: CommunityRequest) : SimpleResult<String> {
        return try {
            SimpleResult.Success(communityRepository.addCommunity(community))
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }
}