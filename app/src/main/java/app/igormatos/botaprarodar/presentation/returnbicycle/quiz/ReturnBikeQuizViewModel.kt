package app.igormatos.botaprarodar.presentation.returnbicycle.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.data.local.quiz.DevolutionQuizAnswerName
import app.igormatos.botaprarodar.data.local.quiz.QuizBuilder
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter

class ReturnBikeQuizViewModel(
    val stepperAdapter: StepperAdapter.ReturnStepper,
    val quizBuilder: BikeDevolutionQuizBuilder
) : ViewModel() {

    private val _clickToNextFragment = MutableLiveData<Boolean>(false)
    val clickToNextFragment: LiveData<Boolean> = _clickToNextFragment

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
                needTakeRideRg.value.isRadioValid() &&
                isTextValid(whichDistrict.value)

    }

    private fun String?.isRadioValid() = this != RADIO_INITIAL_VALUE

    private fun isTextValid(data: String?) = !data.isNullOrBlank()

    fun navigateToNextStep() {
        stepperAdapter.navigateToNext()
    }

    fun setClickToNextFragmentToFalse() {
        _clickToNextFragment.value = false
    }

    fun finishQuiz() {
        _clickToNextFragment.postValue(true)
    }

    fun setUsedBikeToMoveRb(id: Int) {
        usedBikeToMoveRg.value = getReasonByRadioButton(id)
        quizBuilder.withAnswer1(getReasonByRadioButton(id))
    }

    fun setProblemsDuringRidingRb(id: Int) {
        problemsDuringRidingRg.value = getYesOrNoByRadioButton(id)
        quizBuilder.withAnswer3(getYesOrNoByRadioButton(id))
    }

    fun setNeedTakeRideRb(id: Int) {
        needTakeRideRg.value = getYesOrNoByRadioButton(id)
        quizBuilder.withAnswer4(getYesOrNoByRadioButton(id))
        quizBuilder
    }

    companion object {
        private const val RADIO_INITIAL_VALUE = ""
    }
}