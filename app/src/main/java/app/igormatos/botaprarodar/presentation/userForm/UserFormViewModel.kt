package app.igormatos.botaprarodar.presentation.userForm

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.user.UserUseCase
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.launch

class UserFormViewModel(
    private val userUseCase: UserUseCase,
    private val community: Community
) : ViewModel() {

    private val _status = MutableLiveData<ViewModelStatus<String>>()
    val status: LiveData<ViewModelStatus<String>> = _status

    var isEditableAvailable = false
    var user = User()

    var userCompleteName = MutableLiveData<String>("")
    var userAddress = MutableLiveData<String>("")
    var userDocument = MutableLiveData<String>("")
    var userImageProfile = MutableLiveData<String>("")
    var userImageDocumentResidence = MutableLiveData<String>("")
    var userImageDocumentFront = MutableLiveData<String>("")
    var userImageDocumentBack = MutableLiveData<String>("")
    var userGender = MutableLiveData<Int>(GENDER_INITIAL_VALUE)

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

    fun updateUserValues(currentUser: User) {
        user = currentUser.apply {
            userCompleteName.value = this.name
            userAddress.value = this.address
            userDocument.value = this.doc_number.toString()
            userImageProfile.value = this.profile_picture
            userImageDocumentResidence.value = this.residence_proof_picture
            userImageDocumentFront.value = this.doc_picture
            userImageDocumentBack.value = this.doc_picture_back
            userGender.value = this.gender
        }
        isEditableAvailable = true
    }

    private fun validateUserForm() {
        isButtonEnabled.value = isTextValid(userCompleteName.value) &&
                isTextValid(userAddress.value) &&
                isTextValid(userDocument.value) &&
                isTextValid(userImageProfile.value) &&
                isTextValid(userImageDocumentResidence.value) &&
                isTextValid(userImageDocumentFront.value) &&
                isTextValid(userImageDocumentBack.value) &&
                userGender.value != GENDER_INITIAL_VALUE
    }

    private fun isTextValid(data: String?) = !data.isNullOrBlank()

    fun registerUser() {
        _status.value = ViewModelStatus.Loading
        createUser()
        viewModelScope.launch {
            if (isEditableAvailable)
                updateUser()
            else
                addUser()
        }
    }

    private suspend fun updateUser() {
        userUseCase.updateUser(community.id, user).let {
            when (it) {
                is SimpleResult.Success -> showSuccess()
                is SimpleResult.Error -> showError()
            }
        }
    }

    private suspend fun addUser() {
        userUseCase.addUser(community.id, user).let {
            when (it) {
                is SimpleResult.Success -> showSuccess()
                is SimpleResult.Error -> showError()
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
            gender = userGender.value ?: NO_ANSWER
        }
    }

    fun setUserGender(radioButtonGenderId: Int) {
        userGender.value = getGenderId(radioButtonGenderId)
    }

    fun setProfileImage(path: String) {
        userImageProfile.value = path
    }

    fun setDocumentImageFront(path: String) {
        userImageDocumentFront.value = path
    }

    fun setDocumentImageBack(path: String) {
        userImageDocumentBack.value = path
    }

    fun setResidenceImage(path: String) {
        userImageDocumentResidence.value = path
    }

    private fun showSuccess() {
        _status.value = ViewModelStatus.Success("")
    }

    private fun showError() {
        val message = if (isEditableAvailable) UNKNOWN_ERROR_EDIT else UNKNOWN_ERROR_REGISTER
        _status.value = ViewModelStatus.Error(message)
    }

    companion object {
        private const val UNKNOWN_ERROR_REGISTER = "Falha ao cadastrar o usuário"
        private const val UNKNOWN_ERROR_EDIT = "Falha ao editar o usuário"
        private const val GENDER_INITIAL_VALUE = -1
    }
}