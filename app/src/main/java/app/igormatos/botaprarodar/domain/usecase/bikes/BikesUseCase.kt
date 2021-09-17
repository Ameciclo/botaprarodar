package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.common.extensions.convertToBikeList
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class BikesUseCase(private val bikeRepository: BikeRepository) {

    suspend fun getBikes(communityId: String): SimpleResult<List<Bike>> {
        return when (val result = bikeRepository.getBikesByCommunityId(communityId)) {
            is SimpleResult.Success -> {
                val listBikes = result.data.values.toList().convertToBikeList()
                SimpleResult.Success(listBikes)
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(result.exception)
            }
        }
    }
}