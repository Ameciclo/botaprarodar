package app.igormatos.botaprarodar.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import app.igormatos.botaprarodar.common.enumType.StepConfigType.USER_PERSONAl_INFO

class UserViewModel(val stepper: RegisterUserStepper) : ViewModel() {

    val uiState = stepper.currentStep.asLiveData()

    var isEditableAvailable = false

    fun navigateToPrevious(){
        stepper.navigateToPrevious()
    }

    fun backToInitialState(){
        stepper.setCurrentStep(USER_PERSONAl_INFO)
    }
}
