package app.igormatos.botaprarodar.domain.adapter

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import kotlinx.coroutines.flow.MutableStateFlow

class ReturnStepper(
    private val initialStep: StepConfigType
) : StepperAdapter {
    override val currentStep: MutableStateFlow<StepConfigType> = MutableStateFlow(initialStep)
    override val steps: List<StepConfigType> =
        listOf(
            SELECT_BIKE,
            QUIZ,
            CONFIRM_DEVOLUTION,
            FINISHED_ACTION
        )
    override fun navigateToNext() {
        val result = when (currentStep.value) {
            SELECT_BIKE -> steps[1]
            QUIZ -> steps[2]
            else -> steps[3]
        }
        currentStep.value = result
    }

    override fun navigateToPrevious() {
        val result = when (currentStep.value) {
            CONFIRM_DEVOLUTION -> steps[1]
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