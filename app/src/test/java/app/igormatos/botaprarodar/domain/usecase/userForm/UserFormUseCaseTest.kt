package app.igormatos.botaprarodar.domain.usecase.userForm

import androidx.test.platform.app.InstrumentationRegistry
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.utils.mockImageUploadResponse
import app.igormatos.botaprarodar.utils.userSimpleSuccess
import app.igormatos.botaprarodar.utils.userSimpleSuccessEdit
import app.igormatos.botaprarodar.utils.validUser
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File

class UserFormUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val firebaseHelperRepository = mockk<FirebaseHelperRepository>()
    private lateinit var userUseCase: UserFormUseCase

    @Before
    fun setup() {
        userUseCase = UserFormUseCase(userRepository, firebaseHelperRepository)
    }

    @Test
    fun `when 'addUser' should create new user and return simple result with string`() =
        runBlocking {
            mockTestSuccess()

            val responseResult =
                userUseCase.addUser(validUser) as SimpleResult.Success

            assertEquals("User registered", responseResult.data.name)
        }


    @Test
    fun `when 'addUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockTestException(exceptionResult)

            val responseResult = userUseCase.addUser(validUser)

            assertTrue(responseResult is SimpleResult.Error)
            assertThat(
                (responseResult as SimpleResult.Error).exception,
                instanceOf(Exception::class.java)
            )
        }

    @Test
    fun `when 'updateUser' should update the user and return simple result with string`() =
        runBlocking {
            mockUpdateTestSuccess()

            val responseResult =
                userUseCase.startUpdateUser(validUser) as SimpleResult.Success

            assertEquals("User edited", responseResult.data.name)
        }

    @Test
    fun `when 'updateUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockUpdateTestException(exceptionResult)

            val responseResult = userUseCase.startUpdateUser(validUser)

            assertTrue(responseResult is SimpleResult.Error)
            assertThat(
                (responseResult as SimpleResult.Error).exception,
                instanceOf(Exception::class.java)
            )
        }

    @Test
    fun `when 'deleteImage' profilePicture from repository should return simple result without exception`() =
        runBlocking {
            val imagePath = validUser.profilePicture.orEmpty()
            coEvery { firebaseHelperRepository.deleteImageResource(imagePath) } returns SimpleResult.Success(
                Unit
            )

            val responseResult = userUseCase.deleteImageFromRepository(imagePath)

            assertTrue(responseResult is SimpleResult.Success<Unit>)
        }

    @Test
    fun `when 'deleteImage' from local storage should return simple result without exception`() =
        runBlocking {
            val file = File.createTempFile("temImage", ".jpg")
            file.createNewFile()

            val responseResult = userUseCase.deleteImageLocal(file.path)

            assertTrue(responseResult is SimpleResult.Success<Unit>)
        }

    private fun mockTestSuccess() {
        coEvery {
            userRepository.addNewUser(any())
        } returns userSimpleSuccess
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
    }

    private fun mockUpdateTestSuccess() {
        coEvery {
            userRepository.updateUser(any())
        } returns userSimpleSuccessEdit
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
    }

    private fun mockTestException(exceptionResult: Exception) {
        coEvery {
            userRepository.addNewUser(any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Error(exceptionResult)
    }

    private fun mockUpdateTestException(exceptionResult: Exception) {
        coEvery {
            userRepository.updateUser(any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Error(exceptionResult)
    }

    private fun getContext() = InstrumentationRegistry.getInstrumentation().context
}