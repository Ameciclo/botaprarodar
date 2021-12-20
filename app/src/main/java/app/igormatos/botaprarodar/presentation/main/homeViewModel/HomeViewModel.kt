package app.igormatos.botaprarodar.presentation.main.homeViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(private val bikesUseCase: BikesUseCase) : ViewModel() {

    private var _uiState = MutableLiveData(HomeUiState())
    val uiState: LiveData<HomeUiState>
        get() = _uiState

    @ExperimentalCoroutinesApi
    fun getBikes(communityId: String) {
        viewModelScope.launch {
            when (val bikeslist = bikesUseCase.getBikes(communityId)) {
                is SimpleResult.Success -> {
                    _uiState.value = HomeUiState.fromBikes(bikeslist.data)
                }
                is SimpleResult.Error -> {}
            }
        }
    }
}