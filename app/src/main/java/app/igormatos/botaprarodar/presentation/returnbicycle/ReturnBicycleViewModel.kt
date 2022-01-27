package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ReturnBicycleViewModel(
    val stepperAdapter: ReturnStepper,
    private val stepOneReturnBikeUseCase: StepOneReturnBikeUseCase,
    private val bikeHolder: BikeHolder
) : ViewModel() {
    private val _bikesAvailableToReturn = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikesAvailableToReturn: LiveData<SimpleResult<List<Bike>>>
        get() = _bikesAvailableToReturn

    val _uiStep = MutableLiveData<StepConfigType>()
    val uiStep: LiveData<StepConfigType>
        get() = _uiStep

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
        _uiStep.value = stepperAdapter.currentStep.value
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
        _uiStep.value = stepperAdapter.currentStep.value
    }

    fun navigateToPrevious() {
        stepperAdapter.navigateToPrevious()
        _uiStep.value = stepperAdapter.currentStep.value
    }

    fun getBikesInUseToReturn(communityId: String) {
        viewModelScope.launch {
            val value = stepOneReturnBikeUseCase.getBikesInUseToReturn(communityId = communityId)
            _bikesAvailableToReturn.value = value
        }
    }

    fun setBike(bike: Bike) {
        bikeHolder.bike = bike
    }
}