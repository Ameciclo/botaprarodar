package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow

class ReturnBikeViewModel(val stepper: StepperAdapter.ReturnStepper) : ViewModel() {
    val uiState = stepper.currentStep.asLiveData()
}
