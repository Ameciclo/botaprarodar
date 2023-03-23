package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.common.extensions.convertMapperToBikeList
import app.igormatos.botaprarodar.common.extensions.sortByOrderNumber
import app.igormatos.botaprarodar.data.repository.BikeRepository
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StepOneReturnBikeUseCase(private val bikeRepository: BikeRepository) {

    suspend fun getBikesInUseToReturn(communityId: String) =
        when (val bikesMap = bikeRepository.getBikesByCommunityId(communityId)) {
            is SimpleResult.Success -> {
                bikesMap.data.convertMapperToBikeList()
                    .filter { it.inUse }
                    .sortByOrderNumber()
                    .takeIf { it.isNotEmpty() }
                    ?.let { SimpleResult.Success(it) }
                    ?: SimpleResult.Error(Exception("No bikes found"))
            }
            is SimpleResult.Error -> {
                bikesMap
            }
        }

    fun getBicycleReturnUseMap() = bikeRepository.getBicycleReturnUseMap()
}
