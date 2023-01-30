package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.domain.usecase.signin.LoginUseCase
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import app.igormatos.botaprarodar.utils.loginRequestValid
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LoginUseCaseTest {

    @RelaxedMockK
    private lateinit var adminRepository: AdminRepository
    private lateinit var useCase: LoginUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        useCase = LoginUseCase(adminRepository)
    }

    @Test
    fun `should return NETWORK error when has no network connection`() = runBlocking {
        // arrange
        val expectedErrorResult = BprErrorType.NETWORK
        val email = loginRequestValid.email
        val password = loginRequestValid.password

        coEvery {
            adminRepository.authenticateAdmin(email,
                password)
        } throws UserAdminErrorException.AdminNetwork

        // action
        val result = useCase.invoke(email, password)

        // assert
        assertTrue(result is BprResult.Failure)
        val convertedResultError = result as BprResult.Failure
        assertEquals(expectedErrorResult, convertedResultError.error)
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
            val result = useCase.invoke(email, password)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedResultError = result as BprResult.Failure
            assertEquals(expectedErrorResult, convertedResultError.error)
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
            val result = useCase.invoke(email, password)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedResultError = result as BprResult.Failure
            assertEquals(expectedErrorResult, convertedResultError.error)
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
            val result = useCase.invoke(email, password)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedResultError = result as BprResult.Failure
            assertEquals(expectedErrorResult, convertedResultError.error)
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
            val result = useCase.invoke(email, password)

            // assert
            assertTrue(result is BprResult.Success)
            val convertedResultSuccess = result as BprResult.Success
            assertEquals(expectedAdminResult, convertedResultSuccess.data)
        }
    }
}