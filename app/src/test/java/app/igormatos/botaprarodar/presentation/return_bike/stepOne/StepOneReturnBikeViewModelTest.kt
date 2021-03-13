package app.igormatos.botaprarodar.presentation.return_bike.stepOne

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.utils.listBikes
import com.brunotmgomes.ui.SimpleResult
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StepOneReturnBikeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val stepperAdapter = mockk<ReturnStepper>(relaxed = true)
    private val stepOneReturnBikeUseCase = mockk<StepOneReturnBikeUseCase>()
    private val bikeHolder = mockk<BikeHolder>(relaxed = true)
    private lateinit var viewModel: StepOneReturnBikeViewModel

    @Before
    fun setup() {
        viewModel = StepOneReturnBikeViewModel(stepperAdapter, stepOneReturnBikeUseCase, bikeHolder)
    }

    @Test
    fun `when call getBikesInUseToReturn() and the return is success then should return a list of bike`() {
        coEvery {
            stepOneReturnBikeUseCase.getBikesInUseToReturn(any())
        } returns SimpleResult.Success(listBikes)

        viewModel.getBikesInUseToReturn("12345")
        val bikesReturned = (viewModel.bikesAvailableToReturn.value as SimpleResult.Success).data

        assertTrue(viewModel.bikesAvailableToReturn.value is SimpleResult.Success)
        assertEquals(bikesReturned, listBikes)
    }

    @Test
    fun `when call setInitialStep() then the stepperAdapter should be update with the SELECT_BIKE value`() {
        viewModel.setInitialStep()

        val slot = slot<StepConfigType>()
        verify { stepperAdapter.setCurrentStep(capture(slot)) }

        assertEquals(slot.captured, StepConfigType.SELECT_BIKE)
    }

    @Test
    fun `when call navigateToNextStep() then the stepperAdapter should be update with the new value`() {
        viewModel.navigateToNextStep()

        verify {
            stepperAdapter.navigateToNext()
        }
    }
}
