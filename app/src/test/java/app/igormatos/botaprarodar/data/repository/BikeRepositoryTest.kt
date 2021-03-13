package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.utils.*
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
            val result = (response as SimpleResult.Success<List<Bike>>).data

            assertEquals(mapOfBikes.size, result.size)
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