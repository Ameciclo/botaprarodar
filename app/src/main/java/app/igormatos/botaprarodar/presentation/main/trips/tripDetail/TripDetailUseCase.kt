package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult

class TripDetailUseCase(private val repository: TripDetailRepository) {

    suspend fun getBikeById(bikeId: String): SimpleResult<BikeRequest> {
        return repository.getBikeById(bikeId)
    }
}