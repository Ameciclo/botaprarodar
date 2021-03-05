package app.igormatos.botaprarodar.presentation.return_bike

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ReturnBikeViewModelTest {

    private val stepper = StepperAdapter.ReturnStepper(StepConfigType.SELECT_BIKE)
    private val viewModel = ReturnBikeViewModel(stepper)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `when init viewModel then steps should be SELECT_BIKE`() = runBlocking {

        val observer = spyk<Observer<StepConfigType>>()

        viewModel.uiState.observeForever(observer)

        verify {
            observer.onChanged(StepConfigType.SELECT_BIKE)
        }
    }

    @Test
    fun `stepStatus should match Stepper currentStep sort`() = runBlocking {
        val observer = spyk<Observer<StepConfigType>>()

        viewModel.uiState.observeForever(observer)

        stepper.currentStep.value = StepConfigType.QUIZ
        stepper.currentStep.value = StepConfigType.CONFIRM_RETURN

        verifyOrder {
            observer.onChanged(StepConfigType.SELECT_BIKE)
            observer.onChanged(StepConfigType.QUIZ)
            observer.onChanged(StepConfigType.CONFIRM_RETURN)
        }
    }
}
