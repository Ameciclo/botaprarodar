package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BikeRequestConvert
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult

private const val FIREBASE_URL = "https://"
private const val PATH = "bikes"

class BikeFormUseCase(
    private val bikeRepository: BikeRepository,
    private val firebaseHelperRepository: FirebaseHelperRepository
) {

    suspend fun addNewBike(bike: Bike): SimpleResult<AddDataResponse> {
        val imageResponse = uploadImage(bike)
        return saveBike(imageResponse, bike) {
            registerBike(it)
        }
    }

    suspend fun startUpdateBike(bike: Bike): SimpleResult<AddDataResponse> {
        return if (bike.path.contains(FIREBASE_URL)) {
            updateBike(bike)
        } else {
            val imageResponse = uploadImage(bike)
            saveBike(imageResponse, bike) {
                updateBike(it)
            }
        }
    }

    private suspend fun saveBike(
        imageResponse: SimpleResult<ImageUploadResponse>?,
        bike: Bike,
        actionFunction: suspend (Bike) -> SimpleResult<AddDataResponse>
    ): SimpleResult<AddDataResponse> {
        return when (imageResponse) {
            is SimpleResult.Success -> {
                setupBike(bike, imageResponse.data)
                actionFunction(bike)
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
        bike.serialNumber?.let {
            firebaseHelperRepository.uploadImageAndThumb(
                bike.path,
                "community/bike/$it"
            )
        }

    private fun setupBike(bike: Bike, imageResponse: ImageUploadResponse) {
        bike.photoPath = imageResponse.fullImagePath
        bike.photoThumbnailPath = imageResponse.thumbPath
        bike.path = PATH
    }

    private suspend fun registerBike(bike: Bike): SimpleResult<AddDataResponse> {
        return bikeRepository.addNewBike(bike)
    }

    private suspend fun updateBike(bike: Bike): SimpleResult<AddDataResponse> {
        bike.path = PATH
        return bikeRepository.updateBike(bike)
    }
}