package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class RecoveryPasswordViewModelTest {

    private lateinit var useCase: PasswordRecoveryUseCase
    private lateinit var viewModel: RecoveryPasswordViewModel

    private val observerButtonEnableMock = mockk<Observer<Boolean>>(relaxed = true)
    private val observerPasswordRecoveryStateMock =
        mockk<Observer<PasswordRecoveryState>>(relaxed = true)

    @BeforeEach
    fun setup() {
        useCase = mockk()
        viewModel = RecoveryPasswordViewModel(useCase)
    }

    @Test
    fun `should change isButtonEnable to false when email is invalid`() {
        // arrange
        val expectedResult = false
        val invalidEmail = INVALID_EMAIL
        every {
            useCase.isEmailValid(invalidEmail)
        } returns false
        viewModel.isButtonEnable.observeForever(observerButtonEnableMock)

        // action
        viewModel.email.value = invalidEmail
        viewModel.javaClass.getDeclaredMethod(
            VALIDATE_FORM_METHOD
        ).apply {
            isAccessible = true
            invoke(viewModel)
        }

        // assert
        verify {
            observerButtonEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change isButtonEnable to true when email is valid`() {
        // arrange
        val expectedResult = true
        val validEmail = VALID_EMAIL
        every {
            useCase.isEmailValid(validEmail)
        } returns true
        viewModel.isButtonEnable.observeForever(observerButtonEnableMock)

        // action
        viewModel.email.value = validEmail
        viewModel.javaClass.getDeclaredMethod(
            VALIDATE_FORM_METHOD
        ).apply {
            isAccessible = true
            invoke(viewModel)
        }

        // assert
        verify {
            observerButtonEnableMock.onChanged(expectedResult)
        }
    }

    @Test
    fun `should change passwordRecoveryState to success when usecase execute with success`() {
        // arrange
        val validEmail = VALID_EMAIL
        coEvery {
            useCase.sendPasswordResetEmail(validEmail)
        } returns PasswordRecoveryState.Success
        viewModel.passwordRecoveryState.observeForever(observerPasswordRecoveryStateMock)

        // action
        viewModel.recoveryPassword(validEmail)

        //assert
        verifyOrder {
            observerPasswordRecoveryStateMock.onChanged(PasswordRecoveryState.Loading)
            observerPasswordRecoveryStateMock.onChanged(PasswordRecoveryState.Success)
        }
    }

    @Test
    fun `should change passwordRecoveryState to network error when usecase execute with network fail`() {
        // arrange
        val expectedErrorType = PasswordRecoveryState.Error(BprErrorType.NETWORK)
        val validEmail = VALID_EMAIL
        coEvery {
            useCase.sendPasswordResetEmail(validEmail)
        } returns expectedErrorType
        viewModel.passwordRecoveryState.observeForever(observerPasswordRecoveryStateMock)

        // action
        viewModel.recoveryPassword(validEmail)

        //assert
        verifyOrder {
            observerPasswordRecoveryStateMock.onChanged(PasswordRecoveryState.Loading)
            observerPasswordRecoveryStateMock.onChanged(expectedErrorType)
        }
    }

    @Test
    fun `should change passwordRecoveryState to INVALID_ACCOUNT error when usecase execute with email invalid account`() {
        // arrange
        val expectedErrorType = PasswordRecoveryState.Error(BprErrorType.INVALID_ACCOUNT)
        val email = INVALID_ACCOUNT
        coEvery {
            useCase.sendPasswordResetEmail(email)
        } returns expectedErrorType
        viewModel.passwordRecoveryState.observeForever(observerPasswordRecoveryStateMock)

        // action
        viewModel.recoveryPassword(email)

        //assert
        verifyOrder {
            observerPasswordRecoveryStateMock.onChanged(PasswordRecoveryState.Loading)
            observerPasswordRecoveryStateMock.onChanged(expectedErrorType)
        }
    }

    companion object {
        private const val VALIDATE_FORM_METHOD = "validateForm"
        private const val VALID_EMAIL = "test@test.com"
        private const val INVALID_EMAIL = "test@test"
        private const val INVALID_ACCOUNT = "test@test.test"
    }
}