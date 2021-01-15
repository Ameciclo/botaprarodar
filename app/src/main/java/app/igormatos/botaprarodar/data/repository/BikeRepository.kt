package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.Bike
import kotlinx.coroutines.*

class BikeRepository(private val bicycleApi: BicycleApi) {

    suspend fun getBicycles(communityId: String): Map<String, Bike> {
        return withContext(Dispatchers.IO) {
            return@withContext bicycleApi.getBicycles(communityId = communityId).await()
        }
    }

    suspend fun addNewBike(communityId: String, bicycle: BicycleRequest): String {
        return bicycleApi.addNewBicycle(communityId, bicycle).name
    }

}