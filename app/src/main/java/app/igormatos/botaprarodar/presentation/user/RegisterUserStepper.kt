package app.igormatos.botaprarodar.presentation.user

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.domain.adapter.StepperAdapter
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterUserStepper(private val initialStep: StepConfigType) : StepperAdapter {

    override val currentStep: MutableStateFlow<StepConfigType> = MutableStateFlow(initialStep)

    override val steps: List<StepConfigType> =
        listOf(
            USER_FORM,
            USER_QUIZ
        )

    override fun navigateToNext() {
        val result = when (currentStep.value) {
            USER_FORM -> steps[1]
            else -> steps[1]
        }

        currentStep.value = result
    }

    override fun navigateToPrevious() {
        val result = when (currentStep.value) {
            USER_QUIZ -> steps[1]
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