package app.igormatos.botaprarodar.domain

import app.igormatos.botaprarodar.common.enumType.StepConfigType.*
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WithdrawStepperTest {

    private lateinit var stepper: StepperAdapter

    @Before
    fun setup(){
        stepper = StepperAdapter.ReturnStepper(SELECT_BIKE)
    }

    @Test
    fun `when stepperManager init then current step should be first step`(){
        val expectedStep = SELECT_BIKE
        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return QUIZ StepConfigType`(){
        val expectedStep = QUIZ

       stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return SELECT_BIKE StepConfigType`(){
        val expectedStep = SELECT_BIKE

       stepper.navigateToPrevious()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return QUIZ StepConfigType`(){
        val expectedStep = QUIZ

       stepper.setCurrentStep(CONFIRM_RETURN)
       stepper.navigateToPrevious()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return CONFIRM_RETURN StepConfigType`(){
        val expectedStep = CONFIRM_RETURN

        stepper.setCurrentStep(QUIZ)
        stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when navigateToNext and current step is last step then currentStep should return CONFIRM_RETURN `(){
        val expectedStep = CONFIRM_RETURN

        stepper.setCurrentStep(CONFIRM_RETURN)
        stepper.navigateToNext()

        assertEquals(expectedStep, stepper.currentStep.value)
    }
}
