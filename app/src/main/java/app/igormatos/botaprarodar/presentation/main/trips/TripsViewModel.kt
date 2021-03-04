package app.igormatos.botaprarodar.presentation.main.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase

class TripsViewModel(
    private val bikeActionUseCase: BikeActionUseCase
): ViewModel() {

    private val bikeActionLiveData = MutableLiveData<List<BikeActionsMenuType>>()

    fun loadBikeActions(){
        bikeActionLiveData.postValue(bikeActionUseCase.getBikeActionsList())
    }

    fun getBikeActions(): LiveData<List<BikeActionsMenuType>> = bikeActionLiveData
}