package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class BikesUseCase(private val bikeRepository: BikeRepository) {

    suspend fun getBikes(communityId: String): Flow<SimpleResult<List<BikeRequest>>> {
        return bikeRepository.getBikes(communityId)
    }
}