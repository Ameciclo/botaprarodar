package app.igormatos.botaprarodar.domain.usecase.userForm

import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.utils.mockImageUploadResponse
import app.igormatos.botaprarodar.utils.userFake
import app.igormatos.botaprarodar.utils.userRequest
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserFormUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val firebaseHelperRepository = mockk<FirebaseHelperRepository>()
    private val userConverter = mockk<UserRequestConvert>()
    private lateinit var userUseCase: UserFormUseCase

    @Before
    fun setup() {
        userUseCase = UserFormUseCase(userRepository, firebaseHelperRepository, userConverter)
        coEvery {
            userConverter.convert(any())
        } returns userRequest
    }

    @Test
    fun `when 'addUser' should create new user and return simple result with string`() =
        runBlocking {
            mockTestSuccess()

            val responseResult =
                userUseCase.addUser("100", userFake) as SimpleResult.Success

            assertEquals("User registered", responseResult.data)
        }


    @Test
    fun `when 'addUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockTestException(exceptionResult)

            val responseResult = userUseCase.addUser("100", userFake)

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
                userUseCase.updateUser("100", userFake) as SimpleResult.Success

            assertEquals("User edited", responseResult.data)
        }

    @Test
    fun `when 'updateUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockUpdateTestException(exceptionResult)

            val responseResult = userUseCase.updateUser("100", userFake)

            assertTrue(responseResult is SimpleResult.Error)
            assertThat(
                (responseResult as SimpleResult.Error).exception,
                instanceOf(Exception::class.java)
            )
        }

    private fun mockTestSuccess() {
        coEvery {
            userRepository.addNewUser(any(), any())
        } returns "User registered"
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
    }

    private fun mockUpdateTestSuccess() {
        coEvery {
            userRepository.updateUser(any(), any())
        } returns "User edited"
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Success(mockImageUploadResponse)
    }

    private fun mockTestException(exceptionResult: Exception) {
        coEvery {
            userRepository.addNewUser(any(), any())
        } throws exceptionResult
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Error(exceptionResult)
    }

    private fun mockUpdateTestException(exceptionResult: Exception) {
        coEvery {
            userRepository.updateUser(any(), any())
        } throws exceptionResult
        coEvery {
            firebaseHelperRepository.uploadImageAndThumb(any(), any())
        } returns SimpleResult.Error(exceptionResult)
        coEvery {
            firebaseHelperRepository.uploadOnlyImage(any(), any())
        } returns SimpleResult.Error(exceptionResult)
    }
}