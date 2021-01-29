package app.igormatos.botaprarodar.presentation.adduser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.common.ViewModelStatus

class AddUserViewModel : ViewModel() {

    private val _status = MutableLiveData<ViewModelStatus<String>>()
    val status: LiveData<ViewModelStatus<String>> = _status

    var userCompleteName = MutableLiveData<String>()
    var userAddress = MutableLiveData<String>()
    var userCPF = MutableLiveData<String>()
    var userImageProfile = MutableLiveData<String>()
}