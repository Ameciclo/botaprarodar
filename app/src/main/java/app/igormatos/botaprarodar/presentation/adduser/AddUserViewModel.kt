package app.igormatos.botaprarodar.presentation.adduser

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.user.UserUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val userUseCase: UserUseCase,
    private val community: Community
) : ViewModel() {

    private val _status = MutableLiveData<ViewModelStatus<String>>()
    val status: LiveData<ViewModelStatus<String>> = _status

    var user = User()

    var userCompleteName = MutableLiveData<String>("")
    var userAddress = MutableLiveData<String>("")
    var userDocument = MutableLiveData<String>("")
    var userImageProfile = MutableLiveData<String>("")
    var userImageDocumentResidence = MutableLiveData<String>("")
    var userImageDocumentFront = MutableLiveData<String>("")
    var userImageDocumentBack = MutableLiveData<String>("")
    var userGender = MutableLiveData<Int>(0)

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userCompleteName) { validateUserForm() }
        addSource(userAddress) { validateUserForm() }
        addSource(userDocument) { validateUserForm() }
        addSource(userImageProfile) { validateUserForm() }
        addSource(userImageDocumentResidence) { validateUserForm() }
        addSource(userImageDocumentFront) { validateUserForm() }
        addSource(userImageDocumentBack) { validateUserForm() }
        addSource(userGender) { validateUserForm() }
    }

    private fun validateUserForm() {
        isButtonEnabled.value = isTextValid(userCompleteName.value) &&
                isTextValid(userAddress.value) &&
                isTextValid(userDocument.value) &&
                isTextValid(userImageProfile.value) &&
                isTextValid(userImageDocumentResidence.value) &&
                isTextValid(userImageDocumentFront.value) &&
                isTextValid(userImageDocumentBack.value) &&
                userGender.value != 0
    }

    private fun isTextValid(data: String?) = !data.isNullOrBlank()

    fun setUserGender(radioButtonGenderId: Int) {
        userGender.value = radioButtonGenderId
        user.gender = getGenderId(radioButtonGenderId)
    }

    fun registerUser() {
        _status.value = ViewModelStatus.Loading
        createUser()
        viewModelScope.launch {
            userUseCase.addUser(community.id, user).let {
                when (it) {
                    is SimpleResult.Success -> {
                        showSuccess()
                    }
                    is SimpleResult.Error -> {
                        showError()
                    }
                }
            }
        }
    }

    private fun createUser() {
        user.apply {
            name = userCompleteName.value
            address = userAddress.value
            doc_number = userDocument.value?.toLong() ?: 0L
            profile_picture = userImageProfile.value
            residence_proof_picture = userImageDocumentResidence.value
            doc_picture = userImageDocumentFront.value
            doc_picture_back = userImageDocumentBack.value
        }
    }

    fun setProfileImage(path: String) {
        userImageProfile.value = path
    }

    fun setDocumentImage(path: String) {
        userImageDocumentFront.value = path
    }

    fun setDocumentImageBack(path: String) {
        userImageDocumentBack.value = path
    }

    fun setResidenceImage(path: String) {
        userImageDocumentResidence.value = path
    }

    private fun showSuccess() {
        _status.value = ViewModelStatus.Success("Usuário cadastrado com sucesso")
    }

    private fun showError() {
        _status.value = ViewModelStatus.Error("Ocorreu um erro, tente novamente")
    }
}