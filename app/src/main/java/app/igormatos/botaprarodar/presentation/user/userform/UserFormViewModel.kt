package app.igormatos.botaprarodar.presentation.user.userform

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.ViewEvent
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank
import com.brunotmgomes.ui.extensions.isValidTelephone

class UserFormViewModel(
    private val community: Community,
    val stepper: RegisterUserStepper,
    private val communityUsers: ArrayList<User>
) : ViewModel() {
    val openUserSocialData = MutableLiveData<ViewEvent<Triple<User, Boolean, List<String>>>>()
    var isEditableAvailable = false
    var user = User()

    var userCompleteName = MutableLiveData("")
    var userAddress = MutableLiveData("")
    var userImageProfile = MutableLiveData("")

    var userBirthday = MutableLiveData("")
    var userTelephone = MutableLiveData("")

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

        openUserSocialData.value = ViewEvent(
            Triple(
                user,
                isEditableAvailable,
                emptyList()
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
