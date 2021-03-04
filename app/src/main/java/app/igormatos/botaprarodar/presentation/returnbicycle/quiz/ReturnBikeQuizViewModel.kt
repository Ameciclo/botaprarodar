package app.igormatos.botaprarodar.presentation.returnbicycle.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReturnBikeQuizViewModel : ViewModel() {

    private val _finishQuiz = MutableLiveData<Boolean>()
    val finishQuiz: LiveData<Boolean> = _finishQuiz

    val usedBikeToMoveRg = MutableLiveData(RADIO_INITIAL_VALUE)
    val problemsDuringRidingRg = MutableLiveData(RADIO_INITIAL_VALUE)
    val needTakeRideRg = MutableLiveData(RADIO_INITIAL_VALUE)
    val whichDistrict = MutableLiveData("")

    val isEnabled = MediatorLiveData<Boolean>().apply {
        addSource(usedBikeToMoveRg) {
            validateQuiz()
        }
        addSource(problemsDuringRidingRg) {
            validateQuiz()
        }
        addSource(needTakeRideRg) {
            validateQuiz()
        }
        addSource(whichDistrict) {
            validateQuiz()
        }
    }

    private fun validateQuiz() {
        isEnabled.value = usedBikeToMoveRg.value.isRadioValid() &&
                problemsDuringRidingRg.value.isRadioValid() &&
                needTakeRideRg.value.isRadioValid()
    }

    private fun Int?.isRadioValid() = this != RADIO_INITIAL_VALUE

    fun finishQuiz() {
        _finishQuiz.postValue(true)
    }

    fun setUsedBikeToMoveRb(id: Int) {
        usedBikeToMoveRg.value = id
    }

    fun setProblemsDuringRidingRb(id: Int) {
        problemsDuringRidingRg.value = id
    }

    fun setNeedTakeRideRb(id: Int) {
        needTakeRideRg.value = id
    }

    companion object {
        private const val RADIO_INITIAL_VALUE = -1
    }
}