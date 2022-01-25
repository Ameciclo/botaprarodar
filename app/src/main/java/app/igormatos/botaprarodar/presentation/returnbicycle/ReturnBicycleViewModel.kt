package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
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