package app.igormatos.botaprarodar.presentation.main.bikes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.converter.bicycle.TesteConverter
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class BikesViewModel(private val bikesUseCase: BikesUseCase) : ViewModel() {

    private val _bikes = MutableLiveData<SimpleResult<List<Bike>>>()
    val bikes: LiveData<SimpleResult<List<Bike>>> = _bikes

    private val convertMapper = TesteConverter()

    fun getBikes(communityId: String) {
        viewModelScope.launch {
            bikesUseCase.getBikes(communityId)
                .collect {
                    when (it) {
                        is SimpleResult.Success -> {
                            val a = convertMapper.convert(it.data)
                            _bikes.postValue(SimpleResult.Success(a))
                        }
                        is SimpleResult.Error -> {
                            _bikes.postValue(it)
                        }
                    }
                }
        }
    }
}