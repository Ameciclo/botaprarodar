package app.igormatos.botaprarodar.domain.usecase.withdraw

import app.igormatos.botaprarodar.common.extensions.convertToBikeRequest
import app.igormatos.botaprarodar.common.utils.generateRandomAlphanumeric
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.data.repository.WithdrawBikeRepository
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult

class SendBikeWithdraw(
    private val withdrawRepository: WithdrawBikeRepository,
    private val userRepository: UserRepository
) {

    suspend fun execute(
        withdrawDate: String,
        bikeHolder: BikeHolder,
        userHolder: UserHolder
    ): SimpleResult<AddDataResponse> {
        val withdraw = withdrawToSend(withdrawDate, userHolder)
        updateBikeToSend(bikeHolder, withdraw)
        val bikeRequest = bikeHolder.bike?.convertToBikeRequest()!!

        val addWithdrawResponse: SimpleResult<AddDataResponse> =
            withdrawRepository.addWithdraw(bikeRequest)

        if (addWithdrawResponse is SimpleResult.Success)
            return updateUserWithActiveWithdraw(withdraw)

        return addWithdrawResponse
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

    private suspend fun updateUserWithActiveWithdraw(withdraw: Withdraws): SimpleResult<AddDataResponse> {
        val user: User? = withdraw.user
        user?.hasActiveWithdraw = true
        return userRepository.updateUser(user!!)
    }
}