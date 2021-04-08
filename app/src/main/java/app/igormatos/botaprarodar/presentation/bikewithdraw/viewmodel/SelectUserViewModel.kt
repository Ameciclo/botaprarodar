package app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.UserHolder
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.users.GetUsersByCommunity
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SelectUserViewModel(
    private val userHolder: UserHolder,
    private val stepperAdapter: WithdrawStepper,
    private val getUsersByCommunity: GetUsersByCommunity
) : ViewModel() {
    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>>
        get() = _userList

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun getUserList(communityId: String) {
        viewModelScope.launch {
            getUsersByCommunity.execute(communityId)
                .catch { }
                .collect {
                    if (it is SimpleResult.Success) {
                        _userList.postValue(it.data)
                    }else{
                        _userList.postValue(emptyList())

                    }
                }
        }
    }

    fun setUser(user: User) {
        userHolder.user = user
    }
}