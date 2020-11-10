package app.igormatos.botaprarodar.screens.login

import androidx.lifecycle.MutableLiveData
import app.igormatos.botaprarodar.data.model.UserCommunityInfo
import app.igormatos.botaprarodar.network.Community
import com.brunotmgomes.ui.ViewEvent

class LoginActivityViewModelFake(
    override val navigateMain: MutableLiveData<ViewEvent<Boolean>> = MutableLiveData(),
    override val loadedCommunities: MutableLiveData<ViewEvent<UserCommunityInfo>> = MutableLiveData(),
    override val loading: MutableLiveData<Boolean> = MutableLiveData(),
    override val showResendEmailSnackBar: MutableLiveData<ViewEvent<Boolean>> = MutableLiveData(),
) : LoginActivityViewModel() {

    var loggedIn = false
    var chosenCommunity: Community? = null

    override fun checkPreviousState() {}

    override fun onUserLoggedIn() {
        loggedIn = true
    }

    override fun chooseCommunity(community: Community) {
        chosenCommunity = community
    }

    override fun sendEmailVerification(){}

    fun finishEmailResend(){
        showResendEmailSnackBar.postValue(ViewEvent(false))
    }

}