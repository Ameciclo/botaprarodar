package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.*
import utils.Result
import java.lang.Exception

class AddCommunityInteractor(
    private val firebaseHelperModule: FirebaseHelperModule
) {

    fun addCommunityToServer(community: Community) : Result<Boolean> {
        return try {
            firebaseHelperModule.addCommunity(community)
            Result.Success(true)
        } catch (exception: Exception) {
            Result.Error(exception)
        }
    }
}