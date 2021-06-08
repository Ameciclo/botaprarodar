package app.igormatos.botaprarodar.presentation.login.registration

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.*
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
internal class RegisterActivityTest {

    private lateinit var scenario: ActivityScenario<RegisterActivity>
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var testModule: Module

    @Before
    fun setup() {
        registerUseCase = mockk(relaxed = true)

        testModule = module {
            single(override = true) {
                registerUseCase
            }
        }
        loadKoinModules(testModule)
        scenario = launchActivity()
    }

    @Test
    fun shouldButtonSendDisable_whenEmailIsNotValidAndPasswordAndConfirmPasswordAreValid() {
        val registerRequest: RegisterRequest = registerRequestWithInvalidEmail
        defineUseCaseBehavior(registerRequest, false)

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
        } verify {
            checkBtnSendIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSendDisable_whenPasswordIsNotValidAndEmailAndConfirmPasswordAreValid() {
        val registerRequest: RegisterRequest = registerRequestWithInvalidPassword
        defineUseCaseBehavior(registerRequest, false)

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
        } verify {
            checkBtnSendIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSendDisable_whenConfirmPasswordIsNotValidAndEmailAndPasswordAreValid() {
        val registerRequest: RegisterRequest = registerRequestWithInvalidConfirmPassword
        defineUseCaseBehavior(registerRequest, false)

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
        } verify {
            checkBtnSendIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSendEnable_whenEmailAndPasswordAndConfirmPasswordAreValid() {
        val registerRequest: RegisterRequest = registerRequestValid
        defineUseCaseBehavior(registerRequest, true, RegisterState.Success)

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
        } verify {
            checkBtnSendIsEnable()
        }
    }

    @Test
    fun shouldShowSuccessMessage_whenRegisterRequestIsExecutedSuccessfully() {
        val registerRequest: RegisterRequest = registerRequestValid
        defineUseCaseBehavior(registerRequest, true, RegisterState.Success)

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
            clickBtnSend()
        } verify {
            checkSuccessMessage()
        }
    }

    @Test
    fun shouldShowUnknownErrorMessage_whenUnmappedExceptionOccursToRegister() {
        val registerRequest: RegisterRequest = registerRequestValid
        defineUseCaseBehavior(registerRequest, true, RegisterState.Error(BprErrorType.UNKNOWN))

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
            clickBtnSend()
        } verify {
            checkUnknownErrorMessage()
        }
    }

    @Test
    fun shouldShowNetworkErrorMessage_whenThereIsNoConnectionToRegister() {
        val registerRequest: RegisterRequest = registerRequestValid
        defineUseCaseBehavior(registerRequest, true, RegisterState.Error(BprErrorType.NETWORK))

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
            clickBtnSend()
        } verify {
            checkNetworkErrorMessage()
        }
    }

    @Test
    fun shouldShowEmailAccountAlreadyExistsErrorMessage_whenEmailAlreadyRegisteredIsInformedToRegister() {
        val registerRequest: RegisterRequest = registerRequestValid
        defineUseCaseBehavior(
            registerRequest,
            true,
            RegisterState.Error(BprErrorType.INVALID_ACCOUNT)
        )

        registerActivity {
            fillEmailField(registerRequest.email)
            fillPasswordField(registerRequest.password)
            fillConfirmPasswordField(registerRequest.confirmPassword)
            clickBtnSend()
        } verify {
            checkInputEmailHasError()
        }
    }

    private fun defineUseCaseBehavior(
        registerRequest: RegisterRequest,
        isRegisterFormValid: Boolean,
        registerState: RegisterState = RegisterState.Error(BprErrorType.UNKNOWN)
    ) {
        every {
            registerUseCase.isRegisterFormValid(
                registerRequest.email,
                registerRequest.password,
                registerRequest.confirmPassword
            )
        } returns isRegisterFormValid

        coEvery {
            registerUseCase.register(
                registerRequest.email,
                registerRequest.password
            )
        } returns registerState
    }
}