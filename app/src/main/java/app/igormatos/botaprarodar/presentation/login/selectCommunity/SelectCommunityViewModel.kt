package app.igormatos.botaprarodar.presentation.login.selectCommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.SharedPreferencesModule
import app.igormatos.botaprarodar.data.network.RequestError
import app.igormatos.botaprarodar.data.network.SingleRequestListener
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.data.network.firebase.FirebaseHelperModule
import app.igormatos.botaprarodar.domain.model.community.Community

class SelectCommunityViewModel(
    private val firebaseAuthModule: FirebaseAuthModule,
    private val firebaseHelperModule: FirebaseHelperModule,
    private val preferencesModule: SharedPreferencesModule
) : ViewModel() {

    private val _selectCommunityState = MutableLiveData<SelectCommunityState>()
    val selectCommunityState: LiveData<SelectCommunityState>
        get() = _selectCommunityState

    fun loadcommunities() {
        val currentUser = firebaseAuthModule.getCurrentUser()!!
        firebaseHelperModule.getCommunities(
            currentUser.uid,
            currentUser.email!!,
            object : SingleRequestListener<Pair<Boolean, List<Community>>> {
                override fun onStart() {
                    _selectCommunityState.postValue(SelectCommunityState.Loading)
                }

                override fun onCompleted(result: Pair<Boolean, List<Community>>) {
                    val isAdmin = result.first
                    val communities = result.second
                    val successUserState = verifiySuccessUserState(isAdmin, communities)
                    _selectCommunityState.postValue(successUserState)
                }

                override fun onError(error: RequestError) {
                    _selectCommunityState.postValue(
                        SelectCommunityState.Error(error)
                    )
                }
            }
        )
    }

    private fun verifiySuccessUserState(
        isAdmin: Boolean,
        communities: List<Community>
    ): SelectCommunityState.Success {
        return when {
            isAdmin -> {
                SelectCommunityState.Success(UserInfoState.Admin(communities))
            }
            communities.isNotEmpty() -> {
                SelectCommunityState.Success(UserInfoState.NotAdmin(communities))
            }
            else -> {
                SelectCommunityState.Success(UserInfoState.NotAdminWithoutCommunities)
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