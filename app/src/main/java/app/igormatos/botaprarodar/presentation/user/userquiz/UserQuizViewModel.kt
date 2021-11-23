package app.igormatos.botaprarodar.presentation.user.userquiz

import androidx.lifecycle.*
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.UserQuiz
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import com.brunotmgomes.ui.SimpleResult
import com.brunotmgomes.ui.extensions.isNotNull
import com.brunotmgomes.ui.extensions.isNotNullOrBlank
import kotlinx.coroutines.launch

class UserQuizViewModel(
    private val userUseCase: UserFormUseCase
) : ViewModel() {

    var editMode = false

    val accessOtherTransport = MutableLiveData<Boolean>()

    val accessOtherTransportOpenQuestion = MutableLiveData<String>()

    val alreadyUseBPR = MutableLiveData<Boolean>()

    val showUseBPROpenQuestion: LiveData<Boolean>
        get() = alreadyUseBPR

    val alreadyUseBPROpenQuestion = MutableLiveData<String>()

    val motivationOpenQuestion = MutableLiveData<String>()

    val alreadyAccidentVictim = MutableLiveData<Boolean>()

    val problemsOnWayOpenQuestion = MutableLiveData<String>()

    val timeOnWayOpenQuestion = MutableLiveData<String>()

    private lateinit var deleteImagePaths: List<String>

    private lateinit var user: User

    private val _status = MutableLiveData<ViewModelStatus<String>>()
    val status: LiveData<ViewModelStatus<String>> = _status

    private val _lgpd = MutableLiveData<Boolean>()
    val lgpd: LiveData<Boolean> = _lgpd

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(accessOtherTransport) { validateQuestions() }
        addSource(accessOtherTransportOpenQuestion) { validateQuestions() }
        addSource(alreadyUseBPR) { validateQuestions() }
        addSource(alreadyUseBPROpenQuestion) { validateQuestions() }
        addSource(motivationOpenQuestion) { validateQuestions() }
        addSource(alreadyAccidentVictim) { validateQuestions() }
        addSource(problemsOnWayOpenQuestion) { validateQuestions() }
        addSource(timeOnWayOpenQuestion) { validateQuestions() }
    }

    fun init(user: User, editMode: Boolean, deleteImagePaths: List<String>) {
        this.user = user
        this.editMode = editMode
        this.deleteImagePaths = deleteImagePaths
        if (editMode) {
            fillUserQuiz()
        }
    }

    private fun fillUserQuiz() {
        user.userQuiz?.let {
            accessOtherTransport.value = it.accessOtherTransport ?: false
            accessOtherTransportOpenQuestion.value = it.accessOtherTransportOpenQuestion.orEmpty()
            alreadyUseBPR.value = it.alreadyUseBPR ?: false
            alreadyUseBPROpenQuestion.value = it.alreadyUseBPROpenQuestion.orEmpty()
            motivationOpenQuestion.value = it.motivationOpenQuestion.orEmpty()
            alreadyAccidentVictim.value = it.alreadyAccidentVictim ?: false
            problemsOnWayOpenQuestion.value = it.problemsOnWayOpenQuestion.orEmpty()
            timeOnWayOpenQuestion.value = it.timeOnWayOpenQuestion.orEmpty()
        }
    }

    fun createUserQuiz() = UserQuiz(
        accessOtherTransport = accessOtherTransport.value,
        accessOtherTransportOpenQuestion = accessOtherTransportOpenQuestion.value,
        alreadyUseBPR = alreadyUseBPR.value,
        alreadyUseBPROpenQuestion = alreadyUseBPROpenQuestion.value,
        motivationOpenQuestion = motivationOpenQuestion.value,
        alreadyAccidentVictim = alreadyAccidentVictim.value,
        problemsOnWayOpenQuestion = problemsOnWayOpenQuestion.value,
        timeOnWayOpenQuestion = timeOnWayOpenQuestion.value
    )

    fun onSaveClick() {
        _lgpd.value = true
    }

    fun registerUser() {
        _status.value = ViewModelStatus.Loading
        user.userQuiz = createUserQuiz()
        viewModelScope.launch {
            if (editMode)
                updateUser()
            else
                addUser()
        }
    }

    private suspend fun updateUser() {
        userUseCase.startUpdateUser(user, deleteImagePaths).let {
            when (it) {
                is SimpleResult.Success -> {
                    showSuccess()
                }
                is SimpleResult.Error -> showError()
            }
        }
    }

    private suspend fun addUser() {
        userUseCase.addUser(user).let {
            when (it) {
                is SimpleResult.Success -> showSuccess()
                is SimpleResult.Error -> showError()
            }
        }
    }

    private fun showSuccess() {
        _status.value = ViewModelStatus.Success("")
    }

    private fun showError() {
        val message = if (editMode) UNKNOWN_ERROR_EDIT else UNKNOWN_ERROR_REGISTER
        _status.value = ViewModelStatus.Error(message)
    }

    private fun validateQuestions() {
        isButtonEnabled.value =
            isQuestionValid(accessOtherTransport, accessOtherTransportOpenQuestion) &&
                    isQuestionValid(alreadyUseBPR, alreadyUseBPROpenQuestion) &&
                    motivationOpenQuestion.isNotNullOrBlank() &&
                    alreadyAccidentVictim.isNotNull() &&
                    problemsOnWayOpenQuestion.isNotNullOrBlank() &&
                    timeOnWayOpenQuestion.isNotNullOrBlank()
    }

    private fun isQuestionValid(
        yesNoQuestion: MutableLiveData<Boolean>,
        openQuestion: MutableLiveData<String>
    ) = when (yesNoQuestion.value) {
        null -> false
        true -> !openQuestion.value.isNullOrBlank()
        else -> true
    }

    companion object {
        private const val UNKNOWN_ERROR_REGISTER = "Falha ao cadastrar o usuário"
        private const val UNKNOWN_ERROR_EDIT = "Falha ao editar o usuário"
    }
}