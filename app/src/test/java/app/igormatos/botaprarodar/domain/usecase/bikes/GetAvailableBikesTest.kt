package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikewithdraw.GetAvailableBikesException
import app.igormatos.botaprarodar.utils.availableBikes
import app.igormatos.botaprarodar.utils.bikeList
import app.igormatos.botaprarodar.utils.borrowedBikes
import app.igormatos.botaprarodar.utils.communityFixture
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class GetAvailableBikesTest {
    private val bikeRepository = mockk<BikeRepository>()
    private val getAvailableBikes = GetAvailableBikes(bikeRepository)

    @Test
    fun `given a bikeList then result should contain only available bikes`() = runBlocking {
        val mockResult = SimpleResult.Success(bikeList)
        coEvery { bikeRepository.getBicycles() } returns mockResult

        val result = getAvailableBikes.execute(communityFixture.id)

        assertEquals(listOf(availableBikes[0]), result)
    }

    @Test
    fun `given a community then result should contain only available bikes in this community`(): Unit =
        runBlocking {
            val mockResult = SimpleResult.Success(bikeList)
            coEvery { bikeRepository.getBicycles() } returns mockResult

            val result = getAvailableBikes.execute(communityFixture.id)

            result.onEach {
                assertEquals(communityFixture.id, it.communityId)
            }
        }

    @Test
    fun `given borrowed bike list then result should return empty list`() = runBlocking {
        val mockResult = SimpleResult.Success(borrowedBikes)

        coEvery { bikeRepository.getBicycles() } returns mockResult

        val result = getAvailableBikes.execute(communityFixture.id)

        assertEquals(emptyList<Bike>(), result)
    }

    @Test(expected = GetAvailableBikesException::class)
    fun `given an error then result should return throw expected exception`(): Unit = runBlocking {
        coEvery { bikeRepository.getBicycles() } returns SimpleResult.Error(Exception())

        getAvailableBikes.execute(communityFixture.id)
    }
}
