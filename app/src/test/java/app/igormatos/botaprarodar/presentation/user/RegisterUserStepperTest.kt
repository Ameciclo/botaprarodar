package app.igormatos.botaprarodar.presentation.user

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class RegisterUserStepperTest {

    private val registerUserStepper = RegisterUserStepper(USER_PERSONAl_INFO)
    private val steps: List<StepConfigType> =
        listOf(
            USER_PERSONAl_INFO,
            USER_SOCIAL_INFO,
            USER_MOTIVATION,
            USER_FINISHED
        )

    @Test
    fun `SHOULD return USER_SOCIAL_INFO step WHEN call navigationToNext last step is USER_PERSONAL_INFO`() {
        registerUserStepper.navigateToNext()
        assertEquals(steps[1],  registerUserStepper.currentStep.value)
    }

    @Test
    fun `SHOULD return USER_MOTIVATION step WHEN call navigationToNext and last step is USER_SOCIAL_INFO`() {
        registerUserStepper.currentStep.value = USER_SOCIAL_INFO
        registerUserStepper.navigateToNext()
        assertEquals(steps[2],  registerUserStepper.currentStep.value)
    }

    @Test
    fun `SHOULD return USER_FINISHED step WHEN call navigationToNext and last step is USER_MOTIVATION`() {
        registerUserStepper.currentStep.value = USER_MOTIVATION
        registerUserStepper.navigateToNext()
        assertEquals(steps[3],  registerUserStepper.currentStep.value)
    }

    @Test
    fun `SHOULD return USER_MOTIVATION step WHEN call navigateToPrevious and last step is USER_FINISHED`() {
        registerUserStepper.currentStep.value = USER_FINISHED
        registerUserStepper.navigateToPrevious()
        assertEquals(steps[2],  registerUserStepper.currentStep.value)
    }

    @Test
    fun `SHOULD return USER_SOCIAL_INFO step WHEN call navigateToPrevious and last step is USER_MOTIVATION`() {
        registerUserStepper.currentStep.value = USER_MOTIVATION
        registerUserStepper.navigateToPrevious()
        assertEquals(steps[1],  registerUserStepper.currentStep.value)
    }

    @Test
    fun `SHOULD return USER_PERSONAl_INFO step WHEN call navigateToPrevious and last step is USER_SOCIAL_INFO`() {
        registerUserStepper.currentStep.value = USER_SOCIAL_INFO
        registerUserStepper.navigateToPrevious()
        assertEquals(steps[0],  registerUserStepper.currentStep.value)
    }

}
