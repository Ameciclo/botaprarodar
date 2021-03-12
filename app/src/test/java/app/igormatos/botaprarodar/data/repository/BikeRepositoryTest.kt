package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.utils.addDataResponseBike
import app.igormatos.botaprarodar.utils.bicycleRequest
import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.mapOfBikes
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@DisplayName("Given BicycleRepository")
internal class BikeRepositoryTest {
    @InjectMockKs
    private lateinit var repository: BikeRepository

    @MockK
    private lateinit var api: BicycleApi

    @MockK
    private lateinit var firebaseDatabase: FirebaseDatabase

    @MockK
    private lateinit var iterator: Iterator<DataSnapshot>

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Nested
    @DisplayName("WHEN request for get Bicycles")
    inner class GetBicycles {

        @Test
        fun `should return all bicycles of community`() = runBlocking {
            coEvery { api.getBicycles().await() } returns mapOfBikes

            val response = repository.getBicycles()
            val result = (response as SimpleResult.Success<Map<String, Bike>>).data

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
            coEvery { api.addNewBike(any()) } returns addDataResponseBike

            val response = repository.addNewBike(bike)
            val result = response as SimpleResult.Success<AddDataResponse>

            assertEquals(SimpleResult.Success(addDataResponseBike), result)
            assertEquals("New Bicycle", result.data.name)
        }
    }
}