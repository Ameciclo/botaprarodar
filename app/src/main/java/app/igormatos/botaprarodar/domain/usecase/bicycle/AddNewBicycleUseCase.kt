package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.repository.BicycleRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.BicycleRequestConvert
import app.igormatos.botaprarodar.domain.model.Bicycle
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNewBicycleUseCase(private val bicycleRepository: BicycleRepository) {

    private val bicycleRequestConvert = BicycleRequestConvert()

    suspend fun addNewBicycle(communityId: String, bicycle: Bicycle): String {
        val bicycleRequest = bicycleRequestConvert.convert(bicycle)
        return bicycleRepository.addNewBicycle(communityId, bicycleRequest)
    }
}