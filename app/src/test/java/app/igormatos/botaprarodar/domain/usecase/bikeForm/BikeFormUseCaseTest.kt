package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.utils.bikeSimpleError
import app.igormatos.botaprarodar.utils.bikeSimpleSuccess
import app.igormatos.botaprarodar.utils.bikeSimpleSuccessEdit
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class BikeFormUseCaseTest {

    private val repository = mockk<BikeRepository>()
    private val firebaseRepository = mockk<FirebaseHelperRepository>()
    private lateinit var userCaseForm: BikeFormUseCase

    @BeforeEach
    fun setUp() {
        userCaseForm =
            BikeFormUseCase(
                repository,
                firebaseRepository
            )
    }

    @Test
    fun `should create new bicycle and return simple result with string`() {
        runBlocking {
            coEvery {
                repository.addNewBike(any(), any())
            } returns bikeSimpleSuccess
            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Success(mockImageUploadResponse)
            val responseResult =
                userCaseForm.addNewBike("100", buildBicycle()) as SimpleResult.Success

            assertEquals("New Bicycle", responseResult.data.name)
        }
    }

    @Test
    fun `should return simple result with exception`() {
        runBlocking {
            val exceptionResult = Exception()
            coEvery {
                repository.addNewBike(any(), any())
            } throws exceptionResult

            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Error(exceptionResult)
            val responseResult = userCaseForm.addNewBike("100", buildBicycle())

            assertTrue(responseResult is SimpleResult.Error)
            assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
        }
    }

    @Test
    fun `should edit bicycle with different image and return simple result with string`() {
        runBlocking {
            coEvery {
                repository.updateBike(any(), any())
            } returns bikeSimpleSuccessEdit
            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Success(mockImageUploadResponse)
            val responseResult =
                userCaseForm.startUpdateBike("100", buildBicycle()) as SimpleResult.Success

            assertEquals("Bicycle Edited", responseResult.data.name)
        }
    }

    @Test
    fun `should edit bicycle with and return simple result with string`() {
        runBlocking {
            coEvery {
                repository.updateBike(any(), any())
            } returns bikeSimpleSuccessEdit
            val bike = buildBicycle().apply {
                path = "https://bla.com"
            }
            val responseResult =
                userCaseForm.startUpdateBike("100", bike) as SimpleResult.Success

            assertEquals("Bicycle Edited", responseResult.data.name)
        }
    }

    @Test
    fun `should edit bicycle with different image and return simple result with exception`() {
        runBlocking {
            val exceptionResult = Exception()
            coEvery {
                repository.updateBike(any(), any())
            } throws exceptionResult

            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Error(exceptionResult)
            val responseResult = userCaseForm.startUpdateBike("100", buildBicycle())

            assertTrue(responseResult is SimpleResult.Error)
            assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
        }
    }

    @Test
    fun `should edit bicycle and return simple result with exception`() {
        runBlocking {
            coEvery {
                repository.updateBike(any(), any())
            } returns bikeSimpleError

            val bike = buildBicycle().apply {
                path = "https://bla.com"
            }
            val responseResult = userCaseForm.startUpdateBike("100", bike)

            assertTrue(responseResult is SimpleResult.Error)
        }
    }

    private fun buildBicycle(): Bike {
        return Bike().apply {
            name = "Bicycle"
            order_number = 123
            serial_number = "123serial"
            photo_path = "https://bla.com"
            photo_thumbnail_path = "https://bla.com"
        }
    }

    private val mockImageUploadResponse = ImageUploadResponse(
        fullImagePath = "teste",
        thumbPath = "teste"
    )
}