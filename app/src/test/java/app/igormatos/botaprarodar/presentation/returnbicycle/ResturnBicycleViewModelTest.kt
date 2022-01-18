package app.igormatos.botaprarodar.presentation.returnbicycle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.utils.listBikes
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class ResturnBicycleViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val stepperAdapter = spyk(ReturnStepper(StepConfigType.SELECT_BIKE))
    private val stepOneReturnBikeUseCase = mockk<StepOneReturnBikeUseCase>()
    private val bikeHolder = spyk<BikeHolder>()
    private lateinit var viewModel: ReturnBicycleViewModel

    @Test
    fun `when viewModel getBikesInUseToReturn return then sets _bikesAvailableToReturn`() {
        viewModel = ReturnBicycleViewModel(
            stepperAdapter = stepperAdapter,
            bikeHolder = bikeHolder,
            stepOneReturnBikeUseCase = stepOneReturnBikeUseCase
        )
        coEvery {
            stepOneReturnBikeUseCase.getBikesInUseToReturn(any())
        } returns SimpleResult.Success(listBikes)
        viewModel.getBikesInUseToReturn("12345")
        val bikesReturned = (viewModel.bikesAvailableToReturn.value as SimpleResult.Success).data
        assertTrue(viewModel.bikesAvailableToReturn.value is SimpleResult.Success)
        assertEquals(bikesReturned, listBikes)
    }
}