package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.extensions.convertToBike
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class TripDetailViewModel(private val tripDetailUseCase: TripDetailUseCase) : ViewModel() {

    private val _bike = MutableLiveData<SimpleResult<Bike>>()
    val bike: LiveData<SimpleResult<Bike>>
        get() = _bike

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    fun getBikeById(bikeId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val response = tripDetailUseCase.getBikeById(bikeId)) {
                is SimpleResult.Success -> {
                    val bike = response.data.convertToBike()
                    _bike.postValue(SimpleResult.Success(bike))
                    _uiState.value = UiState.Success
                }
                is SimpleResult.Error -> {
                    _bike.postValue(response)
                    _uiState.value = UiState.Error(response.exception.message.toString())
                }
            }
        }
    }

    fun getWithdrawById(bike: Bike, id: String) = tripDetailUseCase.getWithdrawById(bike, id)

    fun getDevolutionById(bike: Bike, id: String) = tripDetailUseCase.getDevolutionById(bike, id)

    fun getDevolutionByWithdrawId(bike: Bike, id: String) =
        tripDetailUseCase.getDevolutionByWithdrawId(bike, id)

    fun verifyIfBikeIsInUse(bike: Bike) = tripDetailUseCase.verifyIfBikeIsInUse(bike)

    fun verifyIfIsWithdraw(status: String) = (status.equals(IS_WITHDRAW, true))

    fun returnWithdrawAndDevolution(
        status: String,
        id: String,
        bike: Bike
    ): Pair<Withdraws?, Devolution?> {
        return if (verifyIfIsWithdraw(status).not()) {
            val devolution = getDevolutionById(bike, id)
            val withdraw = devolution?.withdrawId?.let { getWithdrawById(bike, it) }
            Pair(withdraw, devolution)
        } else {
            val devolution = getDevolutionByWithdrawId(bike, id)
            val withdraw = getWithdrawById(bike, id)
            Pair(withdraw, devolution)
        }
    }

    companion object {
        const val IS_WITHDRAW = "empréstimo"
    }
}