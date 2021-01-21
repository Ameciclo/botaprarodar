package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.model.Bike
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AddNewBikeUseCaseTest {

    private lateinit var userCase: AddNewBikeUseCase
    private val repository = mockk<BikeRepository>()
    private val firebaseRepository = mockk<FirebaseHelperRepository>()

    @Before
    fun setUp() {
        userCase = AddNewBikeUseCase(repository, firebaseRepository)
    }

    @Test
    fun `should create new bicycle and return simple result with string`() {
        runBlocking {
            GlobalScope.launch {
                coEvery { repository.addNewBike(any(), any()) } returns "Created new bicycle"
                val responseResult =
                    userCase.addNewBike("100", buildBicycle()) as SimpleResult.Success

                assertEquals("Created new bicycle", responseResult.data)
            }
        }
    }


    @Test
    fun `should return simple result with exception`() {
        runBlocking {
            GlobalScope.launch {
                val exceptionResult = Exception()
                coEvery { repository.addNewBike(any(), any()) } throws exceptionResult

                val responseResult = userCase.addNewBike("100", buildBicycle())

                assertTrue(responseResult is SimpleResult.Error)
                assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
            }
        }
    }

    private fun buildBicycle(): Bike {
        return Bike().apply {
            name = "Bicycle"
            order_number = 123
            serial_number = "123serial"
            photo_path = "http://bla.com"
            photo_thumbnail_path = "http://bla.com"
        }
    }
}