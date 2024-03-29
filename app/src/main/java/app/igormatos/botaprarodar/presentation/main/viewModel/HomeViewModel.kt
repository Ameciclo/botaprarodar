package app.igormatos.botaprarodar.presentation.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.presentation.main.HomeUiState
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val bikesUseCase: BikesUseCase,
    private val usersUseCase: UsersUseCase
) : ViewModel() {

    private var _uiState = MutableLiveData(HomeUiState())
    val uiState: LiveData<HomeUiState>
        get() = _uiState

    private var _users = MutableLiveData(emptyList<User>())
    val users: LiveData<List<User>>
        get() = _users

    private var _bikes = MutableLiveData(emptyList<Bike>())
    val bikes: LiveData<List<Bike>>
        get() = _bikes

    @ExperimentalCoroutinesApi
    fun getBikes(communityId: String) {
        viewModelScope.launch {
            when (val bikes = bikesUseCase.getBikes(communityId)) {
                is SimpleResult.Success -> {
                    _uiState.value = HomeUiState.fromBikes(bikes.data)
                    _bikes.value = bikes.data
                }
                is SimpleResult.Error -> {}
            }
        }
    }

    fun getUsers(communityId: String) {
        viewModelScope.launch {
            when (val users = usersUseCase.getAvailableUsersByCommunityId(communityId)) {
                is SimpleResult.Success -> {
                    _users.value = users.data
                }
                else -> {}
            }
        }
    }
}
