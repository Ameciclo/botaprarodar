package app.igormatos.botaprarodar.presentation.user.userform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.common.extensions.getIndexFromList
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.ViewEvent
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank
import com.brunotmgomes.ui.extensions.isValidTelephone

class UserFormViewModel(
    private val community: Community,
    val stepper: RegisterUserStepper,
    val communityUsers: ArrayList<User>
) : ViewModel() {
    val openQuiz = MutableLiveData<ViewEvent<Triple<User, Boolean, List<String>>>>()
    var isEditableAvailable = false
    var user = User()

    var userCompleteName = MutableLiveData("")
    var userAddress = MutableLiveData("")
    var userImageProfile = MutableLiveData("")
    private var _deleteImagePaths = MutableLiveData(ArrayList<String>())
    val deleteImagePaths: LiveData<ArrayList<String>> = _deleteImagePaths

    var userBirthday = MutableLiveData("")
    var userTelephone = MutableLiveData("")

    private var _statusDeleteImage = MutableLiveData<ViewModelStatus<Unit>>()
    val statusDeleteImage: LiveData<ViewModelStatus<Unit>> = _statusDeleteImage

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userCompleteName) { validateUserForm() }
        addSource(userAddress) { validateUserForm() }
        addSource(userImageProfile) { validateUserForm() }
        addSource(userBirthday) { validateUserForm() }
        addSource(userTelephone) { validateUserForm() }
    }

    fun updateUserValues(currentUser: User) {
        user = currentUser.apply {
            userCompleteName.value = this.name.orEmpty()
            userAddress.value = this.address.orEmpty()
            userImageProfile.value = this.profilePicture.orEmpty()
            userBirthday.value = this.birthday.orEmpty()
            userTelephone.value = this.telephone.orEmpty()
        }
        isEditableAvailable = true
        communityUsers.remove(currentUser)
    }

    private fun validateUserForm() {
        val validated = userCompleteName.value.isNotNullOrNotBlank() &&
                userAddress.value.isNotNullOrNotBlank() &&
                userBirthday.value.isNotNullOrNotBlank() &&
                (userTelephone.value.isNullOrEmpty() || userTelephone.value.isValidTelephone())
        isButtonEnabled.value = validated
    }

    fun setProfileImage(path: String) {
        userImageProfile.value = path
    }

    fun navigateToNextStep() {
        stepper.navigateToNext()
        createUser()

        openQuiz.value = ViewEvent(
            Triple(
                user,
                isEditableAvailable,
                deleteImagePaths.value?.toList() ?: listOf()
            )
        )
    }

    private fun createUser() {
        user.apply {
            name = userCompleteName.value
            address = userAddress.value
            profilePicture = userImageProfile.value
            communityId = community.id
            birthday = userBirthday.value
            telephone = userTelephone.value
        }
    }
}
