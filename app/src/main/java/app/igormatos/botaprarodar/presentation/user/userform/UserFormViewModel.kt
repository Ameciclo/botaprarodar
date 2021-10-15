package app.igormatos.botaprarodar.presentation.user.userform

import androidx.lifecycle.*
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import com.brunotmgomes.ui.ViewEvent
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank
import com.brunotmgomes.ui.extensions.isValidTelephone
import kotlinx.coroutines.launch

class UserFormViewModel(
    private val community: Community,
    val stepper: RegisterUserStepper,
    val communityUsers: ArrayList<User>,
    val racialList: List<String>,
    val incomeList: List<String>,
    val schoolingList: List<String>,
    val genderList: List<String>
) : ViewModel() {
    val openQuiz = MutableLiveData<ViewEvent<Triple<User, Boolean, List<String>>>>()
    var isEditableAvailable = false
    var user = User()

    var userCompleteName = MutableLiveData("")
    var userAddress = MutableLiveData("")
    var userDocument = MutableLiveData("")
    var userImageProfile = MutableLiveData("")
    private var _userImageDocumentResidence = MutableLiveData("")
    val userImageDocumentResidence: LiveData<String> = _userImageDocumentResidence
    private var _deleteImagePaths = MutableLiveData(ArrayList<String>())
    val deleteImagePaths: LiveData<ArrayList<String>> = _deleteImagePaths
    var userImageDocumentFront = MutableLiveData("")
    var userImageDocumentBack = MutableLiveData("")
    var userGender = MutableLiveData("")
    var userRacial = MutableLiveData("")
    var userSchooling = MutableLiveData("")
    var userIncome = MutableLiveData("")
    var userAge = MutableLiveData("")
    var userTelephone = MutableLiveData("")
    var selectedSchoolingIndex = 0
    var selectedIncomeIndex = 0
    var selectedRacialIndex = 0
    var selectedGenderIndex = 0
    private var _statusDeleteImage = MutableLiveData<ViewModelStatus<Unit>>()
    val statusDeleteImage: LiveData<ViewModelStatus<Unit>> = _statusDeleteImage

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(userCompleteName) { validateUserForm() }
        addSource(userAddress) { validateUserForm() }
        addSource(userDocument) { validateUserForm() }
        addSource(userImageProfile) { validateUserForm() }
        addSource(userImageDocumentFront) { validateUserForm() }
        addSource(userImageDocumentBack) { validateUserForm() }
        addSource(userGender) { validateUserForm() }
        addSource(userRacial) { validateUserForm() }
        addSource(userSchooling) { validateUserForm() }
        addSource(userIncome) { validateUserForm() }
        addSource(userAge) { validateUserForm() }
        addSource(userTelephone) { validateUserForm() }
    }

    val docNumberErrorValidationMap = MediatorLiveData<MutableMap<Int, Boolean>>().apply {
        value = mutableMapOf()

        addSource(userDocument) {
            validateDocNumber()
        }
    }

    private fun validateDocNumber() {
        with(docNumberErrorValidationMap.value) {
            this?.set(
                DOC_NUMBER_INVALID_ERROR,
                userDocument.value.isNullOrEmpty()
            )

            this?.set(
                DOC_NUMBER_ALREADY_REGISTERED_ERROR,
                communityUsersHasDocNumber(userDocument.value)
            )
        }
    }

    private fun communityUsersHasDocNumber(docNumber: String?) =
        communityUsers.any { it.docNumber.toString() == docNumber }

    fun updateUserValues(currentUser: User) {
        user = currentUser.apply {
            userCompleteName.value = this.name.orEmpty()
            userAddress.value = this.address.orEmpty()
            userDocument.value = this.docNumber.toString()
            userImageProfile.value = this.profilePicture.orEmpty()
            _userImageDocumentResidence.value = this.residenceProofPicture.orEmpty()
            userImageDocumentFront.value = this.docPicture.orEmpty()
            userImageDocumentBack.value = this.docPictureBack.orEmpty()
            userGender.value = this.gender.orEmpty()
            userRacial.value = this.racial.orEmpty()
            userSchooling.value = this.schooling.orEmpty()
            userIncome.value = this.income.orEmpty()
            userAge.value = this.age.orEmpty()
            userTelephone.value = this.telephone.orEmpty()
        }
        isEditableAvailable = true
        communityUsers.remove(currentUser)
    }


    private fun validateUserForm() {
        val validated = userCompleteName.value.isNotNullOrNotBlank() &&
                userAddress.value.isNotNullOrNotBlank() &&
                isDocNumberValid() &&
                userImageProfile.value.isNotNullOrNotBlank() &&
                userImageDocumentFront.value.isNotNullOrNotBlank() &&
                userImageDocumentBack.value.isNotNullOrNotBlank() &&
                userRacial.value.isNotNullOrNotBlank() &&
                userSchooling.value.isNotNullOrNotBlank() &&
                userIncome.value.isNotNullOrNotBlank() &&
                userAge.value.isNotNullOrNotBlank() &&
                (userTelephone.value.isNullOrEmpty() || userTelephone.value.isValidTelephone()) &&
                userGender.value.isNotNullOrNotBlank()

        isButtonEnabled.value = validated
    }


    private fun isDocNumberValid(): Boolean {
        val existsDocNumberError = docNumberErrorValidationMap.value?.containsValue(true)
        if (existsDocNumberError != null) {
            return !existsDocNumberError
        }
        return true
    }

    private fun createUser() {
        user.apply {
            name = userCompleteName.value
            address = userAddress.value
            docNumber = userDocument.value?.toLong() ?: 0L
            profilePicture = userImageProfile.value
            residenceProofPicture = _userImageDocumentResidence.value
            docPicture = userImageDocumentFront.value
            docPictureBack = userImageDocumentBack.value
            gender = userGender.value
            communityId = community.id
            racial = userRacial.value
            schooling = userSchooling.value
            income = userIncome.value
            age = userAge.value
            telephone = userTelephone.value
        }
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
        _userImageDocumentResidence.value = path
    }

    fun confirmUserGender() {
        userGender.value = genderList[selectedGenderIndex]
    }

    fun confirmUserRace() {
        userRacial.value = racialList[selectedRacialIndex]
    }

    fun confirmUserSchooling() {
        userSchooling.value = schoolingList[selectedSchoolingIndex]
    }

    fun confirmUserIncome() {
        userIncome.value = incomeList[selectedIncomeIndex]
    }

    fun getSelectedGenderListIndex(): Int {
        selectedGenderIndex =
            genderList.indexOfLast { userGender.value.equals(it) }.takeIf { it > -1 } ?: 0
        return selectedGenderIndex
    }

    fun getSelectedSchoolingListIndex(): Int {
        selectedSchoolingIndex =
            schoolingList.indexOfLast { userSchooling.value.equals(it) }.takeIf { it > -1 } ?: 0
        return selectedSchoolingIndex
    }

    fun getSelectedIncomeListIndex(): Int {
        selectedIncomeIndex =
            incomeList.indexOfLast { userIncome.value.equals(it) }.takeIf { it > -1 } ?: 0
        return selectedIncomeIndex
    }

    fun getSelectedRacialListIndex(): Int {
        selectedRacialIndex =
            racialList.indexOfLast { userRacial.value.equals(it) }.takeIf { it > -1 } ?: 0
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

    fun deleteProofResidenceImage() {
        viewModelScope.launch {
            userImageDocumentResidence.value?.apply {
                _deleteImagePaths.value?.add(_userImageDocumentResidence.value.orEmpty() )
                _userImageDocumentResidence.value = ""
                _statusDeleteImage.value = ViewModelStatus.Success(Unit)
            }
        }
    }


    fun navigateToNextStep() {
        stepper.navigateToNext()
        createUser()

        openQuiz.value = ViewEvent(Triple(user, isEditableAvailable, deleteImagePaths.value?.toList() ?: listOf()))
    }

    companion object {
        private const val GENDER_INITIAL_VALUE = -1
        private const val DOC_NUMBER_ALREADY_REGISTERED_ERROR =
            R.string.add_user_doc_number_already_registered

        private const val DOC_NUMBER_INVALID_ERROR =
            R.string.add_user_invalid_doc_number
    }
}