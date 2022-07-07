package app.igormatos.botaprarodar.presentation.login.registration

import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.utils.*
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class RegisterViewModelTest {

    private lateinit var useCase: RegisterUseCase
    private lateinit var viewModel: RegisterViewModel

    private val observerButtonRegisterEnableMock = mockk<Observer<Boolean>>(relaxed = true)
    private val observerRegisterStateMock =
        mockk<Observer<RegisterState>>(relaxed = true)

    @BeforeEach
    fun setup() {
        useCase = mockk()
        viewModel = RegisterViewModel(useCase)
    }

    @Test
    fun `should change isButtonEnable to false when email is invalid`() {
        // arrange
        val expectedResult = false
        val invalidEmail = loginRequestWithInvalidEmail.email
        every {
            useCase.isRegisterFormValid(
                email = invalidEmail,
                password = any(),
                confirmPassword = any()
            )
        } returns false
        viewModel.isButtonRegisterEnable.observeForever(observerButtonRegisterEnableMock)

        // action
        viewModel.email.value = invalidEmail
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonRegisterEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonEnable to false when password is invalid`() {
        // arrange
        val expectedResult = false
        val invalidPassword = loginRequestWithInvalidPassword.password
        every {
            useCase.isRegisterFormValid(
                email = any(),
                password = invalidPassword,
                confirmPassword = any()
            )
        } returns false
        viewModel.isButtonRegisterEnable.observeForever(observerButtonRegisterEnableMock)

        // action
        viewModel.password.value = invalidPassword
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonRegisterEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonEnable to false when confirm password is invalid`() {
        // arrange
        val expectedResult = false
        val invalidConfirmPassword = loginRequestWithInvalidConfirmPassword.confirmPassword
        every {
            useCase.isRegisterFormValid(
                email = any(),
                password = any(),
                confirmPassword = invalidConfirmPassword
            )
        } returns false
        viewModel.isButtonRegisterEnable.observeForever(observerButtonRegisterEnableMock)

        // action
        viewModel.confirmPassword.value = invalidConfirmPassword
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonRegisterEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonEnable to true when email, password and confirm password are valid`() {
        // arrange
        val expectedResult = true
        every {
            useCase.isRegisterFormValid(
                email = any(),
                password = any(),
                confirmPassword = any()
            )
        } returns true
        viewModel.isButtonRegisterEnable.observeForever(observerButtonRegisterEnableMock)

        // action
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonRegisterEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonEnable to true when email (with trailing spaces), password and confirm password are valid`() {
        every {
            useCase.isRegisterFormValid(
                email = loginRequestValid.email,
                password = loginEmailTrailingSpacesRequestValid.password,
                confirmPassword = loginEmailTrailingSpacesRequestValid.confirmPassword
            )
        } returns true

        viewModel.email.value = loginEmailTrailingSpacesRequestValid.email
        viewModel.password.value = loginEmailTrailingSpacesRequestValid.password
        viewModel.confirmPassword.value = loginEmailTrailingSpacesRequestValid.confirmPassword
        viewModel.isButtonRegisterEnable.observeForever(observerButtonRegisterEnableMock)

        // action
        invokePrivateMethod(name = VALIDATE_FORM_METHOD)

        // assert
        verify {
            observerButtonRegisterEnableMock.onChanged(true)
        }
    }

    @Test
    fun `should change registerState to NETWORK error when register with network fail`() {
        // arrange
        val expectedErrorType = RegisterState.Error(BprErrorType.NETWORK)
        coEvery {
            useCase.register(
                email = any(),
                password = any()
            )
        } returns expectedErrorType
        viewModel.registerState.observeForever(observerRegisterStateMock)

        // action
        viewModel.register()

        //assert
        verifyOrder {
            observerRegisterStateMock.onChanged(RegisterState.Loading)
            observerRegisterStateMock.onChanged(expectedErrorType)
        }
    }

    @Test
    fun `should change registerState to INVALID_ACCOUNT error when register with email already exists`() {
        // arrange
        val expectedErrorType = RegisterState.Error(BprErrorType.INVALID_ACCOUNT)
        val emailAlreadyExists = loginRequestValid.email
        coEvery {
            useCase.register(
                email = emailAlreadyExists,
                password = any()
            )
        } returns expectedErrorType
        viewModel.registerState.observeForever(observerRegisterStateMock)

        // action
        viewModel.email.value = emailAlreadyExists
        viewModel.register()

        //assert
        verifyOrder {
            observerRegisterStateMock.onChanged(RegisterState.Loading)
            observerRegisterStateMock.onChanged(expectedErrorType)
        }
    }

    @Test
    fun `should change registerState to UNKNOWN error when register with unkown error`() {
        // arrange
        val expectedErrorType = RegisterState.Error(BprErrorType.UNKNOWN)
        coEvery {
            useCase.register(
                email = any(),
                password = any()
            )
        } returns expectedErrorType
        viewModel.registerState.observeForever(observerRegisterStateMock)

        // action
        viewModel.register()

        //assert
        verifyOrder {
            observerRegisterStateMock.onChanged(RegisterState.Loading)
            observerRegisterStateMock.onChanged(expectedErrorType)
        }
    }

    @Test
    fun `should change registerState to Success when register with with success`() {
        // arrange
        val expectedState = RegisterState.Success
        coEvery {
            useCase.register(
                email = any(),
                password = any()
            )
        } returns expectedState
        viewModel.registerState.observeForever(observerRegisterStateMock)

        // action
        viewModel.register()

        //assert
        verifyOrder {
            observerRegisterStateMock.onChanged(RegisterState.Loading)
            observerRegisterStateMock.onChanged(expectedState)
        }
    }

    @Test
    fun `when register, should trim email parameter`() {
        coEvery {
            useCase.register(
                email = loginRequestValid.email,
                password = loginRequestValid.password
            )
        } returns RegisterState.Success

        viewModel.email.value = loginEmailTrailingSpacesRequestValid.email
        viewModel.password.value = loginEmailTrailingSpacesRequestValid.password
        viewModel.registerState.observeForever(observerRegisterStateMock)

        viewModel.register()

        verifyOrder {
            observerRegisterStateMock.onChanged(RegisterState.Loading)
            observerRegisterStateMock.onChanged(RegisterState.Success)
        }

        coVerify { useCase.register(loginRequestValid.email, any()) }
    }

    private fun invokePrivateMethod(name: String) {
        viewModel.javaClass.getDeclaredMethod(name).apply {
            isAccessible = true
            invoke(viewModel)
        }
    }

    companion object {
        private const val VALIDATE_FORM_METHOD = "validateForm"
    }
}