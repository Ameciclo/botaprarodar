package app.igormatos.botaprarodar.presentation.login.registration

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
internal class RegisterUseCaseTest {

    private lateinit var useCase: RegisterUseCase
    private lateinit var adminRepository: AdminRepository
    private lateinit var emailValidator: Validator<String?>
    private lateinit var passwordValidator: Validator<String?>

    @BeforeEach
    fun setup() {
        adminRepository = mockk()
        emailValidator = mockk()
        passwordValidator = mockk()
        useCase = RegisterUseCase(adminRepository, emailValidator, passwordValidator)
    }

    @Test
    fun `should return false when isRegisterFormValid execute with invalid email`() {
        // arrange
        val expectedResult = false
        val invalidEmail = loginRequestWithInvalidEmail.email

        every {
            emailValidator.validate(invalidEmail)
        } returns false

        // action
        val result: Boolean = useCase.isRegisterFormValid(
            email = invalidEmail,
            password = "",
            confirmPassword = ""
        )

        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should return false when isRegisterFormValid execute with invalid password`() {
        // arrange
        val expectedResult = false
        val invalidPassword = loginRequestWithInvalidPassword.email

        every {
            emailValidator.validate(any())
        } returns true

        every {
            passwordValidator.validate(invalidPassword)
        } returns false

        // action
        val result: Boolean = useCase.isRegisterFormValid(
            email = "",
            password = invalidPassword,
            confirmPassword = ""
        )

        // assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `should return RegisterState with NETWORK error when register execute without network connection`() {

        runBlocking {
            // arrange
            val expectedTypeError = BprErrorType.NETWORK
            val email: String = loginRequestValid.email
            val password: String = loginRequestValid.password

            coEvery {
                adminRepository.createAdmin(
                    email = email,
                    password = password
                )
            } throws UserAdminErrorException.AdminNetwork

            // action
            val result: RegisterState = useCase.register(
                email = email,
                password = password
            )

            // assert
            assertTrue(result is RegisterState.Error)
            val convertedErrorResult: RegisterState.Error = result as RegisterState.Error
            assertEquals(expectedTypeError, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return RegisterState with INVALID_ACCOUNT error when register execute with email already exists`() {

        runBlocking {
            // arrange
            val expectedTypeError = BprErrorType.INVALID_ACCOUNT
            val existingEmail: String = loginRequestValid.email
            val password: String = loginRequestValid.password

            coEvery {
                adminRepository.createAdmin(
                    email = existingEmail,
                    password = password
                )
            } throws UserAdminErrorException.AdminAccountAlreadyExists

            // action
            val result: RegisterState = useCase.register(
                email = existingEmail,
                password = password
            )

            // assert
            assertTrue(result is RegisterState.Error)
            val convertedErrorResult: RegisterState.Error = result as RegisterState.Error
            assertEquals(expectedTypeError, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return RegisterState with UNKNOWN error when register execute with unmapped exception`() {

        runBlocking {
            // arrange
            val expectedTypeError = BprErrorType.UNKNOWN
            val email: String = loginRequestValid.email
            val password: String = loginRequestValid.password

            coEvery {
                adminRepository.createAdmin(
                    email = email,
                    password = password
                )
            } throws Exception()

            // action
            val result: RegisterState = useCase.register(
                email = email,
                password = password
            )

            // assert
            assertTrue(result is RegisterState.Error)
            val convertedErrorResult: RegisterState.Error = result as RegisterState.Error
            assertEquals(expectedTypeError, convertedErrorResult.type)
        }
    }

    @Test
    fun `should return RegisterState with Success when register execute with success`() {

        runBlocking {
            // arrange
            val expectedResultState = RegisterState.Success
            val email: String = loginRequestValid.email
            val password: String = loginRequestValid.password

            coEvery {
                adminRepository.createAdmin(
                    email = email,
                    password = password
                )
            } returns Admin(email, password, "1")

            // action
            val result: RegisterState = useCase.register(
                email = email,
                password = password
            )

            // assert
            assertEquals(expectedResultState, result)
        }
    }

}