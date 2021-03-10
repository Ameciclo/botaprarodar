package app.igormatos.botaprarodar.domain.usecase.bikeForm

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.utils.bike
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
                repository.addNewBike(any())
            } returns bikeSimpleSuccess
            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Success(mockImageUploadResponse)
            val responseResult =
                userCaseForm.addNewBike(buildBicycle()) as SimpleResult.Success

            assertEquals("New Bicycle", responseResult.data.name)
        }
    }

    @Test
    fun `should return simple result with exception`() {
        runBlocking {
            val exceptionResult = Exception()
            coEvery {
                repository.addNewBike(any())
            } throws exceptionResult

            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Error(exceptionResult)
            val responseResult = userCaseForm.addNewBike(buildBicycle())

            assertTrue(responseResult is SimpleResult.Error)
            assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
        }
    }

    @Test
    fun `should edit bicycle with different image and return simple result with string`() {
        runBlocking {
            coEvery {
                repository.updateBike(any())
            } returns bikeSimpleSuccessEdit
            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Success(mockImageUploadResponse)
            val responseResult =
                userCaseForm.startUpdateBike(buildBicycle()) as SimpleResult.Success

            assertEquals("Bicycle Edited", responseResult.data.name)
        }
    }

    @Test
    fun `should edit bicycle with and return simple result with string`() {
        runBlocking {
            coEvery {
                repository.updateBike(any())
            } returns bikeSimpleSuccessEdit
            val bike = buildBicycle().apply {
                path = "https://bla.com"
            }
            val responseResult =
                userCaseForm.startUpdateBike(bike) as SimpleResult.Success

            assertEquals("Bicycle Edited", responseResult.data.name)
        }
    }

    @Test
    fun `should edit bicycle with different image and return simple result with exception`() {
        runBlocking {
            val exceptionResult = Exception()
            coEvery {
                repository.updateBike(any())
            } throws exceptionResult

            coEvery {
                firebaseRepository.uploadImageAndThumb(any(), any())
            } returns SimpleResult.Error(exceptionResult)
            val responseResult = userCaseForm.startUpdateBike(buildBicycle())

            assertTrue(responseResult is SimpleResult.Error)
            assertEquals(exceptionResult, (responseResult as SimpleResult.Error).exception)
        }
    }

    @Test
    fun `should edit bicycle and return simple result with exception`() {
        runBlocking {
            coEvery {
                repository.updateBike(any())
            } returns bikeSimpleError

            val bike = buildBicycle().apply {
                path = "https://bla.com"
            }
            val responseResult = userCaseForm.startUpdateBike(bike)

            assertTrue(responseResult is SimpleResult.Error)
        }
    }

    private fun buildBicycle(): Bike {
        return Bike().apply {
            name = "Bicycle"
            orderNumber = 123
            serialNumber = "123serial"
            photoPath = "https://bla.com"
            photoThumbnailPath = "https://bla.com"
        }
    }

    private val mockImageUploadResponse = ImageUploadResponse(
        fullImagePath = "teste",
        thumbPath = "teste"
    )
}