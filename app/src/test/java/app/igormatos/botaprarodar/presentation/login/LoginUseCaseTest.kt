package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.Validator
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import app.igormatos.botaprarodar.utils.loginRequestValid
import app.igormatos.botaprarodar.utils.loginRequestWithInvalidEmail
import app.igormatos.botaprarodar.utils.loginRequestWithInvalidPassword
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private lateinit var adminRepository: AdminRepository
    private lateinit var emailValidator: Validator<String?>
    private lateinit var passwordValidator: Validator<String?>

    @BeforeEach
    fun setup() {
        adminRepository = mockk()
        emailValidator = mockk()
        passwordValidator = mockk()
        useCase = LoginUseCase(adminRepository, emailValidator, passwordValidator)
    }

    @Test
    fun `should return LoginState with NETWORK error when usecase authenticateAdmin without network connection`() {
        runBlocking {
            // arrange
            val expectedErrorResult = BprErrorType.NETWORK
            val email = loginRequestValid.email
            val password = loginRequestValid.password

            coEvery {
                adminRepository.authenticateAdmin(email, password)
            } throws UserAdminErrorException.AdminNetwork

            // action
            val result: LoginState = useCase.authenticateAdmin(email, password)

            // assert
            assertTrue(result is LoginState.Error)
            val convertedResultError = result as LoginState.Error
            assertEquals(expectedErrorResult, convertedResultError.type)
        }
    }

    @Test
    fun `should return LoginState with INVALID_ACCOUNT error when usecase authenticateAdmin with non-existent email account`() {
        runBlocking {
            // arrange
            val expectedErrorResult = BprErrorType.INVALID_ACCOUNT
            val email = loginRequestValid.email
            val password = loginRequestValid.password

            coEvery {
                adminRepository.authenticateAdmin(email, password)
            } throws UserAdminErrorException.AdminAccountNotFound

            // action
            val result: LoginState = useCase.authenticateAdmin(email, password)

            // assert
            assertTrue(result is LoginState.Error)
            val convertedResultError = result as LoginState.Error
            assertEquals(expectedErrorResult, convertedResultError.type)
        }
    }

    @Test
    fun `should return LoginState with INVALID_PASSWORD error when usecase authenticateAdmin with existing email account and wrong password`() {
        runBlocking {
            // arrange
            val expectedErrorResult = BprErrorType.INVALID_PASSWORD
            val email = loginRequestValid.email
            val password = loginRequestValid.password

            coEvery {
                adminRepository.authenticateAdmin(email, password)
            } throws UserAdminErrorException.AdminPasswordInvalid

            // action
            val result: LoginState = useCase.authenticateAdmin(email, password)

            // assert
            assertTrue(result is LoginState.Error)
            val convertedResultError = result as LoginState.Error
            assertEquals(expectedErrorResult, convertedResultError.type)
        }
    }

    @Test
    fun `should return LoginState with UNKNOWN error when usecase authenticateAdmin with unmapped exception`() {
        runBlocking {
            // arrange
            val expectedErrorResult = BprErrorType.UNKNOWN
            val email = loginRequestValid.email
            val password = loginRequestValid.password

            coEvery {
                adminRepository.authenticateAdmin(email, password)
            } throws Exception()

            // action
            val result: LoginState = useCase.authenticateAdmin(email, password)

            // assert
            assertTrue(result is LoginState.Error)
            val convertedResultError = result as LoginState.Error
            assertEquals(expectedErrorResult, convertedResultError.type)
        }
    }

    @Test
    fun `should return LoginState with Success error when usecase authenticateAdmin with success`() {
        runBlocking {
            // arrange
            val email = loginRequestValid.email
            val password = loginRequestValid.password
            val expectedAdminResult = Admin(password, email, "1")

            coEvery {
                adminRepository.authenticateAdmin(email, password)
            } returns Admin(password, email, "1")

            // action
            val result: LoginState = useCase.authenticateAdmin(email, password)

            // assert
            assertTrue(result is LoginState.Success)
            val convertedResultSuccess = result as LoginState.Success
            assertEquals(expectedAdminResult, convertedResultSuccess.admin)
        }
    }

    @Test
    fun `should return false when usecase isLoginFormValid with invalid email`() {
        // arrange
        val email = loginRequestWithInvalidEmail.email
        val password = loginRequestWithInvalidEmail.password
        val expectResponse = false

        every {
            emailValidator.validate(email)
        } returns false

        // action
        val result: Boolean = useCase.isLoginFormValid(email, password)

        // assert
        assertEquals(expectResponse, result)
    }

    @Test
    fun `should return false when usecase isLoginFormValid with valid email and invalid password`() {
        // arrange
        val email = loginRequestWithInvalidPassword.email
        val password = loginRequestWithInvalidEmail.password
        val expectResponse = false

        every {
            emailValidator.validate(email)
        } returns true

        every {
            passwordValidator.validate(password)
        } returns false

        // action
        val result: Boolean = useCase.isLoginFormValid(email, password)

        // assert
        assertEquals(expectResponse, result)
    }

    @Test
    fun `should return true when usecase isLoginFormValid with valid email and valid password`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectResponse = true

        every {
            emailValidator.validate(email)
        } returns true

        every {
            passwordValidator.validate(password)
        } returns true

        // action
        val result: Boolean = useCase.isLoginFormValid(email, password)

        // assert
        assertEquals(expectResponse, result)
    }
}