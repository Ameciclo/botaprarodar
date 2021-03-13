package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.presentation.bikewithdraw.GetAvailableBikesException
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class SelectBikeViewModel @ExperimentalCoroutinesApi constructor(
    private val bikeHolder: BikeHolder,
    private val stepperAdapter: WithdrawStepper,
    private val getAvailableBikes: GetAvailableBikes
) : ViewModel() {
    private val _availableBikes = MutableLiveData<List<Bike>>()
    val availableBikes: LiveData<List<Bike>>
        get() = _availableBikes

    fun setInitialStep() {
        stepperAdapter.setCurrentStep(StepConfigType.SELECT_BIKE)
    }

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun getBikeList(communityId: String) {
        viewModelScope.launch {
            try {
                _availableBikes.value = getAvailableBikes.execute(communityId)
            }catch (e: GetAvailableBikesException){

            }
        }
    }

    fun setBike(bike: Bike) {
        bikeHolder.bike = bike
    }
}