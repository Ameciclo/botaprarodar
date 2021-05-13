package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TripDetailRepository(private val bicycleApi: BicycleApi) {

    suspend fun getBikeById(bikeId: String): SimpleResult<BikeRequest> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.getBikeById(bikeId)
            }
        }
    }
}