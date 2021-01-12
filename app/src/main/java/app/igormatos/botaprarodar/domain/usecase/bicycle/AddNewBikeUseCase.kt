package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BikeRequestConvert
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult

class AddNewBikeUseCase(private val bikeRepository: BikeRepository) {

    private val bike = BikeRequestConvert()

    suspend fun addNewBike(communityId: String, bike: Bike): SimpleResult<String> {
        return try {
            val bicycleRequest = this.bike.convert(bike)
            val result = bikeRepository.addNewBike(communityId, bicycleRequest)
            SimpleResult.Success(result)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }
}