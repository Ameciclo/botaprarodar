package app.igormatos.botaprarodar.data.repository

import android.util.Log
import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import com.google.firebase.database.*
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.util.*
import javax.security.auth.callback.Callback

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
    private lateinit var databaseReference: DatabaseReference

    @MockK
    private lateinit var valueEventListener: ValueEventListener

    @MockK
    private lateinit var dataSnapshot: DataSnapshot

    @MockK
    private lateinit var iterable: Iterable<DataSnapshot>

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
            coEvery { api.addNewBike(any(), any()) } returns AddDataResponse("New Bicycle")

            val result = repository.addNewBike("1000", bicycleRequest)

            assertTrue(result.isNotBlank())
            assertEquals("New Bicycle", result)
        }
    }

    fun createBicycleResponse(): Map<String, Bike> {
        return mapOf(
            Pair("123", Bike()),
            Pair("456", Bike()),
            Pair("789", Bike()),
            Pair("098", Bike()),
            Pair("876", Bike())
        )
    }

    private val bicycleRequest = BicycleRequest(
        id = "",
        available = true,
        inUse = false,
        name = "New Bicycle",
        orderNumber = 1010,
        serialNumber = "New Serial",
        createdDate = Date().toString()
    )
}