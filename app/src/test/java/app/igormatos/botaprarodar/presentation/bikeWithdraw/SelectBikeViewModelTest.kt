package app.igormatos.botaprarodar.presentation.bikeWithdraw

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.WithdrawStepper
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.usecase.bikes.GetAvailableBikes
import app.igormatos.botaprarodar.presentation.bikewithdraw.viewmodel.SelectBikeViewModel
import app.igormatos.botaprarodar.presentation.returnbicycle.BikeHolder
import app.igormatos.botaprarodar.utils.availableBikes
import app.igormatos.botaprarodar.utils.communityFixture
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SelectBikeViewModelTest {
    private val bikeHolderMock = mockk<BikeHolder>()
    private val withdrawStepperMock = mockk<WithdrawStepper>()
    private val getAvailableBikesMock = mockk<GetAvailableBikes>()

    private val selectBikeViewModel =
        SelectBikeViewModel(bikeHolderMock, withdrawStepperMock, getAvailableBikesMock)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun `when setInitialStep then should call withdrawStepper#setCurrentStep`() = runBlocking {
        val stepTypeSlot = slot<StepConfigType>()

        every { withdrawStepperMock.setCurrentStep(capture(stepTypeSlot)) } returns Unit

        selectBikeViewModel.setInitialStep()

        verify {
            withdrawStepperMock.setCurrentStep(stepTypeSlot.captured)
        }

        assertEquals(StepConfigType.SELECT_BIKE, stepTypeSlot.captured)
    }

    @Test
    fun `when navigateToNextStep then should call withdrawStepper#navigateToNext`() = runBlocking {
        every {
            withdrawStepperMock.navigateToNext()
        } returns Unit

        selectBikeViewModel.navigateToNextStep()

        verify {
            withdrawStepperMock.navigateToNext()
        }
    }

    @Test
    fun `when getAvailableBikes then availableBikes value should return availableBikes`() =
        runBlocking {
            val mockObserver = spyk<Observer<List<Bike>>>()
            selectBikeViewModel.availableBikes.observeForever(mockObserver)

            coEvery {
                getAvailableBikesMock.execute(communityFixture.id)
            } returns availableBikes

            selectBikeViewModel.getBikeList(communityFixture.id)

            verify {
                mockObserver.onChanged(availableBikes)
            }
        }
}
