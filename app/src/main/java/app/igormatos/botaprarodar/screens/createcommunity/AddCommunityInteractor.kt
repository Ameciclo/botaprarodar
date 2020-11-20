package app.igormatos.botaprarodar.screens.createcommunity

import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener
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