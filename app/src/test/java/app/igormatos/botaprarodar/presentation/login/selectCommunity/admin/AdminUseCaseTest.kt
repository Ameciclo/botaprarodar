package app.igormatos.botaprarodar.presentation.login.selectCommunity.admin

import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AdminUseCaseTest {

    private lateinit var adminUseCase: AdminUseCase
    private lateinit var adminRepository: AdminRepository

    @BeforeEach
    fun setup() {
        adminRepository = mockk()
        adminUseCase = AdminUseCase(adminRepository)
    }

    @Test
    fun `should return AdminState IsAdmin when repository returns Admin object`() {
        runBlocking {
            // arrange
            val id = "1"
            val expectedResultState = AdminState.IsAdmin

            coEvery {
                adminRepository.getAdminById(id)
            } returns Admin("", "", "")

            // action
            val resultState: AdminState = adminUseCase.isAdmin(id)

            // assert
            assertEquals(expectedResultState, resultState)
        }
    }

    @Test
    fun `should return AdminState NotIsAdmin when repository returns null`() {
        runBlocking {
            // arrange
            val id = "1"
            val expectedResultState = AdminState.NotIsAdmin

            coEvery {
                adminRepository.getAdminById(id)
            } returns null

            // action
            val resultState: AdminState = adminUseCase.isAdmin(id)

            // assert
            assertEquals(expectedResultState, resultState)
        }
    }

    @Test
    fun `should return AdminState with NETWORK error when repository execute without network connection`() {
        runBlocking {
            // arrange
            val id = "1"
            val expectedErrorType = BprErrorType.NETWORK

            coEvery {
                adminRepository.getAdminById(id)
            } throws UserAdminErrorException.AdminNetwork

            // action
            val resultState: AdminState = adminUseCase.isAdmin(id)

            // assert
            assertTrue(resultState is AdminState.Error)
            val resultStateError = resultState as AdminState.Error
            assertEquals(expectedErrorType, resultStateError.type)
        }
    }

    @Test
    fun `should return AdminState with UNKNOWN error when repository execute with unmapped Exception`() {
        runBlocking {
            // arrange
            val id = "1"
            val expectedErrorType = BprErrorType.UNKNOWN

            coEvery {
                adminRepository.getAdminById(id)
            } throws Exception()

            // action
            val resultState: AdminState = adminUseCase.isAdmin(id)

            // assert
            assertTrue(resultState is AdminState.Error)
            val resultStateError = resultState as AdminState.Error
            assertEquals(expectedErrorType, resultStateError.type)
        }
    }

}