package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BikeRequestConvert
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult

class BikeFormUseCase(
    private val bikeRepository: BikeRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository
) {

    private val bike = BikeRequestConvert()

    suspend fun addNewBike(communityId: String, bike: Bike): SimpleResult<String> {
        val imageResponse = uploadImage(bike)
        return when (imageResponse) {
            is SimpleResult.Success -> {
                updateBike(bike, imageResponse.data)
                onSuccessUploadImage(bike, communityId)
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(imageResponse.exception)
            }
            else -> { SimpleResult.Error(Exception(""))}
        }
    }

    private suspend fun uploadImage(bike: Bike) =
        bike.serial_number?.let { firebaseHelperRepository.uploadImage(bike.path, it) }

    private fun updateBike(bike: Bike, imageResponse: ImageUploadResponse) {
        bike.photo_path = imageResponse.fullImagePath
        bike.photo_thumbnail_path = imageResponse.thumbPath
    }

    private suspend fun onSuccessUploadImage(
        bike: Bike,
        communityId: String
    ): SimpleResult<String> {
        return try {
            val bicycleRequest = this.bike.convert(bike)
            val result = bikeRepository.addNewBike(communityId, bicycleRequest)
            SimpleResult.Success(result)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }
}