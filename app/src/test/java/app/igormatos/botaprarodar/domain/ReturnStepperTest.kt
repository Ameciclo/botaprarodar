package app.igormatos.botaprarodar.domain

import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReturnStepperTest {

    private lateinit var stepper: ReturnStepper

    @Before
    fun setup() {
        stepper = ReturnStepper(
            SELECT_BIKE
        )
    }

    @Test
    fun `when stepperManager init then current step should be first step`() {
        val expectedStep = SELECT_BIKE
        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return QUIZ StepConfigType`() {
        val expectedStep = QUIZ

        stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return SELECT_BIKE StepConfigType`() {
        val expectedStep = SELECT_BIKE

        stepper.navigateToPrevious()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return QUIZ StepConfigType`() {
        val expectedStep = QUIZ

        stepper.setCurrentStep(CONFIRM_DEVOLUTION)
        stepper.navigateToPrevious()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return CONFIRM_RETURN StepConfigType`() {
        val expectedStep = CONFIRM_DEVOLUTION

        stepper.setCurrentStep(QUIZ)
        stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when navigateToNext and current step is last step then currentStep should return CONFIRM_RETURN `() {
        val expectedStep = FINISHED_ACTION

        stepper.setCurrentStep(CONFIRM_DEVOLUTION)
        stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }
}
