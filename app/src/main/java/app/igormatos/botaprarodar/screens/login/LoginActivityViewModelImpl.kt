package app.igormatos.botaprarodar.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.igormatos.botaprarodar.data.model.RequestException
import app.igormatos.botaprarodar.data.model.UserCommunityInfo
import app.igormatos.botaprarodar.local.SharedPreferencesModule
import app.igormatos.botaprarodar.network.*
import com.brunotmgomes.ui.ViewEvent

class LoginActivityViewModelImpl(
    private val preferencesModule: SharedPreferencesModule,
    private val firebaseAuthModule: FirebaseAuthModule,
    private val firebaseHelperModule: FirebaseHelperModule
) : LoginActivityViewModel() {

    private val _navigateMain = MutableLiveData<ViewEvent<Boolean>>()
    override val navigateMain: LiveData<ViewEvent<Boolean>>
        get() = _navigateMain

    private val _loadedCommunities = MutableLiveData<ViewEvent<Result<UserCommunityInfo>>>()
    override val loadedCommunities: LiveData<ViewEvent<Result<UserCommunityInfo>>>
        get() = _loadedCommunities

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    override val loading: LiveData<Boolean>
        get() = _loading

    private val _showResendEmailSnackBar = MutableLiveData<ViewEvent<Boolean>>()
    override val showResendEmailSnackBar: LiveData<ViewEvent<Boolean>>
        get() = _showResendEmailSnackBar

    override fun checkPreviousState() {
        if (isLogged() && isCommunitySelected()) {
            val community = preferencesModule.getJoinedCommunity()
            goToMainActivity(community)
        } else if (isLogged()) {
            onUserLoggedIn()
        }
    }

    override fun onUserLoggedIn() {
        if (isEmailVerified()) {
            loadChooseCommunity()
        } else {
            _showResendEmailSnackBar.postValue(ViewEvent(true))
        }
    }

    override fun retryLoadCommunities() {
        loadChooseCommunity()
    }

    private fun loadChooseCommunity() {
        val currentUser = firebaseAuthModule.getCurrentUser()!!
        firebaseHelperModule.getCommunities(
            currentUser.uid,
            currentUser.email!!,
            object : SingleRequestListener<Pair<Boolean, List<Community>>> {
                override fun onStart() {
                    _loading.postValue(true)
                }

                override fun onCompleted(result: Pair<Boolean, List<Community>>) {
                    _loading.postValue(false)

                    val isAdmin = result.first
                    val communities = result.second
                    _loadedCommunities.postValue(
                        ViewEvent(
                            Result.success(
                                UserCommunityInfo(
                                    isAdmin = isAdmin,
                                    communities = communities
                                )
                            )
                        )
                    )
                }

                override fun onError(error: RequestError) {
                    _loading.postValue(false)
                    _loadedCommunities.postValue(
                        ViewEvent(
                            Result.failure(
                                RequestException(error)
                            )
                        )
                    )
                }
            }
        )

    }

    override fun chooseCommunity(community: Community) {
        preferencesModule.saveJoinedCommmunity(community)
        goToMainActivity(community)
    }

    //todo: handle exceptions
    override fun sendEmailVerification() {
        val currentUser = firebaseAuthModule.getCurrentUser()!!
        currentUser.sendEmailVerification().addOnCompleteListener {
            _showResendEmailSnackBar.postValue(ViewEvent(false))
        }
    }

    private fun goToMainActivity(community: Community) {
        firebaseHelperModule.setCommunityId(community.id!!)
        _navigateMain.postValue(ViewEvent(true))
    }

    private fun isCommunitySelected(): Boolean {
        return preferencesModule.isCommunitySelected()
    }

    private fun isLogged(): Boolean {
        return firebaseAuthModule.getCurrentUser() != null
    }

    private fun isEmailVerified(): Boolean {
        val user = firebaseAuthModule.getCurrentUser()
        return user?.isEmailVerified ?: false
    }

}