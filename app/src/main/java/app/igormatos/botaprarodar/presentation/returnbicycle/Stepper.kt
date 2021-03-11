package app.igormatos.botaprarodar.presentation.returnbicycle

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import kotlinx.coroutines.flow.MutableStateFlow

sealed class StepperAdapter(val steps: List<StepConfigType>) {
    abstract fun navigateToNext()
    abstract fun navigateToPrevious()
    abstract fun backToInitialState()
    abstract val currentStep: MutableStateFlow<StepConfigType>

    class ReturnStepper(private val initialStep: StepConfigType) : StepperAdapter(
        listOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.QUIZ,
            StepConfigType.CONFIRM_RETURN
        )
    ) {
        private var _currentStep: StepConfigType = initialStep
        override val currentStep: MutableStateFlow<StepConfigType> = MutableStateFlow(_currentStep)

        override fun navigateToNext() {
            val result = when(_currentStep){
                StepConfigType.SELECT_BIKE -> steps[1]
                else -> steps[2]
            }
            _currentStep = result
            currentStep.value = result
        }

        override fun navigateToPrevious() {
            val result = when(_currentStep){
                StepConfigType.CONFIRM_RETURN -> steps[1]
                else -> steps.first()
            }

            _currentStep = result
            currentStep.value = result
        }

        override fun backToInitialState() {
            _currentStep = StepConfigType.SELECT_BIKE
            currentStep.value = _currentStep
        }

        override fun setCurrentStep(currentStep: StepConfigType) {
            _currentStep = currentStep
        }
    }

    class WithdrawStepper(private val initialStep: StepConfigType) : StepperAdapter(
        listOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.QUIZ,
            StepConfigType.CONFIRM_RETURN
        )
    ) {
        private var _currentStep: StepConfigType = steps.first()
        override val currentStep: MutableStateFlow<StepConfigType>
            get() = MutableStateFlow(_currentStep)

        override fun setCurrentStep(currentStep: StepConfigType) {

        }

        override fun navigateToNext() {
        }

        override fun backToInitialState() {
        }

        override fun navigateToPrevious() {
        }
    }

    abstract fun setCurrentStep(currentStep: StepConfigType)
}
