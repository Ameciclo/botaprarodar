package app.igormatos.botaprarodar.presentation.login.resendEmail

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class ResendEmailUseCaseTest {

    private lateinit var useCase: ResendEmailUseCase
    private lateinit var adminRepository: AdminRepository


    @BeforeEach
    fun setup() {
        adminRepository = mockk()
        useCase = ResendEmailUseCase(adminRepository)
    }

    @Test
    fun `should return ResendEmailState with NETWORK error when usecase sendEmailVerification without network connection`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.NETWORK
            coEvery {
                adminRepository.sendEmailVerification()
            } throws UserAdminErrorException.AdminNetwork

            // action
            val response: ResendEmailState = useCase.execute()

            // assert
            assertTrue(response is ResendEmailState.Error)
            val convertedErrorResult = response as ResendEmailState.Error
            assertEquals(expectedErrorType, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return ResendEmailState with UNKNOWN error when usecase sendEmailVerification with unmapped exception`() {
        runBlocking {
            // arrange
            val expectedErrorType = BprErrorType.UNKNOWN
            coEvery {
                adminRepository.sendEmailVerification()
            } throws Exception()

            // action
            val response: ResendEmailState = useCase.execute()

            // assert
            assertTrue(response is ResendEmailState.Error)
            val convertedErrorResult = response as ResendEmailState.Error
            assertEquals(expectedErrorType, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return ResendEmailState with Success when usecase sendEmailVerification returns true`() {
        runBlocking {
            // arrange
            val expectedState = ResendEmailState.Success
            coEvery {
                adminRepository.sendEmailVerification()
            } returns true

            // action
            val response: ResendEmailState = useCase.execute()

            // assert
            assertEquals(expectedState, response)
        }
    }
}