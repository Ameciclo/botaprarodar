package app.igormatos.botaprarodar.domain.usecase.community

import app.igormatos.botaprarodar.data.repository.CommunityRepository
import app.igormatos.botaprarodar.domain.model.community.CommunityBody
import utils.SimpleResult
import kotlin.Exception

class AddCommunityUseCase(
    private val communityRepository: CommunityRepository
) {

    suspend fun addNewCommunity(community: CommunityBody) : SimpleResult<String> {
        return try {
            SimpleResult.Success(communityRepository.addCommunity(community))
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }

    private fun validateFields(community: CommunityBody) : Boolean {
        return when {
            community.name.isNullOrEmpty() -> false
            community.description.isNullOrEmpty() -> false
            community.address.isNullOrEmpty() -> false
            community.orgName.isNullOrEmpty() -> false
            community.orgEmail.isNullOrEmpty() -> false
            else -> true
        }
    }
}