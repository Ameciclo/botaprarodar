package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper

class ReturnBikeQuizViewModel(
    val stepperAdapter: ReturnStepper,
    val quizBuilder: BikeDevolutionQuizBuilder
) : ViewModel() {

    private val _clickToNextFragment = MutableLiveData<Boolean>(false)
    val clickToNextFragment: LiveData<Boolean> = _clickToNextFragment

    val problemsDuringRidingRg = MutableLiveData(RADIO_INITIAL_VALUE)
    val needTakeRideRg = MutableLiveData(RADIO_INITIAL_VALUE)
    val whichDistrict = MutableLiveData("")

    val isEnabled = MediatorLiveData<Boolean>().apply {
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
        isEnabled.value =
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
        quizBuilder.withAnswer2(whichDistrict.value.orEmpty())
        _clickToNextFragment.postValue(true)
    }

    fun setUsedBikeToMoveRb(id: Int) {
//        quizBuilder.withAnswer1(getReasonByRadioButton(id))
    }

    fun setProblemsDuringRidingRb(id: Int) {
        problemsDuringRidingRg.value = getYesOrNoByRadioButton(id)
        quizBuilder.withAnswer3(getYesOrNoByRadioButton(id))
    }

    fun setNeedTakeRideRb(id: Int) {
        needTakeRideRg.value = getYesOrNoByRadioButton(id)
        quizBuilder.withAnswer4(getYesOrNoByRadioButton(id))
    }

    companion object {
        private const val RADIO_INITIAL_VALUE = ""
    }
}