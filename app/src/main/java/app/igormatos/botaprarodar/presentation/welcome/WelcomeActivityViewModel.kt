package app.igormatos.botaprarodar.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.domain.model.UserCommunityInfo
import app.igormatos.botaprarodar.domain.model.community.Community
import com.brunotmgomes.ui.ViewEvent

abstract class WelcomeActivityViewModel: ViewModel() {

    abstract val navigateMain: LiveData<ViewEvent<Boolean>>

    abstract val loadedCommunities: LiveData<ViewEvent<Result<UserCommunityInfo>>>

    abstract val loading: LiveData<Boolean>

    abstract val showResendEmailSnackBar: LiveData<ViewEvent<Boolean>>

    abstract fun retryLoadCommunities()

    abstract fun checkPreviousState()

    abstract fun onUserLoggedIn()

    abstract fun chooseCommunity(community: Community)

    abstract fun sendEmailVerification()


}