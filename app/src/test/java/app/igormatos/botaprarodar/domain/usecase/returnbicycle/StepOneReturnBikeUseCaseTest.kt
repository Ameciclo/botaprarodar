package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.utils.bikeSimpleError
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycle
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycleInUse
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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
    private var communityId = "123"


    @Before
    fun setup() {
        useCase = StepOneReturnBikeUseCase(repository)
    }

    @Test
    fun `when call getBikesInUseToReturn() then some bike is in use should return a success`() =
        runBlocking {
            coEvery { repository.getBikesByCommunityId(communityId) } returns SimpleResult.Success(
                buildMapStringAndBicycleInUse(3)
            )

            val listResult = useCase.getBikesInUseToReturn(communityId)

            assertEquals(3, (listResult as SimpleResult.Success).data.size)
            assertEquals("bicycle 3", listResult.data[0].name)
            assertEquals("bicycle 2", listResult.data[1].name)
            assertEquals("bicycle 1", listResult.data[2].name)
        }

    @Test
    fun `when call getBikesInUseToReturn() then none bike is in use should return an error`() =
        runBlocking {
            communityId = "10"
            coEvery { repository.getBikesByCommunityId(communityId) } returns SimpleResult.Success(
                buildMapStringAndBicycle(3)
            )

            val listResult = useCase.getBikesInUseToReturn(communityId)

            assertTrue(listResult is SimpleResult.Error)
        }

    @Test
    fun `when call getBikesInUseToReturn() then an error is returned should return an error`() =
        runBlocking {
            communityId = "10"
            coEvery { repository.getBikesByCommunityId(communityId) } returns bikeSimpleError

            val listResult = useCase.getBikesInUseToReturn(communityId)

            assertTrue(listResult is SimpleResult.Error)
        }

    @Test
    fun `when call getBicycleReturnUseMap then should return map does not null ou empty`() {
        val returnUseMap = mapOf(
            0 to "Para realizar entregas de aplicativos.",
            1 to "Deslocar para o local de trabalho.",
            2 to "Deslocar para o local de estudo."
        )
        every { repository.getBicycleReturnUseMap() } returns returnUseMap
        assertEquals(returnUseMap, useCase.getBicycleReturnUseMap())
    }
}
