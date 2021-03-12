package app.igormatos.botaprarodar.presentation.return_bike.stepFinalReturnBike

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.presentation.returnbicycle.StepperAdapter
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.StepFinalReturnBikeViewModel
import app.igormatos.botaprarodar.utils.bike
import io.mockk.spyk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StepFinalReturnBikeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val stepperAdapter = spyk(StepperAdapter.ReturnStepper(StepConfigType.SELECT_BIKE))
    private val bikeHolder = spyk(BikeHolder())
    private lateinit var viewModel: StepFinalReturnBikeViewModel

    @Before
    fun setup() {
        viewModel = StepFinalReturnBikeViewModel(stepperAdapter, bikeHolder)
    }

    @Test
    fun `given bikeHolder receive a new value,  when call getBikeHolder() the new values should be in the bike`() {
        bikeHolder.bike = bike

        viewModel.getBikeHolder()

        verify { bikeHolder.bike }

        assertEquals(bike.name, viewModel.getBikeHolder()?.name)
    }
}