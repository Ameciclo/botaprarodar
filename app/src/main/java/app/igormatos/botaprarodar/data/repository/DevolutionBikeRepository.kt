package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DevolutionBikeRepository(val bikeApi: BicycleApi) {

    suspend fun addDevolution(bikeRequest: BikeRequest): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bikeApi.updateBike(bikeRequest.id.orEmpty(), bikeRequest)
            }
        }
    }

}