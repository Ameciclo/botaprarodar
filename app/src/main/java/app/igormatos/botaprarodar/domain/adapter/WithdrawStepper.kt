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
            StepConfigType.CONFIRM_WITHDRAW
        )

    override fun navigateToNext() {
        val result = when (currentStep.value) {
            StepConfigType.SELECT_BIKE -> steps[1]
            else -> steps[2]
        }
        currentStep.value = result
    }

    override fun navigateToPrevious() {
        val result = when (currentStep.value) {
            StepConfigType.CONFIRM_WITHDRAW -> steps[1]
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