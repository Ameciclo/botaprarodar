package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BikeRequestConvert
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult

private const val FIREBASE_URL = "https://"

class BikeFormUseCase(
    private val bikeRepository: BikeRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository
) {

    private val bike = BikeRequestConvert()

    suspend fun addNewBike(communityId: String, bike: Bike): SimpleResult<AddDataResponse> {
        val imageResponse = uploadImage(bike)
        return saveBike(imageResponse, bike, communityId) { _, _ ->
            registerBike(bike, communityId)
        }
    }

    suspend fun startUpdateBike(communityId: String, bike: Bike): SimpleResult<AddDataResponse> {
        return if (bike.path.contains(FIREBASE_URL)) {
            updateBike(bike, communityId)
        } else {
            val imageResponse = uploadImage(bike)
            saveBike(imageResponse, bike, communityId) { _, _ ->
                updateBike(bike, communityId)
            }
        }
    }

    private suspend fun saveBike(
        imageResponse: SimpleResult<ImageUploadResponse>?,
        bike: Bike,
        communityId: String,
        actionFunction: suspend (Bike, String) -> SimpleResult<AddDataResponse>
    ): SimpleResult<AddDataResponse> {
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
        bike.serial_number?.let {
            firebaseHelperRepository.uploadImageAndThumb(
                bike.path,
                "community/bike/$it"
            )
        }

    private fun setupBike(bike: Bike, imageResponse: ImageUploadResponse) {
        bike.photo_path = imageResponse.fullImagePath
        bike.photo_thumbnail_path = imageResponse.thumbPath
    }

    private suspend fun registerBike(
        bike: Bike,
        communityId: String
    ): SimpleResult<AddDataResponse> {
        val bicycleRequest = this.bike.convert(bike)
        return bikeRepository.addNewBike(communityId, bicycleRequest)
    }

    private suspend fun updateBike(
        bike: Bike,
        communityId: String
    ): SimpleResult<AddDataResponse> {
            val bicycleRequest = this.bike.convert(bike)
            return bikeRepository.updateBike(communityId, bicycleRequest)
    }
}