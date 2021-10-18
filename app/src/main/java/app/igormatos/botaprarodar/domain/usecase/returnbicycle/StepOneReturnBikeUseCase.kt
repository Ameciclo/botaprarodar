package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.common.extensions.convertMapperToBikeList
import app.igormatos.botaprarodar.common.extensions.sortByOrderNumber
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StepOneReturnBikeUseCase(private val bikeRepository: BikeRepository) {

    suspend fun getBikesInUseToReturn(communityId: String): SimpleResult<List<Bike>> {
        val bikesMap: SimpleResult<Map<String, BikeRequest>> = bikeRepository.getBicycles()
        return when (bikesMap) {
            is SimpleResult.Success -> {
                val bikes = bikesMap.data.convertMapperToBikeList()
                val bikesInUse = filterInUseBikes(bikes, communityId)
                formatAnswer(bikesInUse.sortByOrderNumber())
            }
            is SimpleResult.Error -> {
                bikesMap
            }
        }
    }

    private fun filterInUseBikes(bikes: List<Bike>, communityId: String): List<Bike> {
        return bikes.filter {
            it.inUse && it.communityId == communityId
        }
    }

    private fun formatAnswer(list: List<Bike>): SimpleResult<List<Bike>> {
        return when (list.isNotEmpty()) {
            true -> SimpleResult.Success(list)
            false -> SimpleResult.Error(Exception(""))
        }
    }
}