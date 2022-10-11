package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.presentation.bikewithdraw.GetAvailableBikesException
import app.igormatos.botaprarodar.utils.borrowedBikes
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycleInUse
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycleRandom
import app.igormatos.botaprarodar.utils.communityFixture
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions
import kotlin.jvm.Throws

@ExperimentalCoroutinesApi
class GetAvailableBikesTest {
    private val bikeRepository = mockk<BikeRepository>()
    private val getAvailableBikes = GetAvailableBikes(bikeRepository)

    @Test
    fun `given a bikeList then result should contain only available bikes`() = runBlocking {
        coEvery { bikeRepository.getBikesByCommunityId(communityFixture.id) } returns SimpleResult.Success(
            buildMapStringAndBicycleRandom(3, 2)
        )

        val result = getAvailableBikes.execute(communityFixture.id)

        assertEquals(2, result.size)
    }

    @Test
    fun `given a community then result should contain only available bikes in this community`() =
        runBlocking {
            coEvery { bikeRepository.getBikesByCommunityId(communityFixture.id) } returns SimpleResult.Success(
                buildMapStringAndBicycleRandom(3, 2)
            )

            val result = getAvailableBikes.execute(communityFixture.id)

            assertEquals(2, result.size)
            assertEquals(communityFixture.id, result[0].communityId)
            assertEquals(communityFixture.id, result[1].communityId)
        }

    @Test
    fun `given borrowed bike list then result should return empty list`() = runBlocking {
        coEvery { bikeRepository.getBikesByCommunityId(communityFixture.id) } returns SimpleResult.Success(
            buildMapStringAndBicycleInUse(2)
        )

        val result = getAvailableBikes.execute(communityFixture.id)

        assertEquals(emptyList<Bike>(), result)
    }

    @Test
    fun `given an error then result should return throw expected exception`() {
        coEvery { bikeRepository.getBikesByCommunityId(communityFixture.id)} returns SimpleResult.Error(Exception())

        Assertions.assertThrows(GetAvailableBikesException::class.java) {
            runBlocking { getAvailableBikes.execute(communityFixture.id) }
        }
    }
}
