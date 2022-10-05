package app.igormatos.botaprarodar.domain.usecase.userForm

import androidx.test.platform.app.InstrumentationRegistry
import app.igormatos.botaprarodar.common.enumType.UserMotivationType
import app.igormatos.botaprarodar.data.repository.FirebaseHelperRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.InternalPlatformDsl.toArray
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class UserFormUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private val firebaseHelperRepository = mockk<FirebaseHelperRepository>()
    private lateinit var userUseCase: UserFormUseCase

    @Before
    fun setup() {
        initUserCase()
    }

    private fun initUserCase() {
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
    fun `when 'addUser' without images, should create new user and return simple result with string`() =
        runBlocking {
            mockTestWithoutImagesSuccess()

            val responseResult =
                userUseCase.addUser(validUserWithNoImages) as SimpleResult.Success

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
    fun `when 'updateUser' without images, should update the user and return simple result with string`() =
        runBlocking {
            mockUpdateTestWithoutImagesSuccess()

            val responseResult =
                userUseCase.startUpdateUser(validUserWithNoImages) as SimpleResult.Success

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

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `WHEN user motivation index is informed THEN should return a value not empty in UserMotivationType `(
        index: Int
    ) {
        initUserCase()
        val userMotivation = userUseCase.getUserMotivationValue(index)
        assertNotEquals("", userMotivation)
    }

    @ParameterizedTest
    @ValueSource(strings = ["Outro",
        "Para economizar dinheiro, usar bicicleta é mais barato.",
        "Porque é mais ecológico. A bicicleta não polui o ambiente.",
        "Para economizar tempo. Usar a bicicleta como transporte é mais eficiente.",
        "Porque começou a trabalhar com entregas.",
        "Para melhorar a saúde física e emocional."])
    fun `WHEN user motivation value is informed THEN should return a value not empty in UserMotivationType `(
        value: String
    ) {
        initUserCase()
        val userMotivation = userUseCase.getUserMotivationIndex(value)
        assertNotNull(userMotivation)
    }

    private fun motivationList(): Array<String> {
       return arrayOf(

        )
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

    private fun mockTestWithoutImagesSuccess() {
        coEvery {
            userRepository.addNewUser(any())
        } returns userSimpleSuccess
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

    private fun mockUpdateTestWithoutImagesSuccess() {
        coEvery {
            userRepository.updateUser(any())
        } returns userSimpleSuccessEdit
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
