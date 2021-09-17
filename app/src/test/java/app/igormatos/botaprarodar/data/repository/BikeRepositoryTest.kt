package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.utils.addDataResponseBike
import app.igormatos.botaprarodar.utils.bikeRequest
import app.igormatos.botaprarodar.utils.mapOfBikesRequest
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.DataSnapshot
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
internal class BikeRepositoryTest {
    @InjectMockKs
    private lateinit var repository: BikeRepository

    @MockK
    private lateinit var api: BicycleApi

    @MockK
    private lateinit var iterator: Iterator<DataSnapshot>

    @Before
    fun setUp() {
        init(this)
    }

    @Test
    fun `should return all bicycles of community`() = runBlocking {
        coEvery { api.getBicycles().await() } returns mapOfBikesRequest

        val response = repository.getBicycles()
        val result = (response as SimpleResult.Success<Map<String, BikeRequest>>).data

        assertNotNull(result)
        assertTrue(result.containsKey("123"))
        assertTrue(result.containsKey("456"))
        assertTrue(result.containsKey("789"))
        assertTrue(result.containsKey("098"))
        assertTrue(result.containsKey("876"))
    }


    @Test
    fun `should add new bicycle`() = runBlocking {
        coEvery { api.addNewBike(any(), any()) } returns addDataResponseBike

        val response = repository.addNewBike(bikeRequest)
        val result = response as SimpleResult.Success<AddDataResponse>

        assertEquals(SimpleResult.Success(addDataResponseBike), result)
        assertEquals("New Bicycle", result.data.name)
    }


    @Test
    fun `should return bike with withdraw by user` () =
        runBlocking {
            coEvery { api.getBikeWithWithdrawByUserId(any()) } returns mapOfBikesRequest

            val bikeWithWithdrawByUser = repository.getBikeWithWithdrawByUser("testUserId")

            assertThat(SimpleResult.Success(mapOfBikesRequest), equalTo(bikeWithWithdrawByUser))
        }

    @Test
    fun `should return list of bikes from a community` () =
        runBlocking {
            coEvery { api.getBikesByCommunityId(any()) } returns mapOfBikesRequest

            val bikeWithWithdrawByUser = repository.getBikesByCommunityId("someCommunityId")

            assertThat(SimpleResult.Success(mapOfBikesRequest), equalTo(bikeWithWithdrawByUser))
        }
}