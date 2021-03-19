package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ReturnBikeQuizViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val stepperAdapter = spyk(ReturnStepper(StepConfigType.QUIZ))
    private val quizBuilder = spyk<BikeDevolutionQuizBuilder>()
    private lateinit var viewModel: ReturnBikeQuizViewModel

    @Before
    fun setup() {
        viewModel = ReturnBikeQuizViewModel(stepperAdapter, quizBuilder)
    }

    @Test
    fun `When 'quizForm' is invalid then 'isEnabled' return false`() {

        val observerQuizForm = mockk<Observer<Boolean>>(relaxed = true)

        viewModel.isEnabled.observeForever(observerQuizForm)
        viewModel.problemsDuringRidingRg.value = "Não"
        viewModel.usedBikeToMoveRg.value = "Trabalho"
        viewModel.whichDistrict.value = "Bairro qualquer"

        verify {
            observerQuizForm.onChanged(false)
        }
    }

    @Test
    fun `When 'quizForm' is valid then 'isEnabled' return true`() {

        val observerQuizForm = mockk<Observer<Boolean>>(relaxed = true)

        viewModel.isEnabled.observeForever(observerQuizForm)
        viewModel.problemsDuringRidingRg.value = "Não"
        viewModel.needTakeRideRg.value = "Não"
        viewModel.usedBikeToMoveRg.value = "Trabalho"
        viewModel.whichDistrict.value = "Bairro qualquer"

        verify {
            observerQuizForm.onChanged(true)
        }
    }

    @Test
    fun `when call navigateToNextStep() then the stepperAdapter should be update with the CONFIRM_RETURN value`() {
        viewModel.navigateToNextStep()

        verify { stepperAdapter.navigateToNext() }

        assertEquals(viewModel.stepperAdapter.currentStep.value, StepConfigType.CONFIRM_DEVOLUTION)
    }

    @Test
    fun `when call setClickToNextFragmentToFalse() then the 'clickToNextFragment' should be update to false value`() {
        val observerClickToNextFragment = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.clickToNextFragment.observeForever(observerClickToNextFragment)

        viewModel.setClickToNextFragmentToFalse()

        verify { observerClickToNextFragment.onChanged(false) }

        assertEquals(viewModel.clickToNextFragment.value, false)
    }

    @Test
    fun `when call 'finishQuiz()' then the 'clickToNextFragment' should be update to true value`() {
        val observerClickToNextFragment = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.clickToNextFragment.observeForever(observerClickToNextFragment)

        viewModel.finishQuiz()

        verify {
            observerClickToNextFragment.onChanged(true)
            viewModel.quizBuilder.withAnswer2("")
        }

        assertEquals(viewModel.clickToNextFragment.value, true)
    }

    @Test
    fun `when call 'setUsedBikeToMoveRb()' then the 'usedBikeToMoveRg' should be update with the correct value`() {
        val expectedReason = "Seu local de trabalho"

        viewModel.setUsedBikeToMoveRb(R.id.workplaceRb)

        verify { viewModel.quizBuilder.withAnswer1(expectedReason) }

        Assert.assertEquals(expectedReason, viewModel.usedBikeToMoveRg.value)
    }

    @Test
    fun `when call 'setProblemsDuringRidingRb()' then the 'problemsDuringRidingRg' should be update with the correct value`() {
        val expectedSufferedViolence = "Não"

        viewModel.setProblemsDuringRidingRb(R.id.problemsDuringRidingNo)

        verify { viewModel.quizBuilder.withAnswer3(expectedSufferedViolence) }

        Assert.assertEquals(expectedSufferedViolence, viewModel.problemsDuringRidingRg.value)
    }

    @Test
    fun `when call 'setNeedTakeRideRb()' then the 'needTakeRideRg' should be update with the correct value`() {
        val expectedGiveRide = "Não"

        viewModel.setNeedTakeRideRb(R.id.needTakeRideNo)

        verify { viewModel.quizBuilder.withAnswer4(expectedGiveRide) }

        Assert.assertEquals(expectedGiveRide, viewModel.needTakeRideRg.value)
    }
}