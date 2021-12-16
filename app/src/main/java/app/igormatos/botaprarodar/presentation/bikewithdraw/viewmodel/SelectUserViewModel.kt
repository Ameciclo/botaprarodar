package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.domain.usecase.users.ValidateUserWithdraw
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SelectUserViewModel(
    private val userHolder: UserHolder,
    private val stepperAdapter: WithdrawStepper,
    private val usersUseCase: UsersUseCase,
    private val validateUserWithdraw: ValidateUserWithdraw,
) : ViewModel() {
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun getUserList(communityId: String) {
        viewModelScope.launch {
            when(val result = usersUseCase.getAvailableUsersByCommunityId(communityId)) {
                is SimpleResult.Success -> {
                    result.data.forEach {
                        validateUserWithdraw(it)
                    }
                    if(!result.data.isNullOrEmpty()){
                        _userList.postValue(result.data!!)
                    } else {
                        _userList.postValue(arrayListOf())
                    }
                }
                is SimpleResult.Error -> {
                    throw Exception()
                }
            }
        }
    }


    fun setUser(user: User) {
        userHolder.user = user
    }

    private suspend fun validateUserWithdraw(user: User): Boolean {
        return validateUserWithdraw.execute(user)
    }

    fun filterBy(word: String) {
       val lista = userList.value?.filter { user -> user.name!!.lowercase().contains(word.lowercase()) }
        _userList.value = lista!!
    }
}