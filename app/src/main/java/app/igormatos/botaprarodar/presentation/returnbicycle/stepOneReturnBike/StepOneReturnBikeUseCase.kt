package app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.ListBicyclesConverter
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StepOneReturnBikeUseCase(private val bikeRepository: BikeRepository) {
    private val listBicyclesConverter = ListBicyclesConverter()

    suspend fun getBikesInUseToReturn(communityId: String): SimpleResult<List<Bike>> {
        val bikesMap = bikeRepository.getBicycles()
        return when (bikesMap) {
            is SimpleResult.Success -> {
                val list = listBicyclesConverter.convert(bikesMap.data)
                val bikesInUse = filterInUseBikes(list, communityId)
                formatAnswer(bikesInUse)
            }
            is SimpleResult.Error -> {
                bikesMap
            }
        }
    }

    private fun filterInUseBikes(list: List<Bike>, communityId: String): List<Bike> {
        return list.filter {
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