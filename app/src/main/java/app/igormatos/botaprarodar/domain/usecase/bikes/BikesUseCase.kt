package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class BikesUseCase(private val bikeRepository: BikeRepository) {

    @ExperimentalCoroutinesApi
    suspend fun getBikes(communityId: String): Flow<List<Bike>> {
        return bikeRepository.getBikes(communityId)
    }
}