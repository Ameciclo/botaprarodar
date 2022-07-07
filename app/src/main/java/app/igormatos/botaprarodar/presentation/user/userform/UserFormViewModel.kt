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
    val communityUsers: ArrayList<User>,
    val mapOptions: Map<String, List<String>>
) : ViewModel() {
    val openQuiz = MutableLiveData<ViewEvent<Triple<User, Boolean, List<String>>>>()
    var isEditableAvailable = false
    var user = User()

    var userCompleteName = MutableLiveData("")
    var userAddress = MutableLiveData("")
    var userImageProfile = MutableLiveData("")
    private var _deleteImagePaths = MutableLiveData(ArrayList<String>())
    val deleteImagePaths: LiveData<ArrayList<String>> = _deleteImagePaths
    var userGender = MutableLiveData("")
    var userRacial = MutableLiveData("")
    var userSchooling = MutableLiveData("")
    var userSchoolingStatus = MutableLiveData("")
    var userIncome = MutableLiveData("")
    var userBirthday = MutableLiveData("")
    var userTelephone = MutableLiveData("")
    var selectedSchoolingIndex = 0
    var selectedSchoolingStatusIndex = MutableLiveData(0)
    var selectedIncomeIndex = 0
    var selectedRacialIndex = 0
    var selectedGenderIndex = 0
    private var _statusDeleteImage = MutableLiveData<ViewModelStatus<Unit>>()
    val statusDeleteImage: LiveData<ViewModelStatus<Unit>> = _statusDeleteImage

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userCompleteName) { validateUserForm() }
        addSource(userAddress) { validateUserForm() }
        addSource(userImageProfile) { validateUserForm() }
        addSource(userGender) { validateUserForm() }
        addSource(userRacial) { validateUserForm() }
        addSource(userSchooling) { validateUserForm() }
        addSource(userIncome) { validateUserForm() }
        addSource(userBirthday) { validateUserForm() }
        addSource(userTelephone) { validateUserForm() }
    }

    fun updateUserValues(currentUser: User) {
        user = currentUser.apply {
            userCompleteName.value = this.name.orEmpty()
            userAddress.value = this.address.orEmpty()
            userImageProfile.value = this.profilePicture.orEmpty()
            userGender.value = this.gender.orEmpty()
            userRacial.value = this.racial.orEmpty()
            userSchooling.value = this.schooling.orEmpty()
            userSchoolingStatus.value = this.schoolingStatus.orEmpty()
            userIncome.value = this.income.orEmpty()
            userBirthday.value = this.birthday.orEmpty()
            userTelephone.value = this.telephone.orEmpty()
        }
        setSelectSchoolingStatusIndex(getSelectedSchoolingStatusListIndex())
        confirmUserSchoolingStatus()
        isEditableAvailable = true
        communityUsers.remove(currentUser)
    }


    private fun validateUserForm() {
        val validated = userCompleteName.value.isNotNullOrNotBlank() &&
                userAddress.value.isNotNullOrNotBlank() &&
                userRacial.value.isNotNullOrNotBlank() &&
                userSchooling.value.isNotNullOrNotBlank() &&
                userSchoolingStatus.value.isNotNullOrNotBlank() &&
                userIncome.value.isNotNullOrNotBlank() &&
                userBirthday.value.isNotNullOrNotBlank() &&
                (userTelephone.value.isNullOrEmpty() || userTelephone.value.isValidTelephone()) &&
                userGender.value.isNotNullOrNotBlank()

        isButtonEnabled.value = validated
    }

    private fun createUser() {
        user.apply {
            name = userCompleteName.value
            address = userAddress.value
            profilePicture = userImageProfile.value
            gender = userGender.value
            communityId = community.id
            racial = userRacial.value
            schooling = userSchooling.value
            schoolingStatus = userSchoolingStatus.value
            income = userIncome.value
            birthday = userBirthday.value
            telephone = userTelephone.value
        }
    }

    fun setProfileImage(path: String) {
        userImageProfile.value = path
    }

    fun confirmUserGender() {
        userGender.value = getGenderList()[selectedGenderIndex]
    }

    fun confirmUserRace() {
        userRacial.value = getRacialList()[selectedRacialIndex]
    }

    fun confirmUserSchooling() {
        userSchooling.value = getSchoolingList()[selectedSchoolingIndex]
    }

    fun confirmUserSchoolingStatus() {
        userSchoolingStatus.value = when (selectedSchoolingStatusIndex.value) {
            R.id.schoolingStatusComplete -> getScoolingStatusList()[0]
            R.id.schoolingStatusIncomplete -> getScoolingStatusList()[1]
            R.id.schoolingStatusStudying -> getScoolingStatusList()[2]
            else -> getScoolingStatusList()[0]
        }
    }

    fun confirmUserIncome() {
        userIncome.value = getIncomeList()[selectedIncomeIndex]
    }

    fun getSelectedGenderListIndex(): Int {
        selectedGenderIndex = mapOptions.getIndexFromList("genderOptions", userGender.value.toString())
        return selectedGenderIndex
    }

    fun getSelectedSchoolingListIndex(): Int {
        selectedSchoolingIndex = mapOptions.getIndexFromList("schoolingOptions", userSchooling.value.toString())
        return selectedSchoolingIndex
    }

    fun getSelectedSchoolingStatusListIndex(): Int {
        return when (mapOptions.getIndexFromList("schoolingStatusOptions", userSchoolingStatus.value.toString())) {
            0 -> R.id.schoolingStatusComplete
            1 -> R.id.schoolingStatusIncomplete
            2 -> R.id.schoolingStatusStudying
            else -> R.id.schoolingStatusComplete
        }
    }

    fun getSelectedIncomeListIndex(): Int {
        selectedIncomeIndex = mapOptions.getIndexFromList("incomeOptions", userIncome.value.toString())
        return selectedIncomeIndex
    }

    fun getSelectedRacialListIndex(): Int {
        selectedRacialIndex = mapOptions.getIndexFromList("racialOptions", userRacial.value.toString())
        return selectedRacialIndex
    }

    fun setSelectGenderIndex(index: Int) {
        selectedGenderIndex = index
    }

    fun setSelectRacialIndex(index: Int) {
        selectedRacialIndex = index
    }

    fun setSelectSchoolingIndex(index: Int) {
        selectedSchoolingIndex = index
    }

    fun setSelectIncomeIndex(index: Int) {
        selectedIncomeIndex = index
    }
    
    fun setSelectSchoolingStatusIndex(index: Int) {
        selectedSchoolingStatusIndex.value = index
    }

    fun getGenderList() : List<String>{
        return mapOptions["genderOptions"].orEmpty()
    }

    fun getRacialList() : List<String>{
        return mapOptions["racialOptions"].orEmpty()
    }

    fun getSchoolingList() : List<String>{
        return mapOptions["schoolingOptions"].orEmpty()
    }

    fun getScoolingStatusList() : List<String>{
        return mapOptions["schoolingStatusOptions"].orEmpty()
    }

    fun getIncomeList() : List<String> {
        return mapOptions["incomeOptions"].orEmpty()
    }

    fun navigateToNextStep() {
        stepper.navigateToNext()
        createUser()

        openQuiz.value = ViewEvent(Triple(user, isEditableAvailable, deleteImagePaths.value?.toList() ?: listOf()))
    }
}