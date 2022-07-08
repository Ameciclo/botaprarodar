package app.igormatos.botaprarodar.presentation.login

import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailState
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import app.igormatos.botaprarodar.utils.*
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var resendEmailUseCase: ResendEmailUseCase

    private val observerLoginStateMock = mockk<Observer<LoginState>>(relaxed = true)
    private val observerResendEmailStateMock = mockk<Observer<ResendEmailState>>(relaxed = true)
    private val observerButtonLoginEnableMock = mockk<Observer<Boolean>>(relaxed = true)

    @BeforeEach
    fun setup() {
        loginUseCase = mockk()
        resendEmailUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase, resendEmailUseCase)
    }


    @Test
    fun `should change isButtonLoginEnable to false when email is invalid`() {
        // arrange
        val expectedResult = false
        val invalidEmail = loginRequestWithInvalidEmail.email
        every {
            loginUseCase.isLoginFormValid(
                email = invalidEmail,
                password = any()
            )
        } returns false
        viewModel.isButtonLoginEnable.observeForever(observerButtonLoginEnableMock)

        // action
        viewModel.email.value = invalidEmail
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonLoginEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonLoginEnable to false when password is invalid`() {
        // arrange
        val expectedResult = false
        val invalidPassword = loginRequestWithInvalidPassword.password
        every {
            loginUseCase.isLoginFormValid(
                email = any(),
                password = invalidPassword
            )
        } returns false
        viewModel.isButtonLoginEnable.observeForever(observerButtonLoginEnableMock)

        // action
        viewModel.password.value = invalidPassword
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonLoginEnableMock.onChanged(expectedResult)
        }
    }


    @Test
    fun `should change isButtonLoginEnable to true when email and password are valid`() {
        // arrange
        val expectedResult = true

        every {
            loginUseCase.isLoginFormValid(any(), any())
        } returns true
        viewModel.isButtonLoginEnable.observeForever(observerButtonLoginEnableMock)

        // action
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonLoginEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonLoginEnable to true when valid password and email has trailing spaces`() {
        every {
            loginUseCase.isLoginFormValid(loginRequestValid.email, any())
        } returns true

        viewModel.isButtonLoginEnable.observeForever(observerButtonLoginEnableMock)
        viewModel.email.value = loginEmailTrailingSpacesRequestValid.email
        viewModel.password.value = loginEmailTrailingSpacesRequestValid.password

        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        verify {
            observerButtonLoginEnableMock.onChanged(true)
        }
    }

    @Test
    fun `should change LoginState to NETWORK error when execute login without network connection`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectedResult = LoginState.Error(BprErrorType.NETWORK)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change LoginState to INVALID_ACCOUNT error when execute login with non-existent email account`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectedResult = LoginState.Error(BprErrorType.INVALID_ACCOUNT)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change LoginState to INVALID_PASSWORD error when execute login with existent email account and wrong password`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectedResult = LoginState.Error(BprErrorType.INVALID_PASSWORD)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change LoginState to EMAIL_NOT_VERIFIED error when execute login with existent email account and corret password, but unverified account`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectedResult = LoginState.Error(BprErrorType.EMAIL_NOT_VERIFIED)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change LoginState to UNKNOWN error when execute login with unmapped exception`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val expectedResult = LoginState.Error(BprErrorType.UNKNOWN)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change LoginState to Success when execute login with success`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val admin = Admin(email, password, "1")
        val expectedResult = LoginState.Success(admin)

        coEvery {
            loginUseCase.authenticateAdmin(email, password)
        } returns expectedResult

        viewModel.loginState.observeForever(observerLoginStateMock)

        // action
        viewModel.login(email, password)

        // assert
        verifyOrder {
            observerLoginStateMock.onChanged(LoginState.Loading)
            observerLoginStateMock.onChanged(expectedResult)
        }
    }


    @Test
    fun `When login, should trim email parameter`() {
        val email = loginEmailTrailingSpacesRequestValid.email
        val password = loginEmailTrailingSpacesRequestValid.password
        val admin = Admin(email, password, "123")
        val successStateResult = LoginState.Success(admin)

        coEvery {
            loginUseCase.authenticateAdmin(loginRequestValid.email, password)
        } returns successStateResult

        viewModel.login(email, password)

        coVerify { loginUseCase.authenticateAdmin(loginRequestValid.email, password) }
    }

    @Test
    fun `should change ResendEmailState to NETWORK error when execute sendEmailVerification without network connection`() {
        // arrange
        val expectedResult = ResendEmailState.Error(BprErrorType.NETWORK)
        coEvery {
            resendEmailUseCase.execute()
        } returns expectedResult

        viewModel.resendEmailState.observeForever(observerResendEmailStateMock)

        // action
        viewModel.sendEmailVerification()

        // assert
        verifyOrder {
            observerResendEmailStateMock.onChanged(ResendEmailState.Loading)
            observerResendEmailStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change ResendEmailState to UNKNOWN error when execute sendEmailVerification with unmapped expcetion`() {
        // arrange
        val expectedResult = ResendEmailState.Error(BprErrorType.UNKNOWN)
        coEvery {
            resendEmailUseCase.execute()
        } returns expectedResult

        viewModel.resendEmailState.observeForever(observerResendEmailStateMock)

        // action
        viewModel.sendEmailVerification()

        // assert
        verifyOrder {
            observerResendEmailStateMock.onChanged(ResendEmailState.Loading)
            observerResendEmailStateMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change ResendEmailState to Success when execute sendEmailVerification with success`() {
        // arrange
        val expectedResult = ResendEmailState.Success
        coEvery {
            resendEmailUseCase.execute()
        } returns expectedResult

        viewModel.resendEmailState.observeForever(observerResendEmailStateMock)

        // action
        viewModel.sendEmailVerification()

        // assert
        verifyOrder {
            observerResendEmailStateMock.onChanged(ResendEmailState.Loading)
            observerResendEmailStateMock.onChanged(expectedResult)
        }
    }

    private fun invokePrivateMethod(name: String) {
        viewModel.javaClass.getDeclaredMethod(name).apply {
            isAccessible = true
            invoke(viewModel)
        }
    }

    companion object {
        private const val VALIDATE_FORM_METHOD = "validateLoginForm"
    }
}