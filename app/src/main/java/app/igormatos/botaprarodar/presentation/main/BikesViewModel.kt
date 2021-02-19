package app.igormatos.botaprarodar.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BikesViewModel(private val bikesUseCase: BikesUseCase) : ViewModel() {

    private val _bikes = MutableLiveData<List<Bike>>()
    val bikes: LiveData<List<Bike>> = _bikes

    @ExperimentalCoroutinesApi
    fun getBikes(communityId: String) {
        viewModelScope.launch {
            bikesUseCase.getBikes(communityId).collect {
                _bikes.postValue(it)
            }
        }
    }
}