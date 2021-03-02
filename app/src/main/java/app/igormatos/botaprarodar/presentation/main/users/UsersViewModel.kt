package app.igormatos.botaprarodar.presentation.main.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class UsersViewModel(private val usersUseCase: UsersUseCase) : ViewModel() {

    private val _users = MutableLiveData<SimpleResult<List<User>>>()
    val users: LiveData<SimpleResult<List<User>>> = _users

    fun getUsers(communityId: String) {
        viewModelScope.launch {
            usersUseCase.getUsers(communityId)
                .catch { }
                .collect {
                    _users.postValue(it)
                }
        }
    }
}