package app.igormatos.botaprarodar.presentation.login

import android.content.Context
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.*
import app.igormatos.botaprarodar.presentation.login.signin.LoginState
import app.igormatos.botaprarodar.presentation.login.signin.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.signin.SignInData
import app.igormatos.botaprarodar.presentation.login.signin.composables.LoginScreen
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalComposeUiApi
@RunWith(AndroidJUnit4::class)
internal class LoginActivityTest {

    private lateinit var testModule: Module
    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val composeTestRule = createComposeRule()

    private val signInButton = (hasText(app.igormatos.botaprarodar.R.string.action_sign_in.asString().uppercase()) and hasClickAction())
    private val forgotPasswordButton = (hasText(app.igormatos.botaprarodar.R.string.forgot_my_password.asString()) and hasClickAction())
    private val registerButton = (hasText(app.igormatos.botaprarodar.R.string.sign_up.asString()) and hasClickAction())

    @Before
    fun setup() {
        viewModel = mockk(relaxed = true)

        testModule = module {
            viewModel(override = true) { viewModel }
        }
        loadKoinModules(testModule)
    }

    @Test
    fun shouldHaveSignInButtonDisable_whenNoDataHasBeenSet() {
        val state = MutableStateFlow(LoginState.Success(SignInData()))

        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onNode(signInButton).assertIsDisplayed()
        composeTestRule.onNode(signInButton).assertIsNotEnabled()
        composeTestRule.onNode(forgotPasswordButton).assertIsDisplayed()
        composeTestRule.onNode(registerButton).assertIsDisplayed()
    }

    @Test
    fun shouldHaveSignInButtonEnabled_whenDataHasBeenSet() {
        val state = MutableStateFlow(LoginState.Success(SignInData(email = "test@test.com", password = "123456")))
        val text = app.igormatos.botaprarodar.R.string.action_sign_in.asString().uppercase()
        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onNodeWithText(text).assertIsEnabled()

        composeTestRule.onNodeWithText(text).performClick()
    }

    @Test
    fun shouldHaveErrorMessageDisplayed_whenDataIsInvalid() {
        val data = SignInData(
            email = "test@test.com",
            password = "123456",
            emailError = app.igormatos.botaprarodar.R.string.sign_in_incorrect_email_password_error,
            passwordError = app.igormatos.botaprarodar.R.string.sign_in_incorrect_email_password_error
        )
        val state = MutableStateFlow(LoginState.Success(data))
        val text = app.igormatos.botaprarodar.R.string.sign_in_incorrect_email_password_error.asString()

        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onAllNodesWithText(text)[0].assertIsDisplayed()
        composeTestRule.onAllNodesWithText(text)[1].assertIsDisplayed()
    }

    @Test
    fun shouldShowNetworkError_whenThereIsNoConnectionToLogin() {
        val data = SignInData(email = "test@test.com", password = "123456")
        val message = app.igormatos.botaprarodar.R.string.network_error_message
        val state = MutableStateFlow(LoginState.Error(data, message))
        val text = message.asString()

        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun shouldShowUnknownError_whenUnmappedExceptionOccursOnLogin() {
        val data = SignInData(email = "test@test.com", password = "123456")
        val message = app.igormatos.botaprarodar.R.string.login_error
        val state = MutableStateFlow(LoginState.Error(data, message))
        val text = message.asString()

        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    @Test
    fun shouldShowEmailNotVerifiedError_whenUnverifiedEmailIsInformedToLogin() {
        val data = SignInData(email = "test@test.com", password = "123456")
        val state = MutableStateFlow(LoginState.RetryVerifyEmail(data))
        val text = app.igormatos.botaprarodar.R.string.login_confirm_email_error.asString()
        val action = app.igormatos.botaprarodar.R.string.resend_email.asString()

        every { viewModel.state } returns state

        composeTestRule.setContent {
            LoginScreen(viewModel)
        }

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
        composeTestRule.onNodeWithText(action).assertIsDisplayed()
    }

    private fun Int.asString() = ApplicationProvider
        .getApplicationContext<Context>()
        .getText(this)
        .toString()
}
