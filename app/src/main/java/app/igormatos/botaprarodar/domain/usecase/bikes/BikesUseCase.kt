package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.ListBicyclesConverter
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class BikesUseCase(private val bikeRepository: BikeRepository) {

    private val listBicyclesConverter = ListBicyclesConverter()


    private fun formatAnswer(list: List<Bike>): SimpleResult<List<Bike>> {
        return when (list.isNotEmpty()) {
            true -> SimpleResult.Success(list)
            false -> SimpleResult.Error(Exception(""))
        }
    }

    suspend fun getBikes(communityId: String): Flow<SimpleResult<List<Bike>>> {
        return bikeRepository.getBikes(communityId)
    }
}