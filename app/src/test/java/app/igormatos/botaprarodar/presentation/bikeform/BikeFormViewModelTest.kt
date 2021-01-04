package app.igormatos.botaprarodar.presentation.bikeform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.Status
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.AddNewBikeUseCase
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormViewModel
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue

class BikeFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val addNewBikeUseCase = mockk<AddNewBikeUseCase>()
    private var community = mockk<Community>(relaxed = true)

    lateinit var bikeViewModel: BikeFormViewModel

    @Before
    fun setup(){
        bikeViewModel = BikeFormViewModel(addNewBikeUseCase = addNewBikeUseCase, community = community)
    }

    @Test
    fun `When 'registerBicycle', then registeredBicycleResult should return Success Status`() = runBlocking{
        val bikeFake = mockk<Bike>()
        val result = SimpleResult.Success<String>("")

        coEvery {
            addNewBikeUseCase.addNewBike(community.id, bikeFake)
        } returns result

        bikeViewModel.registerBicycle(bikeFake)

        assertTrue(bikeViewModel.getRegisteredBicycleResult().value is Status.Success)
    }

    @Test
    fun `When 'registerBicycle', then registeredBicycleResult should return Error Status`() = runBlocking{
        val bikeFake = mockk<Bike>()
        val result = SimpleResult.Error(Exception())

        coEvery {
            addNewBikeUseCase.addNewBike(community.id, bikeFake)
        } returns result

        bikeViewModel.registerBicycle(bikeFake)

        assertTrue(bikeViewModel.getRegisteredBicycleResult().value is Status.Error)
    }

    @Test
    fun `check success status ordering`(){
        val bikeFake = mockk<Bike>()
        val observerBikeResultMock = mockk<Observer<Status<String>>>(relaxed = true)
        val simpleResultData = "bicicleta caloi"
        val result = SimpleResult.Success(simpleResultData)

        coEvery {
            addNewBikeUseCase.addNewBike(community.id, bikeFake)
        } returns result

        bikeViewModel.registerBicycle(bikeFake)
        bikeViewModel.getRegisteredBicycleResult().observeForever(observerBikeResultMock)

        verifyOrder{
            observerBikeResultMock.onChanged(Status.Loading())
            observerBikeResultMock.onChanged(Status.Success(simpleResultData))
        }
    }

}