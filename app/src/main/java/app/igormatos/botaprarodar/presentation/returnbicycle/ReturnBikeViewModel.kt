package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class ReturnBikeViewModel(val stepper: StepperAdapter.ReturnStepper) : ViewModel() {
    val uiState = stepper.currentStep.asLiveData()

    fun navigateToPrevious(){
        stepper.navigateToPrevious()
    }
}
