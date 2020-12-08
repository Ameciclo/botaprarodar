package app.igormatos.botaprarodar.presentation.createcommunity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import app.igormatos.botaprarodar.domain.usecase.community.AddCommunityUseCase
import kotlinx.coroutines.launch
import com.brunotmgomes.ui.SimpleResult

class AddCommunityViewModel(
    private val communityUseCase: AddCommunityUseCase
) : ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    fun getLoadingLiveDataValue() : LiveData<Boolean> = loadingLiveData

    private val successLiveData = MutableLiveData<Boolean>()
    fun getSuccessLiveDataValue() : LiveData<Boolean> = successLiveData

    private val errorLiveData = MutableLiveData<Exception>()
    fun getErrorLiveDataValue() : LiveData<Exception> = errorLiveData

    fun sendCommunity(community: CommunityRequest) {
        loadingLiveData.value = true
        viewModelScope.launch {
            when (val result = communityUseCase.addNewCommunity(community)) {
                is SimpleResult.Success -> successStateHandler(result.data)
                is SimpleResult.Error -> errorStateHandler(result.exception)
            }
        }
    }

    private fun successStateHandler(name: String?) {
        loadingLiveData.value = false
        successLiveData.value = name != null
    }

    private fun errorStateHandler(exception: Exception) {
        loadingLiveData.value = false
        errorLiveData.value = exception
    }

}