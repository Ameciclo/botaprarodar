package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.*
import utils.SimpleResult
import java.lang.Exception

class AddCommunityUseCase(
    private val firebaseHelperModule: FirebaseHelperModule
) {

    suspend fun sendNewCommunity(community: Community) : SimpleResult<Boolean> {
        return if (validateFields(community)) {
            firebaseHelperModule.addCommunity(community)
        } else {
            SimpleResult.Error(Exception())
        }
    }

    private fun validateFields(community: Community) : Boolean {
        return when {
            community.name.isNullOrEmpty() -> false
            community.description.isNullOrEmpty() -> false
            community.address.isNullOrEmpty() -> false
            community.org_name.isNullOrEmpty() -> false
            community.org_email.isNullOrEmpty() -> false
            else -> true
        }
    }
}