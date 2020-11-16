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

abstract class AddCommunityViewModel() : ViewModel() {

    abstract val loading: MutableLiveData<Boolean>

    abstract val success: MutableLiveData<Boolean>

    abstract val inputFieldsWarning: MutableLiveData<Boolean>

    abstract val emailFormatWarning: MutableLiveData<Boolean>

    abstract val communityData: MutableLiveData<Community>

    abstract val community: Community

    abstract fun addCommunity()

    abstract fun sendCommunityToServer()

    abstract fun inputsFilled() : Boolean

    abstract fun getCommunityFromInputs()

    abstract fun verifyEmailFormat() : Boolean

}