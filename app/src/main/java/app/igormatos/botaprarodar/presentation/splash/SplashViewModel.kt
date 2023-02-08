package app.igormatos.botaprarodar.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule

class SplashViewModel(
    preferencesModule: SharedPreferencesModule,
    firebaseAuthModule: FirebaseAuthModule,
    firebaseHelperModule: FirebaseHelperModule
) : ViewModel() {

    private val _userLoginState = MutableLiveData<UserLoginState>()
    val userLoginState: LiveData<UserLoginState>
        get() = _userLoginState

    init {
        val isLogged = firebaseAuthModule.getCurrentUser() != null

        if (isLogged) {
            if (!preferencesModule.isCommunitySelected()) {
                _userLoginState.postValue(UserLoginState.PartiallyLoggedIn)
            } else {
                val community = preferencesModule.getJoinedCommunity()
                firebaseHelperModule.setCommunityId(community.id)
                _userLoginState.postValue(UserLoginState.LoggedIn)
            }
        } else {
            _userLoginState.postValue(UserLoginState.LoggedOut)
        }
    }

    sealed class UserLoginState {
        object LoggedIn : UserLoginState()
        object PartiallyLoggedIn : UserLoginState()
        object LoggedOut : UserLoginState()
    }
}