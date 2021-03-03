package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow

class ReturnBikeViewModel(val stepper: StepperAdapter) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(stepper.currentStep.value)
    // The UI collects from this StateFlow to get its state updates
    val uiState = stepper.currentStep.asLiveData()


}
