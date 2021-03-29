package app.igormatos.botaprarodar.presentation.user.userquiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserQuizViewModel : ViewModel()  {

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

    val quizTimeOnWayOpenQuestion = MutableLiveData<String>()

    fun onSaveClick() {

    }
}