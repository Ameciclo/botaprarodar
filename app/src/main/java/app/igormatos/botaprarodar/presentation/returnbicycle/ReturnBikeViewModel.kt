package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.adapter.StepperAdapter

class ReturnBikeViewModel(val stepper: ReturnStepper) : ViewModel() {
    val uiState = stepper.currentStep.asLiveData()

    fun navigateToPrevious(){
        stepper.navigateToPrevious()
    }

    fun backToInitialState(){
        stepper.setCurrentStep(StepConfigType.SELECT_BIKE)
    }
}
