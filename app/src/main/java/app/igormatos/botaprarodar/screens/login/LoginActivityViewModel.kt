package app.igormatos.botaprarodar.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.model.UserCommunityInfo
import app.igormatos.botaprarodar.network.Community
import com.brunotmgomes.ui.ViewEvent

abstract class LoginActivityViewModel: ViewModel() {

    abstract val navigateMain: LiveData<ViewEvent<Boolean>>

    abstract val loadedCommunities: LiveData<ViewEvent<UserCommunityInfo>>

    abstract val loading: LiveData<Boolean>

    abstract val showResendEmailSnackBar: LiveData<ViewEvent<Boolean>>

    abstract fun checkPreviousState()

    abstract fun onUserLoggedIn()

    abstract fun chooseCommunity(community: Community)

    abstract fun sendEmailVerification()


}