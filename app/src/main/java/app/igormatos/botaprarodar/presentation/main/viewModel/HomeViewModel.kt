package app.igormatos.botaprarodar.presentation.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var _uiState = MutableLiveData(HomeUiState(bikes = emptyList()))
    val uiState: LiveData<HomeUiState>
        get() = _uiState

    private var _originalUserList = listOf<User>()
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

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

    fun getUserList(communityId: String) {
        viewModelScope.launch {
            when (val result = usersUseCase.getAvailableUsersByCommunityId(communityId)) {
                is SimpleResult.Success -> {
                    if (!result.data.isNullOrEmpty()) {
                        _userList.postValue(result.data!!)
                        _originalUserList = result.data
                    } else {
                        _userList.postValue(arrayListOf())
                    }
                }
            }
        }
    }

    fun filterBy(word: String) {
        val lista =
            _originalUserList.filter { user ->
                user.name!!.lowercase().contains(word.lowercase())
            }
        _userList.value = lista!!
    }
}
