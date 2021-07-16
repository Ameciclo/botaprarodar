package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.common.extensions.convertToBikeRequest
import app.igormatos.botaprarodar.common.extensions.getLastWithdraw
import app.igormatos.botaprarodar.common.utils.generateRandomAlphanumeric
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.local.quiz.DevolutionQuizAnswerName
import app.igormatos.botaprarodar.data.repository.DevolutionBikeRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult

class StepFinalReturnBikeUseCase(
    private val devolutionRepository: DevolutionBikeRepository,
    private val userRepository: UserRepository
) {

    suspend fun addDevolution(
        devolutionDate: String,
        bikeHolder: BikeHolder,
        quizBuilder: BikeDevolutionQuizBuilder
    ): SimpleResult<AddDataResponse> {
        val quiz = quizToSend(quizBuilder)
        val devolution = devolutionToSend(bikeHolder, devolutionDate, quiz)
        updateBikeToSend(bikeHolder, devolution)
        val bikeRequest = bikeHolder.bike?.convertToBikeRequest()!!

        val addDevolutionResponse: SimpleResult<AddDataResponse> =
            devolutionRepository.addDevolution(bikeRequest)

        if (addDevolutionResponse is SimpleResult.Success)
            return updateUserWithInactiveWithdraw(devolution)

        return addDevolutionResponse
    }

    private fun updateBikeToSend(
        bikeHolder: BikeHolder,
        devolution: Devolution
    ) {
        with(bikeHolder.bike!!) {
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
        bikeHolder: BikeHolder,
        devolutionDate: String,
        quiz: Quiz
    ): Devolution {
        return Devolution(
            id = "-" + generateRandomAlphanumeric(),
            withdrawId = bikeHolder.bike?.getLastWithdraw()?.id.orEmpty(),
            date = devolutionDate,
            user = bikeHolder.bike?.getLastWithdraw()?.user,
            quiz = quiz
        )
    }

    private fun quizToSend(quizBuilder: BikeDevolutionQuizBuilder): Quiz {
        val quiz = Quiz()
        quizBuilder.build().answerList.map {
            when (it.quizName) {
                DevolutionQuizAnswerName.REASON -> {
                    quiz.reason = it.value.toString()
                }
                DevolutionQuizAnswerName.DESTINATION -> {
                    quiz.destination = it.value.toString()
                }
                DevolutionQuizAnswerName.GIVE_RIDE -> {
                    quiz.giveRide = it.value.toString()
                }
                DevolutionQuizAnswerName.SUFFERED_VIOLENCE -> {
                    quiz.problemsDuringRiding = it.value.toString()
                }
            }
        }
        return quiz
    }

    private suspend fun updateUserWithInactiveWithdraw(devolution: Devolution): SimpleResult<AddDataResponse> {
        val user: User? = devolution.user
        user?.hasActiveWithdraw = false
        return userRepository.updateUser(user!!)
    }
}