package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import app.igormatos.botaprarodar.utils.loginRequestValid
import app.igormatos.botaprarodar.utils.loginRequestWithInvalidEmail
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import app.igormatos.botaprarodar.presentation.authentication.Validator

@ExtendWith(InstantExecutorExtension::class)
internal class PasswordRecoveryUseCaseTest {

    private lateinit var useCase: PasswordRecoveryUseCase
    private lateinit var adminRepository: AdminRepository
    private lateinit var emailValidator: Validator<String?>


    @BeforeEach
    fun setup() {
        adminRepository = mockk()
        emailValidator = mockk()
        useCase = PasswordRecoveryUseCase(adminRepository, emailValidator)
    }

    @Test
    fun `should return PasswordRecoveryState with NETWORK error when sendPasswordResetEmail execute without network connection`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.NETWORK
            val email: String = loginRequestValid.email

            coEvery {
                adminRepository.sendPasswordResetEmail(email)
            } throws UserAdminErrorException.AdminNetwork

            // action
            val result: PasswordRecoveryState = useCase.sendPasswordResetEmail(email)

            // assert
            assertTrue(result is PasswordRecoveryState.Error)
            val convertedErrorResult = result as PasswordRecoveryState.Error
            assertEquals(expectedErrorType, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with INVALID_ACCOUNT error when sendPasswordResetEmail execute with non-existent email account`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.INVALID_ACCOUNT
            val email: String = loginRequestValid.email

            coEvery {
                adminRepository.sendPasswordResetEmail(email)
            } throws UserAdminErrorException.AdminAccountNotFound

            // action
            val result: PasswordRecoveryState = useCase.sendPasswordResetEmail(email)

            // assert
            assertTrue(result is PasswordRecoveryState.Error)
            val convertedErrorResult = result as PasswordRecoveryState.Error
            assertEquals(expectedErrorType, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with UNKNOWN error when sendPasswordResetEmail execute with unmapped exception`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.UNKNOWN
            val email: String = loginRequestValid.email

            coEvery {
                adminRepository.sendPasswordResetEmail(email)
            } throws Exception()

            // action
            val result: PasswordRecoveryState = useCase.sendPasswordResetEmail(email)

            // assert
            assertTrue(result is PasswordRecoveryState.Error)
            val convertedErrorResult = result as PasswordRecoveryState.Error
            assertEquals(expectedErrorType, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with SUCCESS when sendPasswordResetEmail execute with success`() {
        runBlocking {
            // arrange
            val expectedState = PasswordRecoveryState.Success
            val email: String = loginRequestValid.email

            coEvery {
                adminRepository.sendPasswordResetEmail(email)
            } returns true

            // action
            val result: PasswordRecoveryState = useCase.sendPasswordResetEmail(email)

            // assert
            assertEquals(expectedState, result)
        }
    }

    @Test
    fun `should return true when validate execute with valid email`() {
        // arrange
        val expectedResult = true
        val email: String = loginRequestValid.email

        every {
            emailValidator.validate(email)
        } returns true

        // action
        val result: Boolean = useCase.isEmailValid(email)

        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should return false when validate execute with invalid email`() {
        // arrange
        val expectedResult = false
        val email: String = loginRequestWithInvalidEmail.email

        every {
            emailValidator.validate(email)
        } returns false

        // action
        val result: Boolean = useCase.isEmailValid(email)

        // assert
        assertEquals(expectedResult, result)
    }
}