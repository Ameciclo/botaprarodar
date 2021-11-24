package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import com.brunotmgomes.ui.extensions.isNotNullOrNotBlank

class ReturnBikeQuizViewModel(
    val stepperAdapter: ReturnStepper,
    val quizBuilder: BikeDevolutionQuizBuilder
) : ViewModel() {

    private val _clickToNextFragment = MutableLiveData(false)
    val clickToNextFragment: LiveData<Boolean> = _clickToNextFragment

    val reason = MutableLiveData(INITIAL_VALUE)
    val problemsDuringRidingRg = MutableLiveData(INITIAL_VALUE)
    val needTakeRideRg = MutableLiveData(INITIAL_VALUE)
    val whichDistrict = MutableLiveData("")

    val isEnabled = MediatorLiveData<Boolean>().apply {
        addSource(reason) { validateQuiz() }
        addSource(problemsDuringRidingRg) { validateQuiz() }
        addSource(needTakeRideRg) { validateQuiz() }
        addSource(whichDistrict) { validateQuiz() }
    }

    private fun validateQuiz() {
        isEnabled.value = reason.value.isNotNullOrNotBlank() &&
                problemsDuringRidingRg.value.isRadioValid() &&
                needTakeRideRg.value.isRadioValid() &&
                isTextValid(whichDistrict.value)

    }

    private fun String?.isRadioValid() = this != INITIAL_VALUE

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

    fun setUsedBikeToMove(reason: String) {
        this.reason.value = reason
        quizBuilder.withAnswer1(reason)
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
        private const val INITIAL_VALUE = ""
    }
}