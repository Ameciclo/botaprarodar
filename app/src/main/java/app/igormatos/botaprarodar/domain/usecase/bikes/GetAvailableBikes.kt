package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.common.extensions.convertMapperToBikeList
import app.igormatos.botaprarodar.common.extensions.sortByOrderNumber
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikewithdraw.GetAvailableBikesException
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class GetAvailableBikes(private val bikeRepository: BikeRepository) {

    @Throws(GetAvailableBikesException::class)
    suspend fun execute(communityId: String): List<Bike> {
        return when (val result = bikeRepository.getBicycles()) {
            is SimpleResult.Success -> {
                val bikes: MutableList<Bike> = result.data.convertMapperToBikeList()
                val bikesNotInUse = bikes.filter { !it.inUse && it.communityId == communityId }
                bikesNotInUse.sortByOrderNumber()

            }
            is SimpleResult.Error -> {
                throw GetAvailableBikesException()
            }
        }
    }
}