package app.igormatos.botaprarodar.presentation.user.socialdata

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.extensions.getIndexFromList
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.ViewEvent
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank

class SocialDataViewModel(
    private val mapOptions: Map<String, List<String>>,
    var user: User,
    private var isEditableAvailable: Boolean
) : ViewModel() {

    val openQuiz = MutableLiveData<ViewEvent<Triple<User, Boolean, List<String>>>>()

    var userGender = MutableLiveData("")
    var userRacial = MutableLiveData("")
    var userSchooling = MutableLiveData("")
    var userSchoolingStatus = MutableLiveData("")
    var userIncome = MutableLiveData("")

    var selectedSchoolingIndex = 0
    var selectedSchoolingStatusIndex = MutableLiveData(0)
    var selectedIncomeIndex = 0
    var selectedRacialIndex = 0
    var selectedGenderIndex = 0

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userGender) { validateUserForm() }
        addSource(userRacial) { validateUserForm() }
        addSource(userSchooling) { validateUserForm() }
        addSource(userIncome) { validateUserForm() }
    }

    fun updateUserValues(currentUser: User, communityUsers: ArrayList<User>) {
        user = currentUser.apply {
            userGender.value = this.gender.orEmpty()
            userRacial.value = this.racial.orEmpty()
            userSchooling.value = this.schooling.orEmpty()
            userSchoolingStatus.value = this.schoolingStatus.orEmpty()
            userIncome.value = this.income.orEmpty()
        }
        setSelectSchoolingStatusIndex(getSelectedSchoolingStatusListIndex())
        confirmUserSchoolingStatus()
        isEditableAvailable = true
        communityUsers.remove(currentUser)
    }

    private fun validateUserForm() {
        val validated = userRacial.value.isNotNullOrNotBlank() &&
                userSchooling.value.isNotNullOrNotBlank() &&
                userSchoolingStatus.value.isNotNullOrNotBlank() &&
                userIncome.value.isNotNullOrNotBlank() &&
                userGender.value.isNotNullOrNotBlank()

        isButtonEnabled.value = validated
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
        selectedGenderIndex =
            mapOptions.getIndexFromList("genderOptions", userGender.value.toString())
        return selectedGenderIndex
    }

    fun getSelectedSchoolingListIndex(): Int {
        selectedSchoolingIndex =
            mapOptions.getIndexFromList("schoolingOptions", userSchooling.value.toString())
        return selectedSchoolingIndex
    }

    fun getSelectedSchoolingStatusListIndex(): Int {
        return when (mapOptions.getIndexFromList(
            "schoolingStatusOptions",
            userSchoolingStatus.value.toString()
        )) {
            0 -> R.id.schoolingStatusComplete
            1 -> R.id.schoolingStatusIncomplete
            2 -> R.id.schoolingStatusStudying
            else -> R.id.schoolingStatusComplete
        }
    }

    fun getSelectedIncomeListIndex(): Int {
        selectedIncomeIndex =
            mapOptions.getIndexFromList("incomeOptions", userIncome.value.toString())
        return selectedIncomeIndex
    }

    fun getSelectedRacialListIndex(): Int {
        selectedRacialIndex =
            mapOptions.getIndexFromList("racialOptions", userRacial.value.toString())
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

    fun confirmUserGender() {
        userGender.value = getGenderList()[selectedGenderIndex]
    }

    fun confirmUserRace() {
        userRacial.value = getRacialList()[selectedRacialIndex]
    }

    fun confirmUserSchooling() {
        userSchooling.value = getSchoolingList()[selectedSchoolingIndex]
    }

    fun getGenderList(): List<String> {
        return mapOptions["genderOptions"].orEmpty()
    }

    fun getRacialList(): List<String> {
        return mapOptions["racialOptions"].orEmpty()
    }

    fun getSchoolingList(): List<String> {
        return mapOptions["schoolingOptions"].orEmpty()
    }

    fun getScoolingStatusList(): List<String> {
        return mapOptions["schoolingStatusOptions"].orEmpty()
    }

    fun getIncomeList(): List<String> {
        return mapOptions["incomeOptions"].orEmpty()
    }

    fun setSelectSchoolingStatusIndex(index: Int) {
        selectedSchoolingStatusIndex.value = index
    }

    fun navigateToNextStep(stepper: RegisterUserStepper) {
        stepper.navigateToNext()
        fillWithSocialData()

        openQuiz.value = ViewEvent(
            Triple(
                user,
                isEditableAvailable,
                emptyList()
            )
        )
    }

    private fun fillWithSocialData() {
        user.apply {
            gender = userGender.value
            racial = userRacial.value
            schooling = userSchooling.value
            schoolingStatus = userSchoolingStatus.value
            income = userIncome.value

        }
    }


}

