package app.igormatos.botaprarodar.domain.usecase.withdraw

import app.igormatos.botaprarodar.common.extensions.convertToBikeRequest
import app.igormatos.botaprarodar.common.utils.generateRandomAlphanumeric
import app.igormatos.botaprarodar.data.repository.WithdrawBikeRepository
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult

class SendBikeWithdraw(private val withdrawRepository: WithdrawBikeRepository) {

    suspend fun execute(
        withdrawDate: String,
        bikeHolder: BikeHolder,
        userHolder: UserHolder
    ): SimpleResult<AddDataResponse> {
        val withdraw = withdrawToSend(withdrawDate, userHolder)
        updateBikeToSend(bikeHolder, withdraw)
        val bikeRequest = bikeHolder.bike?.convertToBikeRequest()!!
        return withdrawRepository.addWithdraw(bikeRequest)
    }

    private fun withdrawToSend(
        withdrawDate: String,
        userHolder: UserHolder
    ): Withdraws {
        return Withdraws(
            id = "-" + generateRandomAlphanumeric(),
            date = withdrawDate,
            user = userHolder.user
        )
    }

    private fun updateBikeToSend(
        bikeHolder: BikeHolder,
        withdraws: Withdraws
    ) {
        if (bikeHolder.bike?.withdraws == null) {
            bikeHolder.bike?.withdraws = mutableListOf(withdraws)
            bikeHolder.bike?.inUse = true
        } else {
            bikeHolder.bike?.withdraws?.add(withdraws)
            bikeHolder.bike?.inUse = true
        }
    }
}