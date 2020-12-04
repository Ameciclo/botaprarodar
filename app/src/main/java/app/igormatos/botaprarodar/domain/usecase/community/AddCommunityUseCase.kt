package app.igormatos.botaprarodar.domain.usecase.community

import app.igormatos.botaprarodar.data.network.Community
import app.igormatos.botaprarodar.data.network.FirebaseHelperModule
import com.brunotmgomes.ui.SimpleResult
import java.lang.Exception

class AddCommunityUseCase(
    private val firebaseHelperModule: FirebaseHelperModule
) {

    suspend fun addNewCommunity(community: Community) : SimpleResult<Boolean> {
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