package app.igormatos.botaprarodar.domain.adapter

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import kotlinx.coroutines.flow.MutableStateFlow

class WithdrawStepper(
    private val initialStep: StepConfigType,
) : StepperAdapter {
    override val currentStep: MutableStateFlow<StepConfigType> = MutableStateFlow(initialStep)
    override val steps: List<StepConfigType> =
        listOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.SELECT_USER,
            StepConfigType.CONFIRM_WITHDRAW,
            StepConfigType.FINISHED_ACTION
        )

    override fun navigateToNext() {
        val result = when (currentStep.value) {
            StepConfigType.SELECT_BIKE -> steps[1]
            StepConfigType.SELECT_USER -> steps[2]
            StepConfigType.CONFIRM_WITHDRAW -> steps[3]
            StepConfigType.FINISHED_ACTION -> steps[3]
            else -> steps[0]
        }
        currentStep.value = result
    }

    override fun navigateToPrevious() {
        val result = when (currentStep.value) {
            StepConfigType.CONFIRM_WITHDRAW -> steps[1]
            StepConfigType.FINISHED_ACTION -> steps[0]
            else -> steps.first()
        }

        currentStep.value = result
    }

    override fun setCurrentStep(currentStep: StepConfigType) {
        this.currentStep.value = currentStep
    }

    override fun navigateToInitialStep() {
        currentStep.value = initialStep
    }
}
