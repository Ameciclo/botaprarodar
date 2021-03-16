package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StepFinalReturnBikeViewModel(
    private val bikeHolder: BikeHolder,
    val quizBuilder: BikeDevolutionQuizBuilder,
    val stepFinalUseCase: StepFinalReturnBikeUseCase
) : ViewModel() {

    private val date = Calendar.getInstance().time
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val devolutionDate = dateFormat.format(date)

    fun getBikeHolder() = bikeHolder.bike

    fun finalizeDevolution() {
        viewModelScope.launch {
            stepFinalUseCase.addDevolution(devolutionDate, bikeHolder, quizBuilder)
        }
    }

}