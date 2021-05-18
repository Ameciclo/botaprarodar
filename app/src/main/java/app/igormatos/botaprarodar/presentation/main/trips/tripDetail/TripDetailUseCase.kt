package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import app.igormatos.botaprarodar.common.extensions.getLastDevolution
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import com.brunotmgomes.ui.SimpleResult

class TripDetailUseCase(private val repository: TripDetailRepository) {

    private val devolutionMessage = "Aguardando devolução"

    suspend fun getBikeById(bikeId: String): SimpleResult<BikeRequest> {
        return repository.getBikeById(bikeId)
    }

    fun getWithdrawById(bike: Bike, id: String): Withdraws? {
        return bike.withdraws?.first { it.id == id }
    }

    fun getDevolutionById(bike: Bike, id: String): Devolution? {
        return bike.devolutions?.first { it.id == id }
    }

    fun getDevolutionByWithdrawId(bike: Bike, id: String): Devolution? {
        return bike.devolutions?.firstOrNull { it.withdrawId == id }
    }

    fun verifyIfBikeIsInUse(bike: Bike) = bike.inUse

}