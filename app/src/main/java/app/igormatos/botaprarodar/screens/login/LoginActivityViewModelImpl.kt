package app.igormatos.botaprarodar.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.igormatos.botaprarodar.data.model.UserCommunityInfo
import app.igormatos.botaprarodar.local.SharedPreferencesModule
import app.igormatos.botaprarodar.network.*
import com.brunotmgomes.ui.ViewEvent
import com.google.firebase.auth.FirebaseAuth

class LoginActivityViewModelImpl(
    private val preferencesModule: SharedPreferencesModule,
    private val firebaseAuthModule: FirebaseAuthModule,
    private val firebaseHelperModule: FirebaseHelperModule
) : LoginActivityViewModel() {

    private val _navigateMain = MutableLiveData<ViewEvent<Boolean>>()
    override val navigateMain: LiveData<ViewEvent<Boolean>>
        get() = _navigateMain

    private val _loadedCommunities = MutableLiveData<ViewEvent<UserCommunityInfo>>()
    override val loadedCommunities: LiveData<ViewEvent<UserCommunityInfo>>
        get() = _loadedCommunities

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    override val loading: LiveData<Boolean>
        get() = _loading

    private val _showResendEmailSnackBar = MutableLiveData<ViewEvent<Boolean>>()
    override val showResendEmailSnackBar: LiveData<ViewEvent<Boolean>>
        get() = _showResendEmailSnackBar

    override fun checkPreviousState() {
        if (isLogged() && isCommunitySelected()) {
            goToMainActivity()
        } else if (isLogged()) {
            loadChooseCommunity()
        }
    }

    override fun onUserLoggedIn() {
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
                            UserCommunityInfo(
                                isAdmin = isAdmin,
                                communities = communities
                            )
                        )
                    )
                }

                override fun onError(error: RequestError) {
                    _loading.postValue(false)
                    _loadedCommunities.postValue(
                        ViewEvent(
                            UserCommunityInfo(
                                isAdmin = false,
                                communities = emptyList()
                            )
                        )
                    )
                    FirebaseAuth.getInstance().signOut()
                }

            }
        )

    }

    override fun chooseCommunity(community: Community) {
        preferencesModule.saveJoinedCommmunity(community)
        firebaseHelperModule.setCommunityId(community.id!!)
        goToMainActivity()
    }

    //todo: handle exceptions
    override fun sendEmailVerification() {
        val currentUser = firebaseAuthModule.getCurrentUser()!!
        currentUser.sendEmailVerification().addOnCompleteListener {
            _showResendEmailSnackBar.postValue(ViewEvent(false))
        }
    }

    private fun goToMainActivity() {
        firebaseHelperModule.setCommunityId(preferencesModule.getJoinedCommunity().id!!)
        _navigateMain.postValue(ViewEvent(true))
    }

    private fun isCommunitySelected(): Boolean {
        return preferencesModule.isCommunitySelected()
    }

    private fun isLogged(): Boolean {
        return firebaseAuthModule.getCurrentUser() != null
    }

}