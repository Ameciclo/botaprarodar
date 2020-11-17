package app.igormatos.botaprarodar.screens.createcommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.common.util.isValidEmail
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener

class AddCommunityViewModel(
    private var firebaseHelperModule: FirebaseHelperModule,
) : ViewModel() {

    private val loading = MutableLiveData<Boolean>()
    fun getLoadingValue() : LiveData<Boolean> = loading

    private val success = MutableLiveData<Boolean>()
    fun getSuccessValue() : LiveData<Boolean> = success

    private val communityData = MutableLiveData<Community>()
    fun getCommunityDataValue() : LiveData<Community> = communityData

    private lateinit var community : Community

    fun createCommunity(
        name: String,
        description: String,
        address: String,
        orgName: String,
        orgEmail: String
    ) {
        community = Community(name, description, address, orgName, orgEmail)
        communityData.value = community
    }

    fun sendCommunityToServer() {
        loading.value = true
        firebaseHelperModule.addCommunity(
            community,
            object : SingleRequestListener<Boolean> {
                override fun onStart() {
                }

                override fun onCompleted(result: Boolean) {
                    loading.value = false
                    success.value = result
                }

                override fun onError(error: RequestError) {
                    loading.value = false
                    success.value = false
                }

            }
        )
    }

}