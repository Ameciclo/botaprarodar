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

    val alreadyUseBPR = MutableLiveData<Boolean>()

    val alreadyUseBPROpenQuestion = MutableLiveData<String>()

    val motivationOpenQuestion = MutableLiveData("")

    val alreadyAccidentVictim = MutableLiveData<Boolean>()

    val problemsOnWayOpenQuestion = MutableLiveData<String>()

    val timeOnWayOpenQuestion = MutableLiveData<String>()

    private lateinit var deleteImagePaths: List<String>

    private lateinit var user: User

    private val _status = MutableLiveData<ViewModelStatus<String>>()
    val status: LiveData<ViewModelStatus<String>> = _status

    private val _isLgpdAgreement = MutableLiveData<Boolean>()
    val isLgpdAgreement: LiveData<Boolean> = _isLgpdAgreement

    val isButtonEnabled = MediatorLiveData<Boolean>().apply {
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
            alreadyUseBPR.value = it.alreadyUseBPR ?: false
            alreadyUseBPROpenQuestion.value = it.alreadyUseBPROpenQuestion.orEmpty()
            motivationOpenQuestion.value = it.motivationOpenQuestion.orEmpty()
            alreadyAccidentVictim.value = it.alreadyAccidentVictim ?: false
            problemsOnWayOpenQuestion.value = it.problemsOnWayOpenQuestion.orEmpty()
            timeOnWayOpenQuestion.value = it.timeOnWayOpenQuestion.orEmpty()
        }
    }

    fun createUserQuiz() = UserQuiz(
        alreadyUseBPR = alreadyUseBPR.value,
        alreadyUseBPROpenQuestion = alreadyUseBPROpenQuestion.value,
        motivationOpenQuestion = motivationOpenQuestion.value,
        alreadyAccidentVictim = alreadyAccidentVictim.value,
        problemsOnWayOpenQuestion = problemsOnWayOpenQuestion.value,
        timeOnWayOpenQuestion = timeOnWayOpenQuestion.value
    )

    fun onSaveClick() {
        _isLgpdAgreement.value = true
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
                    motivationOpenQuestion.isNotNullOrBlank() &&
                    alreadyAccidentVictim.isNotNull() &&
                    problemsOnWayOpenQuestion.isNotNullOrBlank() &&
                    timeOnWayOpenQuestion.isNotNullOrBlank()
    }

    companion object {
        private const val UNKNOWN_ERROR_REGISTER = "Falha ao cadastrar o usuário"
        private const val UNKNOWN_ERROR_EDIT = "Falha ao editar o usuário"
    }
}
