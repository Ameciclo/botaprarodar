package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.*
import utils.SimpleResult
import java.lang.Exception

class AddCommunityUseCase(
    private val firebaseHelperModule: FirebaseHelperModule
) {

    suspend fun addCommunityToServer(community: Community) : SimpleResult<Boolean> {
        val validateFieldsResult = validateFields(community)
        return if (validateFieldsResult is SimpleResult.Success) {
            firebaseHelperModule.addCommunity(community)
        } else {
            validateFieldsResult
        }
    }

    private fun validateFields(community: Community) : SimpleResult<Boolean> {
        return when {
            community.name.isNullOrEmpty() -> SimpleResult.Error(Exception("Preencha o nome da nova comunidade"))
            community.description.isNullOrEmpty() -> SimpleResult.Error(Exception("Preencha a descrição da nova comunidade"))
            community.address.isNullOrEmpty() -> SimpleResult.Error(Exception("Preencha o endereço da nova comunidade"))
            community.org_name.isNullOrEmpty() -> SimpleResult.Error(Exception("Preencha o nome do responsável pela nova comunidade"))
            community.org_email.isNullOrEmpty() -> SimpleResult.Error(Exception("Preencha o email do responsável pela nova comunidade"))
            else -> SimpleResult.Success(true)
        }
    }

}