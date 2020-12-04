package app.igormatos.botaprarodar.presentation.login

import androidx.lifecycle.MutableLiveData
import app.igormatos.botaprarodar.domain.model.UserCommunityInfo
import app.igormatos.botaprarodar.domain.model.community.Community
import com.brunotmgomes.ui.ViewEvent

class LoginActivityViewModelFake(
    override val navigateMain: MutableLiveData<ViewEvent<Boolean>> = MutableLiveData(),
    override val loadedCommunities: MutableLiveData<ViewEvent<Result<UserCommunityInfo>>> = MutableLiveData(),
    override val loading: MutableLiveData<Boolean> = MutableLiveData(),
    override val showResendEmailSnackBar: MutableLiveData<ViewEvent<Boolean>> = MutableLiveData(),
) : LoginActivityViewModel() {

    var loggedIn = false
    var chosenCommunity: Community? = null
    var retryLoadedCommunity: Boolean = false

    override fun checkPreviousState() {}

    override fun onUserLoggedIn() {
        loggedIn = true
    }

    override fun chooseCommunity(community: Community) {
        chosenCommunity = community
    }

    override fun sendEmailVerification(){}

    override fun retryLoadCommunities() {
        retryLoadedCommunity = true
    }

    fun finishEmailResend(){
        showResendEmailSnackBar.postValue(ViewEvent(false))
    }

}