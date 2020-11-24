package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.*
import utils.SimpleResult
import java.lang.Exception

class AddCommunityUseCase(
    private val firebaseHelperModule: FirebaseHelperModule
) {

    fun addCommunityToServer(community: Community) : SimpleResult<Boolean> {
        return try {
            firebaseHelperModule.addCommunity(community)
            SimpleResult.Success(true)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }
}