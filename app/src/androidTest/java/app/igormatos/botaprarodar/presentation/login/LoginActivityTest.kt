package app.igormatos.botaprarodar.presentation.login

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.*
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.network.firebase.FirebaseAuthModule
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityState
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityUseCase
import app.igormatos.botaprarodar.presentation.login.selectCommunity.UserInfoState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
internal class LoginActivityTest {

    private lateinit var scenario: ActivityScenario<LoginActivity>
    private lateinit var testModule: Module
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var selectCommunityUseCase: SelectCommunityUseCase

    @Before
    fun setup() {
        loginUseCase = mockk(relaxed = true)
        selectCommunityUseCase = mockk(relaxed = true)

        testModule = module {
            factory(override = true) {
                selectCommunityUseCase
            }

            single(override = true) {
                loginUseCase
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
        defineLoginUseCaseBehavior(loginRequest, false)

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
        defineLoginUseCaseBehavior(loginRequest, false)

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
        defineLoginUseCaseBehavior(loginRequest, true)

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
        } verify {
            checkBtnSignInIsEnable()
        }
    }

    @Test
    fun shouldButtonSignInEnable_whenEmailWithTrailingSpacesAndPasswordAreValid() {
        val loginRequest: LoginRequest = loginRequestTrailingSpacesValid
        defineLoginUseCaseBehavior(loginRequest, true)

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
        defineLoginUseCaseBehavior(
            loginRequest,
            true,
            LoginState.Error(BprErrorType.INVALID_ACCOUNT)
        )

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
        defineLoginUseCaseBehavior(
            loginRequest,
            true,
            LoginState.Error(BprErrorType.INVALID_PASSWORD)
        )

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
        defineLoginUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.NETWORK))

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
        defineLoginUseCaseBehavior(loginRequest, true, LoginState.Error(BprErrorType.UNKNOWN))

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
        defineLoginUseCaseBehavior(
            loginRequest,
            true,
            LoginState.Error(BprErrorType.EMAIL_NOT_VERIFIED)
        )

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
        defineLoginUseCaseBehavior(loginRequest, true, LoginState.Success(mockk(relaxed = true)))
        defineSelectCommunityUseCaseBehavior()

        loginActivity {
            fillEmailField(loginRequest.email)
            fillPasswordField(loginRequest.password)
            clickBtnSignIn()
        } verify {
            checkSelectCommunityActivityIsVisible()
        }
    }

    private fun defineSelectCommunityUseCaseBehavior() {
        val communities: MutableList<Community> = mutableListOf(Community(name = "Test Community"))
        coEvery {
            selectCommunityUseCase.loadCommunitiesByAdmin(any(), any())
        } returns SelectCommunityState.Success(UserInfoState.Admin(communities))
    }

    private fun defineLoginUseCaseBehavior(
        loginRequest: LoginRequest,
        isLoginFormValid: Boolean,
        loginState: LoginState = LoginState.Error(BprErrorType.UNKNOWN)
    ) {
        every {
            loginUseCase.isLoginFormValid(
                loginRequest.email,
                loginRequest.password
            )
        } returns isLoginFormValid

        coEvery {
            loginUseCase.authenticateAdmin(
                loginRequest.email,
                loginRequest.password
            )
        } returns loginState
    }
}