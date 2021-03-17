package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.utils.bikeSimpleError
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycle
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycleInUse
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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
                buildMapStringAndBicycleInUse(3)
            )

            val listResult = useCase.getBikesInUseToReturn("123")

            assertEquals(3, (listResult as SimpleResult.Success).data.size)
            assertEquals("bicycle 3", listResult.data[0].name)
            assertEquals("bicycle 2", listResult.data[1].name)
            assertEquals("bicycle 1", listResult.data[2].name)
        }

    @Test
    fun `when call getBikesInUseToReturn() then some bike is in use but the communityId is different should return an error`() =
        runBlocking {
            coEvery { repository.getBicycles() } returns SimpleResult.Success(
                buildMapStringAndBicycleInUse(3)
            )

            val listResult = useCase.getBikesInUseToReturn("123456")

            assertTrue(listResult is SimpleResult.Error)
        }

    @Test
    fun `when call getBikesInUseToReturn() then none bike is in use should return an error`() =
        runBlocking {
            coEvery { repository.getBicycles() } returns SimpleResult.Success(
                buildMapStringAndBicycle(3)
            )

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