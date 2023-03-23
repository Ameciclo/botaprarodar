package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException.AdminAccountNotFound
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException.AdminNetwork
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import app.igormatos.botaprarodar.utils.loginRequestValid
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK

@ExtendWith(InstantExecutorExtension::class)
internal class PasswordRecoveryUseCaseTest {

    @RelaxedMockK
    private lateinit var adminRepository: AdminRepository
    private lateinit var useCase: PasswordRecoveryUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        useCase = PasswordRecoveryUseCase(adminRepository)
    }

    @Test
    fun `should return PasswordRecoveryState with NETWORK error when sendPasswordResetEmail execute without network connection`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.NETWORK
            val email = loginRequestValid.email

            coEvery { adminRepository.sendPasswordResetEmail(email) } throws AdminNetwork

            // action
            val result = useCase.invoke(email)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedErrorResult = result as BprResult.Failure
            assertEquals(expectedErrorType, convertedErrorResult.error)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with INVALID_ACCOUNT error when sendPasswordResetEmail execute with non-existent email account`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.INVALID_ACCOUNT
            val email = loginRequestValid.email

            coEvery { adminRepository.sendPasswordResetEmail(email) } throws AdminAccountNotFound

            // action
            val result = useCase.invoke(email)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedErrorResult = result as BprResult.Failure
            assertEquals(expectedErrorType, convertedErrorResult.error)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with UNKNOWN error when sendPasswordResetEmail execute with unmapped exception`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.UNKNOWN
            val email = loginRequestValid.email

            coEvery { adminRepository.sendPasswordResetEmail(email) } throws Exception()

            // action
            val result = useCase.invoke(email)

            // assert
            assertTrue(result is BprResult.Failure)
            val convertedErrorResult = result as BprResult.Failure
            assertEquals(expectedErrorType, convertedErrorResult.error)
        }
    }

    @Test
    fun `should return PasswordRecoveryState with SUCCESS when sendPasswordResetEmail execute with success`() {
        runBlocking {
            // arrange
            val expectedState = BprResult.Success(Unit)
            val email = loginRequestValid.email

            coEvery { adminRepository.sendPasswordResetEmail(email) } returns true

            // action
            val result = useCase.invoke(email)

            // assert
            assertEquals(expectedState, result)
        }
    }
}