package app.igormatos.botaprarodar.presentation.main.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.common.extensions.convertToBikeList
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TripsViewModel(
    private val bikeActionUseCase: BikeActionUseCase,
) : ViewModel() {

    private val bikeActionLiveData = MutableLiveData<List<BikeActionsMenuType>>()

    private val _trips = MutableLiveData<SimpleResult<List<TripsItemType>>>()
    val trips: LiveData<SimpleResult<List<TripsItemType>>>
        get() = _trips

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    fun loadBikeActions() {
        bikeActionLiveData.postValue(bikeActionUseCase.getBikeActionsList())
    }

    fun getBikeActions(): LiveData<List<BikeActionsMenuType>> = bikeActionLiveData

    fun getBikes(communityId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            when (val result = bikeActionUseCase.getBikes(communityId)) {
                is SimpleResult.Success -> {
                    val bikeList = result.data.convertToBikeList()
                    val tripsBikeType = bikeActionUseCase.convertBikesToTripsItem(bikeList)
                    val trips =
                        bikeActionUseCase.createTitleTripsItem(tripsBikeType.data as MutableList<TripsItemType>)
                    _trips.value = trips
                    _uiState.value = UiState.Success
                }
                is SimpleResult.Error -> {
                    _trips.value = result
                    _uiState.value = UiState.Error(result.exception.message.toString())
                }
            }
        }
    }
}