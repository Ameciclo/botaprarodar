package app.igormatos.botaprarodar.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule

class SplashViewModel(
    private val preferencesModule: SharedPreferencesModule,
    private val firebaseAuthModule: FirebaseAuthModule,
    private val firebaseHelperModule: FirebaseHelperModule
) : ViewModel() {

    private val _userloginState = MutableLiveData<UserLoginState>()
    val userloginState: LiveData<UserLoginState>
        get() = _userloginState

    fun verifyUserLoginState() {
        if (isLogged()) {
            if (isCommunitySelected().not()) {
                _userloginState.postValue(UserLoginState.PartiallyLoggedIn)
            } else {
                val community = preferencesModule.getJoinedCommunity()
                firebaseHelperModule.setCommunityId(community.id)
                _userloginState.postValue(UserLoginState.LoggedIn)
            }
        } else {
            _userloginState.postValue(UserLoginState.NotLoggedIn)
        }
    }

    private fun isLogged() = firebaseAuthModule.getCurrentUser() != null

    private fun isCommunitySelected() = preferencesModule.isCommunitySelected()

    sealed class UserLoginState {
        object LoggedIn : UserLoginState()
        object PartiallyLoggedIn : UserLoginState()
        object NotLoggedIn : UserLoginState()
    }
}