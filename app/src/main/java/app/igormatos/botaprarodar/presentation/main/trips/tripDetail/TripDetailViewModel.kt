package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.extensions.convertToBike
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class TripDetailViewModel(private val tripDetailUseCase: TripDetailUseCase) : ViewModel() {

    private val _bike = MutableLiveData<SimpleResult<Bike>>()
    val bike: LiveData<SimpleResult<Bike>>
        get() = _bike

    fun getBikeById(bikeId: String) {
        viewModelScope.launch {
            when (val response = tripDetailUseCase.getBikeById(bikeId)) {
                is SimpleResult.Success -> {
                    val bike = response.data.convertToBike()
                    _bike.postValue(SimpleResult.Success(bike))

                }
                is SimpleResult.Error -> {
                    _bike.postValue(response)
                }
            }
        }
    }

    fun getWithdrawById(bike: Bike, id: String) = tripDetailUseCase.getWithdrawById(bike, id)

    fun getDevolutionById(bike: Bike, id: String) = tripDetailUseCase.getDevolutionById(bike, id)

    fun getDevolutionByWithdrawId(bike: Bike, id: String) =
        tripDetailUseCase.getDevolutionByWithdrawId(bike, id)

    fun verifyIfBikeIsInUse(bike: Bike) = tripDetailUseCase.verifyIfBikeIsInUse(bike)
}