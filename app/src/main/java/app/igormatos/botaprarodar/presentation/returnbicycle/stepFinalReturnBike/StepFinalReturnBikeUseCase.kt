package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import app.igormatos.botaprarodar.common.extensions.convertToBikeRequest
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.local.quiz.DevolutionQuizAnswerName
import app.igormatos.botaprarodar.data.repository.DevolutionBikeRepository
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Quiz
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import com.brunotmgomes.ui.SimpleResult

class StepFinalReturnBikeUseCase(val devolutionRepository: DevolutionBikeRepository) {

    suspend fun addDevolution(
        devolutionDate: String,
        bikeHolder: BikeHolder,
        quizBuilder: BikeDevolutionQuizBuilder
    ): SimpleResult<AddDataResponse> {
        val quiz = quizToSend(quizBuilder)
        val devolution = devolutionToSend(bikeHolder, devolutionDate, quiz)
        updateBikeToSend(bikeHolder, devolution)
        val bikeRequest = bikeHolder.bike?.convertToBikeRequest()!!
        return devolutionRepository.addDevolution(bikeRequest)
    }

    private fun updateBikeToSend(
        bikeHolder: BikeHolder,
        devolution: Devolution
    ) {
        bikeHolder.bike?.devolutions?.add(devolution)
        bikeHolder.bike?.inUse = false
    }

    private fun devolutionToSend(
        bikeHolder: BikeHolder,
        devolutionDate: String,
        quiz: Quiz
    ): Devolution {
        return Devolution(
            id = bikeHolder.bike?.id + "devolution",
            date = devolutionDate,
            user = bikeHolder.bike?.withdraws?.get(0)?.user,
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
}