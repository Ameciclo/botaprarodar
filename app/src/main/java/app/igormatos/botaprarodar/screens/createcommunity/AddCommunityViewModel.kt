package app.igormatos.botaprarodar.screens.createcommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.network.Community

class AddCommunityViewModel(
    private val addCommunityInteractor: AddCommunityInteractor
) : ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    fun getLoadingLiveDataValue() : LiveData<Boolean> = loadingLiveData

    private val successLiveData = MutableLiveData<Boolean>()
    fun getSuccessLiveDataValue() : LiveData<Boolean> = successLiveData

    private val errorLiveData = MutableLiveData<Boolean>()
    fun getErrorLiveDataValue() : LiveData<Boolean> = errorLiveData

    fun sendCommunity(community: Community) {
        loadingLiveData.value = true
        when (addCommunityInteractor.addCommunityToServer(community)) {
            is Result.Success -> successStateHandler()
            is Result.Error -> errorStateHandler()
        }

    }

    private fun successStateHandler() {
        loadingLiveData.value = false
        successLiveData.value = true
    }

    private fun errorStateHandler() {
        loadingLiveData.value = false
        errorLiveData.value = true
    }

}