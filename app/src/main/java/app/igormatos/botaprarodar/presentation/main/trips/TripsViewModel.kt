package app.igormatos.botaprarodar.presentation.main.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.common.extensions.convertToBikeList
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TripsViewModel(
    private val bikeActionUseCase: BikeActionUseCase
) : ViewModel() {

    private val bikeActionLiveData = MutableLiveData<List<BikeActionsMenuType>>()

    private val _trips = MutableLiveData<SimpleResult<List<TripsItemType>>>()
    val trips: LiveData<SimpleResult<List<TripsItemType>>>
        get() = _trips

    fun loadBikeActions() {
        bikeActionLiveData.postValue(bikeActionUseCase.getBikeActionsList())
    }

    fun getBikeActions(): LiveData<List<BikeActionsMenuType>> = bikeActionLiveData

    fun getBikes(communityId: String) {
        viewModelScope.launch {
            bikeActionUseCase.getBikes(communityId)
                .collect {
                    when (it) {
                        is SimpleResult.Success -> {
                            val bikeList = it.data.convertToBikeList()
                            val response = bikeActionUseCase.teste(bikeList)
                            _trips.value = response
                        }
                        is SimpleResult.Error -> {}
                    }
                }
        }
    }
}