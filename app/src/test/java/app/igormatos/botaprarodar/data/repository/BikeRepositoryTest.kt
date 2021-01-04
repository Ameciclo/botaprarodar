package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
@DisplayName("Given BicycleRepository")
internal class BikeRepositoryTest {
    @InjectMockKs
    private lateinit var repository: BikeRepository
    @MockK
    private lateinit var api: BicycleApi

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Nested
    @DisplayName("WHEN request for get Bicycles")
    inner class GetBicycles {

        @Test
        fun `should return all bicycles of community`() = runBlocking {
            coEvery { api.getBicycles(any()).await() } returns createBicycleResponse()

            val result = repository.getBicycles("1000")

            assertNotNull(result)
            assertTrue(result.containsKey("123"))
            assertTrue(result.containsKey("456"))
            assertTrue(result.containsKey("789"))
            assertTrue(result.containsKey("098"))
            assertTrue(result.containsKey("876"))
        }

    }

    @Nested
    @DisplayName("WHEN add new bicycle")
    inner class AddNewBike {

        @Test
        fun `should add new bicycle`() = runBlocking {
            coEvery { api.addNewBicycle(any(), any()) } returns AddDataResponse("New Bicycle")

            val result = repository.addNewBike(
                "1000",
                BicycleRequest(name = "New Bicycle",
                    orderNumber = 1010,
                    serialNumber = "New Serial",
                    createdDate = Date().toString()
                ))

            assertTrue(result.isNotBlank())
            assertEquals("New Bicycle", result)
        }

    }

    fun createBicycleResponse(): Map<String, Bike> {
        return mapOf(Pair("123", Bike()),
            Pair("456", Bike()),
            Pair("789", Bike()),
            Pair("098", Bike()),
            Pair("876", Bike()))
    }
}