package app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class StepOneReturnBikeViewModel(
    val stepperAdapter: StepperAdapter.ReturnStepper,
    private val stepOneReturnBikeUseCase: StepOneReturnBikeUseCase
) : ViewModel() {

    private val _bikesAvailableToReturn = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikesAvailableToReturn: LiveData<SimpleResult<List<Bike>>>
        get() = _bikesAvailableToReturn

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun getBikesInUseToReturn(communityId: String) {
        viewModelScope.launch {
            val value = stepOneReturnBikeUseCase.getBikesInUseToReturn(communityId)
            _bikesAvailableToReturn.value = value
        }
    }

    fun setReturnBike(bike: Bike) {
        stepOneReturnBikeUseCase.setReturnBike(bike)
    }
}