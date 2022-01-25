package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.bike2
import app.igormatos.botaprarodar.utils.listBikes
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class ReturnBicycleViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val stepperAdapter = spyk(ReturnStepper(StepConfigType.SELECT_BIKE))
    private val stepOneReturnBikeUseCase = mockk<StepOneReturnBikeUseCase>()
    private val bikeHolder = spyk<BikeHolder>()
    private lateinit var viewModel: ReturnBicycleViewModel

    @Before
    fun setup() {
        viewModel = ReturnBicycleViewModel(
            stepperAdapter = stepperAdapter,
            bikeHolder = bikeHolder,
            stepOneReturnBikeUseCase = stepOneReturnBikeUseCase
        )
    }

    @Test
    fun `when viewModel getBikesInUseToReturn return then sets _bikesAvailableToReturn`() {
        coEvery {
            stepOneReturnBikeUseCase.getBikesInUseToReturn(any())
        } returns SimpleResult.Success(listBikes)
        viewModel.getBikesInUseToReturn("12345")
        val bikesReturned = (viewModel.bikesAvailableToReturn.value as SimpleResult.Success).data
        assertTrue(viewModel.bikesAvailableToReturn.value is SimpleResult.Success)
        assertEquals(bikesReturned, listBikes)
    }

    @Test
    fun `when viewModel getBikesInUseToReturn with community in blank should be return error`() {
        coEvery {
            stepOneReturnBikeUseCase.getBikesInUseToReturn("")
        } returns SimpleResult.Error(Exception("Error"))
        viewModel.getBikesInUseToReturn("")
        assertTrue(viewModel.bikesAvailableToReturn.value is SimpleResult.Error)
    }

    @Test
    fun `when viewModel call nextStep, currentStep should be QUIZ`() {
        viewModel.setInitialStep()
        verify { viewModel.setInitialStep() }
        viewModel.navigateToNextStep()
        verify { viewModel.navigateToNextStep() }
        assertEquals(viewModel.stepperAdapter.currentStep.value, StepConfigType.QUIZ)
    }

}