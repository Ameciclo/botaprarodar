package app.igormatos.botaprarodar.presentation.user.userquiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import com.brunotmgomes.ui.extensions.isNotNull
import com.brunotmgomes.ui.extensions.isNotNullOrBlank

class UserQuizViewModel(
    private val userUseCase: UserFormUseCase,
    private val community: Community
) : ViewModel()  {

    var isEditableAvailable = false

    val accessOtherTransport = MutableLiveData<Boolean>()

    val showOtherTransportOpenQuestion: LiveData<Boolean>
        get() = accessOtherTransport

    val accessOtherTransportOpenQuestion = MutableLiveData<String>()

    val alreadyUseBPR = MutableLiveData<Boolean>()

    val showUseBPROpenQuestion : LiveData<Boolean>
        get() = alreadyUseBPR

    val alreadyUseBPROpenQuestion = MutableLiveData<String>()

    val motivationOpenQuestion = MutableLiveData<String>()

    val alreadyAccidentVictim = MutableLiveData<Boolean>()

    val problemsOnWayOpenQuestion = MutableLiveData<String>()

    val timeOnWayOpenQuestion = MutableLiveData<String>()

    private var user = User()

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

    fun onSaveClick() {

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
}