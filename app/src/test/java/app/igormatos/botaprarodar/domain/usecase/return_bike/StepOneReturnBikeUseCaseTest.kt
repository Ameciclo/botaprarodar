package app.igormatos.botaprarodar.domain.usecase.return_bike

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.presentation.returnbicycle.stepOneReturnBike.StepOneReturnBikeUseCase
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class StepOneReturnBikeUseCaseTest {

    private val repository = mockk<BikeRepository>()
    private lateinit var useCase: StepOneReturnBikeUseCase

    @Before
    fun setup() {
        useCase = StepOneReturnBikeUseCase(repository)
    }

    @Test
    fun `when call getBikesInUseToReturn() then some bike is in use should return a success`() =
        runBlocking {
            coEvery { repository.getBicycles() } returns SimpleResult.Success(
                bikeList
            )

            val listResult = useCase.getBikesInUseToReturn("123")

            assertEquals(availableBikes.size, (listResult as SimpleResult.Success).data.size)
        }


    @Test
    fun `when call getBikesInUseToReturn() then none bike is in use should return an error`() =
        runBlocking {
            coEvery { repository.getBicycles() } returns SimpleResult.Success(bikeList)

            val listResult = useCase.getBikesInUseToReturn("10")

            assertTrue(listResult is SimpleResult.Error)
        }

    @Test
    fun `when call getBikesInUseToReturn() then an error is returned should return an error`() =
        runBlocking {
            coEvery { repository.getBicycles() } returns bikeSimpleError

            val listResult = useCase.getBikesInUseToReturn("10")

            assertTrue(listResult is SimpleResult.Error)
        }
}