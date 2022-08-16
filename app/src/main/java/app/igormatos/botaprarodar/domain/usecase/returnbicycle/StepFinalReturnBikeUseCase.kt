package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.common.extensions.convertToBikeRequest
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.common.utils.generateRandomAlphanumeric
import app.igormatos.botaprarodar.data.repository.DevolutionBikeRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.*
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class StepFinalReturnBikeUseCase(
    private val devolutionRepository: DevolutionBikeRepository,
    private val userRepository: UserRepository
) {

    suspend fun addDevolution(
        devolutionDate: String,
        bikeHolder: Bike,
        devolutionQuiz: Quiz
    ): SimpleResult<AddDataResponse> {
        val devolution = devolutionToSend(bikeHolder, devolutionDate, devolutionQuiz)
        updateBikeToSend(bikeHolder, devolution)
        val bikeRequest = bikeHolder.convertToBikeRequest()

        val addDevolutionResponse: SimpleResult<AddDataResponse> =
            devolutionRepository.addDevolution(bikeRequest)

        if (addDevolutionResponse is SimpleResult.Success)
            return updateUserWithInactiveWithdraw(devolution)

        return addDevolutionResponse
    }

    private fun updateBikeToSend(
        bikeHolder: Bike,
        devolution: Devolution
    ) {
        with(bikeHolder) {
            if (this.devolutions == null) {
                this.devolutions = mutableListOf(devolution)
            } else {
                this.devolutions?.add(devolution)
            }
            this.inUse = false
            this.withdrawToUser = ""
        }
    }

    private fun devolutionToSend(
        bikeHolder: Bike,
        devolutionDate: String,
        quiz: Quiz
    ): Devolution {
        val lastWithdraw = bikeHolder.getLastWithdraw()
        return Devolution(
            id = "-" + generateRandomAlphanumeric(),
            withdrawId = lastWithdraw?.id.orEmpty(),
            date = devolutionDate,
            user = lastWithdraw?.user,
            quiz = quiz
        )
    }

    private suspend fun updateUserWithInactiveWithdraw(devolution: Devolution): SimpleResult<AddDataResponse> {
        val user: User? = devolution.user
        user?.hasActiveWithdraw = false
        return userRepository.updateUser(user!!)
    }
}