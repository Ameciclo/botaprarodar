package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.LoginRequest
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.loginRequestValid
import app.igormatos.botaprarodar.loginRequestWithInvalidEmail
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
internal class PasswordRecoveryActivityTest {

    private lateinit var scenario: ActivityScenario<PasswordRecoveryActivity>
    private lateinit var testModule: Module
    private lateinit var passwordRecoveryUseCase: PasswordRecoveryUseCase

    @Before
    fun setup() {
        passwordRecoveryUseCase = mockk(relaxed = true)

        testModule = module {
            single(override = true) {
                passwordRecoveryUseCase
            }
        }

        loadKoinModules(testModule)
        scenario = launchActivity()
    }

    @Test
    fun shouldButtonSendDisable_whenEmailIsNotValid() {
        val loginRequest: LoginRequest = loginRequestWithInvalidEmail
        defineUseCaseBehavior(loginRequest.email, false)
        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
        } verify {
            checkBtnSendIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSendEnable_whenEmailIsValid() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest.email, true)
        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
        } verify {
            checkBtnSendIsEnable()
        }
    }

    @Test
    fun shouldShowSuccessMessage_whenRecoveryPasswordRequestIsExecutedSuccessfully() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest.email, true, PasswordRecoveryState.Success)
        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
            clickBtnSend()
        } verify {
            checkSuccessMessage()
        }
    }

    @Test
    fun shouldShowUnknownErrorMessage_whenUnmappedExceptionOccursToRecoveryPassword() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(
            loginRequest.email,
            true,
            PasswordRecoveryState.Error(BprErrorType.UNKNOWN)
        )

        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
            clickBtnSend()
        } verify {
            checkUnknownErrorMessage()
        }
    }

    private fun defineUseCaseBehavior(
        email: String,
        isEmailValid: Boolean,
        passwordRecoveryState: PasswordRecoveryState = PasswordRecoveryState.Error(BprErrorType.UNKNOWN)
    ) {
        every {
            passwordRecoveryUseCase.isEmailValid(email)
        } returns isEmailValid

        coEvery {
            passwordRecoveryUseCase.sendPasswordResetEmail(email)
        } returns passwordRecoveryState
    }

    @Test
    fun shouldShowNetworkErrorMessage_whenThereIsNoConnectionToRecoveryPassword() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(
            loginRequest.email,
            true,
            PasswordRecoveryState.Error(BprErrorType.NETWORK)
        )

        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
            clickBtnSend()
        } verify {
            checkNetworkErrorMessage()
        }
    }

    @Test
    fun shouldShowInvalidEmailAccountErrorMessage_whenUnregisteredEmailIsInformedToRecoveryPassword() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(
            loginRequest.email,
            true,
            PasswordRecoveryState.Error(BprErrorType.INVALID_ACCOUNT)
        )

        passwordRecoveryActivity {
            fillEmailField(loginRequest.email)
            clickBtnSend()
        } verify {
            checkInputEmailHasError()
        }
    }
}