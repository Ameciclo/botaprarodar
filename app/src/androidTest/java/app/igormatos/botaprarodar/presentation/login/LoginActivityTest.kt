package app.igormatos.botaprarodar.presentation.login

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.LoginRequest
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.loginRequestValid
import app.igormatos.botaprarodar.loginRequestWithInvalidEmail
import app.igormatos.botaprarodar.loginRequestWithInvalidPassword
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
internal class LoginActivityTest {

    private lateinit var scenario: ActivityScenario<LoginActivity>
    private lateinit var testModule: Module
    private lateinit var useCase: LoginUseCase

    @Before
    fun setup() {
        useCase = mockk(relaxed = true)

        testModule = module {
            single(override = true) {
                useCase
            }
            single<FirebaseAuthModule>(override = true) {
                FirebaseAuthModuleTestImpl(mockk(relaxed = true))
            }
        }
        loadKoinModules(testModule)
        scenario = launchActivity()
    }

    @Test
    fun shouldButtonSignInDisable_whenEmailIsNotValid() {
        val loginRequest: LoginRequest = loginRequestWithInvalidEmail
        defineUseCaseBehavior(loginRequest, false)

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
        } verify {
            checkBtnSignInIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSignInDisable_whenPasswordIsNotValid() {
        val loginRequest: LoginRequest = loginRequestWithInvalidPassword
        defineUseCaseBehavior(loginRequest, false)

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
        } verify {
            checkBtnSignInIsNotEnable()
        }
    }

    @Test
    fun shouldButtonSignInEnable_whenEmailAndPasswordAreValid() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true)

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
        } verify {
            checkBtnSignInIsEnable()
        }
    }

    @Test
    fun shouldShowEmailNotFoundError_whenUnregisteredEmailIsInformedToLogin() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.INVALID_ACCOUNT))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkInputEmailHasError()
        }
    }

    @Test
    fun shouldShowPasswordError_whenRegisteredEmailAndWrongPasswordAreInformedToLogin() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.INVALID_PASSWORD))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkInputPasswordHasError()
        }
    }

    @Test
    fun shouldShowNetworkError_whenThereIsNoConnectionToLogin() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.NETWORK))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkNetworkErrorMessage()
        }
    }

    @Test
    fun shouldShowUnknownError_whenUnmappedExceptionOccursOnLogin() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.UNKNOWN))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkUnknownErrorMessage()
        }
    }

    @Test
    fun shouldShowEmailNotVerifiedError_whenUnverifiedEmailIsInformedToLogin() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.EMAIL_NOT_VERIFIED))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkUnverifiedEmailErrorMessage()
        }
    }

    @Test
    fun shouldOpenSelectCommunityActivity_whenLoginIsSuccess() {
        val loginRequest: LoginRequest = loginRequestValid
        defineUseCaseBehavior(loginRequest, true, LoginState.Success(mockk(relaxed = true)))

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkSelectCommunityActivityIsVisible()
        }
    }

    private fun defineUseCaseBehavior(
        loginRequest: LoginRequest,
        isLoginFormValid: Boolean,
        loginState: LoginState = LoginState.Error(BprErrorType.UNKNOWN)
    ) {
        every {
            useCase.isLoginFormValid(
                loginRequest.email,
                loginRequest.password
            )
        } returns isLoginFormValid

        coEvery {
            useCase.authenticateAdmin(
                loginRequest.email,
                loginRequest.password
            )
        } returns loginState
    }
}