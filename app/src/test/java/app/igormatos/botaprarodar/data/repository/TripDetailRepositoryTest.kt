package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import app.igormatos.botaprarodar.utils.bikeRequestWithMappers
import app.igormatos.botaprarodar.utils.exception
import com.brunotmgomes.ui.SimpleResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TripDetailRepositoryTest {

    @MockK
    private lateinit var api: BicycleApi

    private lateinit var repository: TripDetailRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should return bikeRequest when the call is success`() {
        runBlocking {
            // arrange
            val bikeId = "1"
            repository = TripDetailRepository(api)

            coEvery { api.getBikeById(bikeId) } returns bikeRequestWithMappers

            // action
            val response: SimpleResult<BikeRequest> = repository.getBikeById(bikeId)

            // assert
            assertEquals(response, SimpleResult.Success(bikeRequestWithMappers))
        }
    }

    @Test
    fun `should return exception when the call is error`() {
        runBlocking {
            // arrange
            val bikeId = "1"
            repository = TripDetailRepository(api)

            coEvery { api.getBikeById(bikeId) } throws exception

            // action
            val response = repository.getBikeById(bikeId)

            // assert
            assertEquals(response, SimpleResult.Error(exception))
        }
    }
}