package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike.ReturnBikeQuizViewModel
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ReturnBicycleViewModel(
    val stepperAdapter: ReturnStepper,
    private val stepOneReturnBikeUseCase: StepOneReturnBikeUseCase,
    private val bikeHolder: BikeHolder
) : ViewModel() {
    private val INITIAL_VALUE: String = ""
    private val _bikesAvailableToReturn = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikesAvailableToReturn: LiveData<SimpleResult<List<Bike>>>
        get() = _bikesAvailableToReturn
    private val _bikesAvailable: MutableLiveData<List<Bike>> = MutableLiveData<List<Bike>>()
    val bikesAvailable
        get() = _bikesAvailable

    val _uiStep = MutableLiveData<StepConfigType>()
    val uiStep: LiveData<StepConfigType>
        get() = _uiStep

    val reason = MutableLiveData(INITIAL_VALUE)
    val problemsDuringRidingRg = MutableLiveData(INITIAL_VALUE)
    val needTakeRideRg = MutableLiveData(INITIAL_VALUE)
    val whichDistrict = MutableLiveData(INITIAL_VALUE)

    val formIsEnable = MediatorLiveData<Boolean>().apply {
        addSource(reason) { validateForm() }
        addSource(problemsDuringRidingRg) { validateForm() }
        addSource(needTakeRideRg) { validateForm() }
        addSource(whichDistrict) { validateForm() }
    }

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
            when (value) {
                is SimpleResult.Success -> {
                    bikesAvailable.value = value.data
                }
                is SimpleResult.Error -> {
                    value.exception
                }
            }
        }
    }

    fun setBike(bike: Bike) {
        bikeHolder.bike = bike
    }

    private fun validateForm() {
        formIsEnable.value = reason.value.isNotNullOrNotBlank() &&
                problemsDuringRidingRg.value.isRadioValid() &&
                needTakeRideRg.value.isRadioValid() &&
                isTextValid(whichDistrict.value)

    }

    private fun String?.isRadioValid() = this != INITIAL_VALUE

    private fun isTextValid(data: String?) = !data.isNullOrBlank()
}