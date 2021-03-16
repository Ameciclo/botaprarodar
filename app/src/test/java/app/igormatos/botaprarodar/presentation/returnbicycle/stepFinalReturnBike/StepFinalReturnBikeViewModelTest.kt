package app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.domain.usecase.returnbicycle.StepFinalReturnBikeUseCase
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.bikeSimpleError
import app.igormatos.botaprarodar.utils.bikeSimpleSuccess
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StepFinalReturnBikeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val useCase = mockk<StepFinalReturnBikeUseCase>()
    private val bikeHolder = spyk(BikeHolder())
    private val quizBuilder = spyk(BikeDevolutionQuizBuilder())
    private lateinit var viewModel: StepFinalReturnBikeViewModel

    @Before
    fun setup() {
        viewModel = StepFinalReturnBikeViewModel(
            quizBuilder = quizBuilder,
            stepFinalUseCase = useCase,
            bikeHolder = bikeHolder
        )
    }

    @Test
    fun `given bikeHolder receive a new value, when call getBikeHolder() the new values should be in the bike`() {
        bikeHolder.bike = bike

        viewModel.getBikeHolder()

        verify { bikeHolder.bike }

        assertEquals(bike.name, viewModel.getBikeHolder()?.name)
    }

    @Test
    fun `given a new devolution, when call finalizeDevolution() should return success`() {
        coEvery { useCase.addDevolution(any(), any(), any()) } returns bikeSimpleSuccess

        viewModel.finalizeDevolution()

        assert(viewModel.state.value is SimpleResult.Success)
    }

    @Test
    fun `given a new devolution, when call finalizeDevolution() should return error`() {
        coEvery { useCase.addDevolution(any(), any(), any()) } returns bikeSimpleError

        viewModel.finalizeDevolution()

        assert(viewModel.state.value is SimpleResult.Error)
    }
}