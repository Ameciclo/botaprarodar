package app.igormatos.botaprarodar.presentation.return_bike.stepOne

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneBikes.StepOneReturnBikeViewModel
import app.igormatos.botaprarodar.utils.listBikes
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
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

    private val stepperAdapter = mockk<StepperAdapter.ReturnStepper>()
    private val stepOneReturnBikeUseCase = mockk<StepOneReturnBikeUseCase>()
    private lateinit var viewModel: StepOneReturnBikeViewModel

    @Before
    fun setup() {
        viewModel = StepOneReturnBikeViewModel(stepperAdapter, stepOneReturnBikeUseCase)
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

}