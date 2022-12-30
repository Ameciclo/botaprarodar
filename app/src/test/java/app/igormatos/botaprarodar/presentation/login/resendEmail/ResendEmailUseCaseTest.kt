package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.login.signin.SignInResult
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
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
internal class ResendEmailUseCaseTest {

    @RelaxedMockK
    private lateinit var useCase: ResendEmailUseCase
    @RelaxedMockK
    private lateinit var adminRepository: AdminRepository

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        useCase = ResendEmailUseCase(adminRepository)
    }

    @Test
    fun `should return NETWORK error when has no network connection`() = runBlocking {
        // arrange
        val expectedErrorType = BprErrorType.NETWORK
        coEvery { adminRepository.sendEmailVerification() } throws UserAdminErrorException.AdminNetwork

        // action
        val response = useCase.invoke()

        // assert
        assertTrue(response is SignInResult.Failure)
        val convertedErrorResult = response as SignInResult.Failure
        assertEquals(expectedErrorType, convertedErrorResult.error)
    }

    @Test
    fun `should return UNKNOWN error when sendEmailVerification has an unexpected exception`() =
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.UNKNOWN
            coEvery { adminRepository.sendEmailVerification() } throws Exception()

            // action
            val response = useCase.invoke()

            // assert
            assertTrue(response is SignInResult.Failure)
            val convertedErrorResult = response as SignInResult.Failure
            assertEquals(expectedErrorType, convertedErrorResult.error)
        }

    @Test
    fun `should return success when sendEmailVerification returns true`() = runBlocking {
        // arrange
        val expectedState = SignInResult.Success(Unit)
        coEvery { adminRepository.sendEmailVerification() } returns true

        // action
        val response = useCase.invoke()

        // assert
        assertEquals(expectedState, response)
    }
}