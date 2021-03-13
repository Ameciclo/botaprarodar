package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper

class BikeWithdrawViewModel(private val stepper: WithdrawStepper) : ViewModel() {
    val uiState = stepper.currentStep.asLiveData()

    fun navigateToPrevious(){
        stepper.navigateToPrevious()
    }

    fun backToInitialState(){
        stepper.setCurrentStep(StepConfigType.SELECT_BIKE)
    }
}