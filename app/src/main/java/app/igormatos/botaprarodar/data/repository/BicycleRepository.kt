package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.BicycleApi
import app.igormatos.botaprarodar.domain.model.Bicycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BicycleRepository(private val bicycleApi: BicycleApi) {

    suspend fun getBicycles(communityId: String): Map<String, Bicycle> {
        return withContext(Dispatchers.IO) {
            return@withContext bicycleApi.getBicycles(communityId = communityId).await()
        }
    }

}