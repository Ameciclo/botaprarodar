package app.igormatos.botaprarodar.presentation.login.selectCommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.domain.model.community.Community
import kotlinx.coroutines.launch

class SelectCommunityViewModel(
    private val firebaseAuthModule: FirebaseAuthModule,
    private val preferencesModule: SharedPreferencesModule,
    private val selectCommunityUseCase: SelectCommunityUseCase,
) : ViewModel() {

    private val _selectCommunityState = MutableLiveData<SelectCommunityState>()
    val selectCommunityState: LiveData<SelectCommunityState> = _selectCommunityState

    fun loadCommunities() {
        _selectCommunityState.value = SelectCommunityState.Loading
        viewModelScope.launch {
            val userLogged = firebaseAuthModule.getCurrentUser()
            if (userLogged != null) {
                val selectCommunityState: SelectCommunityState =
                    selectCommunityUseCase.loadCommunitiesByAdmin(
                        userLogged.uid,
                        userLogged.email
                    )
                _selectCommunityState.postValue(selectCommunityState)
            } else {
                _selectCommunityState.postValue(SelectCommunityState.Error(BprErrorType.UNKNOWN))
            }
        }
    }

    fun saveJoinedCommmunity(community: Community) {
        preferencesModule.saveJoinedCommmunity(community)
    }

    fun forceUserLogout() {
        firebaseAuthModule.signOut()
        preferencesModule.clear()
    }
}