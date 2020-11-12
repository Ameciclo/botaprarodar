package app.igormatos.botaprarodar.screens.createcommunity

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.common.BprApplication
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.network.FirebaseHelperModule
import app.igormatos.botaprarodar.network.RequestError
import app.igormatos.botaprarodar.network.SingleRequestListener
import org.koin.dsl.koinApplication
import kotlin.reflect.full.declaredMemberExtensionProperties
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.valueParameters

class AddCommunityViewModel(private var firebaseHelperModule: FirebaseHelperModule) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val inputFieldsWarning = MutableLiveData<Boolean>()
    val communityData = MutableLiveData<Community>()
    val community = Community()

    fun addCommunity() {
        if (inputsFilled()) {
            getCommunityFromInputs()
        } else {
            inputFieldsWarning.value = true
        }
    }

    fun sendCommunityToServer() {
        firebaseHelperModule.addCommunity(
            community,
            object : SingleRequestListener<Boolean> {
                override fun onStart() {
                    loading.value = true
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

    fun inputsFilled() : Boolean {
        return when {
            community.name.isNullOrEmpty() -> false
            community.address.isNullOrEmpty() -> false
            community.description.isNullOrEmpty() -> false
            community.org_email.isNullOrEmpty() -> false
            community.org_name.isNullOrEmpty() -> false
            else -> true
        }
    }

    fun getCommunityFromInputs() {
        communityData.value = community
    }

}