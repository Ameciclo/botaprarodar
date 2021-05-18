package app.igormatos.botaprarodar.presentation.user.userform

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.ViewEvent

class UserFormViewModel(
    private val community: Community,
    val stepper: RegisterUserStepper
) : ViewModel() {

    val openQuiz = MutableLiveData<ViewEvent<Pair<User, Boolean>>>()

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
    var userRacial = MutableLiveData<String>("")
    var userSchooling = MutableLiveData<String>("")
    var userIncome = MutableLiveData<String>("")
    var userAge = MutableLiveData<String>("")

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userCompleteName) { validateUserForm() }
        addSource(userAddress) { validateUserForm() }
        addSource(userDocument) { validateUserForm() }
        addSource(userImageProfile) { validateUserForm() }
        addSource(userImageDocumentResidence) { validateUserForm() }
        addSource(userImageDocumentFront) { validateUserForm() }
        addSource(userImageDocumentBack) { validateUserForm() }
        addSource(userGender) { validateUserForm() }
        addSource(userRacial) { validateUserForm() }
        addSource(userSchooling) { validateUserForm() }
        addSource(userIncome) { validateUserForm() }
        addSource(userAge) { validateUserForm() }
    }

    fun updateUserValues(currentUser: User) {
        user = currentUser.apply {
            userCompleteName.value = this.name.orEmpty()
            userAddress.value = this.address.orEmpty()
            userDocument.value = this.docNumber.toString()
            userImageProfile.value = this.profilePicture.orEmpty()
            userImageDocumentResidence.value = this.residenceProofPicture.orEmpty()
            userImageDocumentFront.value = this.docPicture.orEmpty()
            userImageDocumentBack.value = this.docPictureBack.orEmpty()
            userGender.value = this.gender
            userRacial.value = this.racial.orEmpty()
            userSchooling.value = this.schooling.orEmpty()
            userIncome.value = this.income.orEmpty()
            userAge.value = this.age.orEmpty()
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
                isTextValid(userRacial.value) &&
                isTextValid(userSchooling.value) &&
                isTextValid(userIncome.value) &&
                isTextValid(userAge.value) &&
                userGender.value != GENDER_INITIAL_VALUE
    }

    private fun isTextValid(data: String?) = !data.isNullOrBlank()


    private fun createUser() {
        user.apply {
            name = userCompleteName.value
            address = userAddress.value
            docNumber = userDocument.value?.toLong() ?: 0L
            profilePicture = userImageProfile.value
            residenceProofPicture = userImageDocumentResidence.value
            docPicture = userImageDocumentFront.value
            docPictureBack = userImageDocumentBack.value
            gender = userGender.value ?: NO_ANSWER
            communityId = community.id
            racial = userRacial.value
            schooling = userSchooling.value
            income = userIncome.value
            age = userAge.value
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

    fun navigateToNextStep() {
        stepper.navigateToNext()
        createUser()
        openQuiz.value = ViewEvent(user to isEditableAvailable)
    }

    companion object {
        private const val GENDER_INITIAL_VALUE = -1
    }
}