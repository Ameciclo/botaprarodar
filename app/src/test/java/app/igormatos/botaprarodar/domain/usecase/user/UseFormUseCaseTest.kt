package app.igormatos.botaprarodar.domain.usecase.user

import app.igormatos.botaprarodar.data.model.ImageUploadResponse
import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.domain.converter.user.UserRequestConvert
import app.igormatos.botaprarodar.domain.model.User
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UseFormUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val firebaseHelperRepository = mockk<FirebaseHelperRepository>()
    private val userConverter = mockk<UserRequestConvert>()
    private lateinit var userUseCase: UserUseCase

    @Before
    fun setup() {
        userUseCase = UserUseCase(userRepository, firebaseHelperRepository, userConverter)
        coEvery {
            userConverter.convert(any())
        } returns userRequest
    }

    @Test
    fun `when 'addUser' should create new user and return simple result with string`() =
        runBlocking {
            mockTestSuccess()

            val responseResult =
                userUseCase.addUser("100", mockUser) as SimpleResult.Success

            assertEquals("User registered", responseResult.data)
        }


    @Test
    fun `when 'addUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockTestException(exceptionResult)

            val responseResult = userUseCase.addUser("100", mockUser)

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
                userUseCase.updateUser("100", mockUser) as SimpleResult.Success

            assertEquals("User registered", responseResult.data)
        }

    @Test
    fun `when 'updateUser' should return simple result with exception`() =
        runBlocking {
            val exceptionResult = Exception("")
            mockUpdateTestException(exceptionResult)

            val responseResult = userUseCase.updateUser("100", mockUser)

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

    private val mockUser =
        User().apply {
            name = ""
            address = ""
            birthday = ""
            created_date = ""
            doc_number = 0
            doc_picture = ""
            doc_picture_back = ""
            doc_type = 0
            gender = 0
            profile_picture = ""
            profile_picture_thumbnail = ""
            residence_proof_picture = ""
        }

    private val mockImageUploadResponse = ImageUploadResponse(
        fullImagePath = "teste",
        thumbPath = "teste"
    )

    private val userRequest = UserRequest(
        name = "mock",
        createdDate = "",
        id = "",
        address = "",
        docNumber = 0,
        docPicture = "",
        docPictureBack = "",
        docType = 0,
        gender = 0,
        path = "",
        profilePicture = "",
        profilePictureThumbnail = "",
        residenceProofPicture = ""
    )
}