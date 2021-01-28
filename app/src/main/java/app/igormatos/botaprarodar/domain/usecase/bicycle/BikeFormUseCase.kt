package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BikeRequestConvert
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult

private const val DEFAULT_PATH = "bicycles"

class BikeFormUseCase(
    private val bikeRepository: BikeRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository
) {

    private val bike = BikeRequestConvert()

    suspend fun addNewBike(communityId: String, bike: Bike): SimpleResult<String> {
        val imageResponse = uploadImage(bike)
        return saveBike(imageResponse, bike, communityId) { _, _ ->
            registerBike(bike, communityId)
        }
    }

    suspend fun updateBike(communityId: String, bike: Bike): SimpleResult<String> {
        return if (bike.path != DEFAULT_PATH && bike.path.contains("https").not()) {
            val imageResponse = uploadImage(bike)
            saveBike(imageResponse, bike, communityId) { _, _ ->
                updateBike(bike, communityId)
            }
        } else
            updateBike(bike, communityId)
    }

    private suspend fun saveBike(
        imageResponse: SimpleResult<ImageUploadResponse>?,
        bike: Bike,
        communityId: String,
        actionFunction: suspend (Bike, String) -> SimpleResult<String>
    ): SimpleResult<String> {
        return when (imageResponse) {
            is SimpleResult.Success -> {
                setupBike(bike, imageResponse.data)
                actionFunction(bike, communityId)
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(imageResponse.exception)
            }
            else -> {
                SimpleResult.Error(Exception(""))
            }
        }
    }

    private suspend fun uploadImage(bike: Bike) =
        bike.serial_number?.let { firebaseHelperRepository.uploadImage(bike.path, it) }

    private fun setupBike(bike: Bike, imageResponse: ImageUploadResponse) {
        bike.photo_path = imageResponse.fullImagePath
        bike.photo_thumbnail_path = imageResponse.thumbPath
    }

    private suspend fun registerBike(
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

    private suspend fun updateBike(
        bike: Bike,
        communityId: String
    ): SimpleResult<String> {
        return try {
            val bicycleRequest = this.bike.convert(bike)
            val result = bikeRepository.updateBike(communityId, bicycleRequest)
            SimpleResult.Success(result)
        } catch (exception: Exception) {
            SimpleResult.Error(exception)
        }
    }
}