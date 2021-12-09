package app.igormatos.botaprarodar.presentation.bikeWithdraw

import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

class WithdrawStepperTest {

    private lateinit var stepper: WithdrawStepper
    @Before
    fun setup() {
        stepper = WithdrawStepper(
            StepConfigType.SELECT_BIKE
        )
    }

    @Test
    fun `when stepperManager init then current step should be first step`() {
        val expectedStep = StepConfigType.SELECT_BIKE
        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return SELECT_USER StepConfigType`() {
        val expectedStep = StepConfigType.SELECT_USER

        stepper.navigateToNext()

        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return SELECT_BIKE StepConfigType`() {
        val expectedStep = StepConfigType.SELECT_BIKE

        stepper.navigateToPrevious()

        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToPrevious then currentStep should return SELECT_USER StepConfigType`() {
        val expectedStep = StepConfigType.SELECT_USER

        stepper.setCurrentStep(StepConfigType.CONFIRM_WITHDRAW)
        stepper.navigateToPrevious()

        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when stepper navigateToNext then currentStep should return CONFIRM_WITHDRAW StepConfigType`() {
        val expectedStep = StepConfigType.CONFIRM_WITHDRAW

        stepper.setCurrentStep(StepConfigType.SELECT_USER)
        stepper.navigateToNext()

        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }

    @Test
    fun `when navigateToNext and current step is last step then currentStep should return CONFIRM_WITHDRAW`() {
        val expectedStep = StepConfigType.FINISHED_ACTION

        stepper.setCurrentStep(StepConfigType.CONFIRM_WITHDRAW)
        stepper.navigateToNext()

        Assert.assertEquals(expectedStep, stepper.currentStep.value)
    }
}