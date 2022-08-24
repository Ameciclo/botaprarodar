package app.igormatos.botaprarodar.presentation.user

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.domain.adapter.StepperAdapter
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterUserStepper(private val initialStep: StepConfigType) : StepperAdapter {

    override val currentStep: MutableStateFlow<StepConfigType> = MutableStateFlow(initialStep)

    override val steps: List<StepConfigType> =
        listOf(
            USER_PERSONAl_INFO,
            USER_SOCIAL_INFO,
            USER_MOTIVATION,
            USER_FINISHED
        )

    override fun navigateToNext() {
        val result = when (currentStep.value) {
            USER_PERSONAl_INFO -> steps[1]
            USER_SOCIAL_INFO -> steps[2]
            else -> steps[3]
        }

        currentStep.value = result
    }

    override fun navigateToPrevious() {
        val result = when (currentStep.value) {
            USER_FINISHED -> steps[2]
            USER_MOTIVATION -> steps[1]
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
