package app.igormatos.botaprarodar.presentation.return_bike

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBikeViewModel
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReturnBikeViewModelTest {

    private val stepper = mockk<ReturnStepper>(relaxed = true)
    private lateinit var viewModel: ReturnBikeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        every { stepper.currentStep } returns MutableStateFlow(StepConfigType.SELECT_BIKE)
        viewModel = ReturnBikeViewModel(stepper)
    }

    @Test
    fun `when init viewModel then steps should be SELECT_BIKE`() = runBlocking {

        val observer = spyk<Observer<StepConfigType>>()
        viewModel.uiState.observeForever(observer)

        coVerify {
            observer.onChanged(StepConfigType.SELECT_BIKE)
        }
    }

    @Test
    fun `when backToInitialStep then stepper should be in SELECT_BIKE state `() = runBlocking {
        val stepTypeSlot = slot<StepConfigType>()
        viewModel.backToInitialState()

        verify {
            stepper.setCurrentStep(capture(stepTypeSlot))
        }

        assertEquals(stepTypeSlot.captured, StepConfigType.SELECT_BIKE)
    }
}
