package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.presentation.login.resendEmail.ResendEmailUseCase
import app.igormatos.botaprarodar.presentation.login.signin.SignInData
import app.igormatos.botaprarodar.domain.usecase.signin.LoginUseCase
import app.igormatos.botaprarodar.presentation.login.signin.LoginState
import app.igormatos.botaprarodar.presentation.login.signin.LoginViewModel
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import app.igormatos.botaprarodar.utils.*
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class LoginViewModelTest {

    @RelaxedMockK
    private lateinit var loginUseCase: LoginUseCase

    @RelaxedMockK
    private lateinit var resendEmailUseCase: ResendEmailUseCase

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(loginUseCase, resendEmailUseCase)
    }

    @Test
    internal fun `should update state when email changes`() = runBlocking {
        viewModel.onEmailChanged(loginRequestValid.email)

        assertThat(viewModel.state.value).isInstanceOf(LoginState.Success::class.java)
        assertThat(viewModel.state.value.data.email).isEqualTo(loginRequestValid.email)
    }

    @Test
    internal fun `should update state when password changes`() = runBlocking {
        viewModel.onPasswordChanged(loginRequestValid.password)

        assertThat(viewModel.state.value).isInstanceOf(LoginState.Success::class.java)
        assertThat(viewModel.state.value.data.password).isEqualTo(loginRequestValid.password)
    }

    @Test
    internal fun `should update state when show password changes`() = runBlocking {
        viewModel.onShowPasswordChanged(true)

        assertThat(viewModel.state.value).isInstanceOf(LoginState.Success::class.java)
        assertThat(viewModel.state.value.data.showPassword).isTrue()
    }

    @Test
    fun `should change LoginState to NETWORK error when execute login without network connection`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.NETWORK)
            val expectedResult = LoginState.Error(signInDataValid, R.string.network_error_message)

            viewModel.onEmailChanged(loginRequestValid.email)
            viewModel.onPasswordChanged(loginRequestValid.password)

            coEvery { loginUseCase.invoke(any(), any()) } returns error

            // action
            viewModel.login {}

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should change LoginState to INVALID_ACCOUNT error when execute login with non-existent email account`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.INVALID_ACCOUNT)
            val data = SignInData(
                emailError = R.string.sign_in_incorrect_email_password_error,
                passwordError = R.string.sign_in_incorrect_email_password_error
            )
            val expectedResult = LoginState.Success(data)

            coEvery { loginUseCase.invoke(any(), any()) } returns error

            // action
            viewModel.login {}

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should change LoginState to INVALID_PASSWORD error when execute login with existent email account and wrong password`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.INVALID_ACCOUNT)
            val data = SignInData(
                emailError = R.string.sign_in_incorrect_email_password_error,
                passwordError = R.string.sign_in_incorrect_email_password_error
            )
            val state = LoginState.Success(data)

            coEvery { loginUseCase.invoke(any(), any()) } returns error

            // action
            viewModel.login {}

            // assert
            assertThat(viewModel.state.first()).isEqualTo(state)
        }

    @Test
    fun `should change LoginState to EMAIL_NOT_VERIFIED error when execute login with existent email account and corret password, but unverified account`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.EMAIL_NOT_VERIFIED)
            val expectedResult = LoginState.RetryVerifyEmail(SignInData())

            coEvery { loginUseCase.invoke(any(), any()) } returns error

            // action
            viewModel.login {}

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should change LoginState to UNKNOWN error when execute login with unmapped exception`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.UNKNOWN)
            val expectedResult = LoginState.Error(SignInData(), R.string.login_error)

            coEvery { loginUseCase.invoke(any(), any()) } returns error

            // action
            viewModel.login {}

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should have an admin when execute login successfully`() {
        // arrange
        val email = loginRequestValid.email
        val password = loginRequestValid.password
        val admin = Admin(email, password, "1")
        val expectedResult = BprResult.Success(admin)
        var actualAdmin: Admin? = null

        coEvery { loginUseCase.invoke(any(), any()) } returns expectedResult

        // action
        viewModel.login {
            actualAdmin = it
        }

        // assert
        assertThat(actualAdmin).isEqualTo(admin)
    }

    @Test
    fun `should change ResendEmailState to NETWORK error when execute sendEmailVerification without network connection`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.NETWORK)
            val expectedResult = LoginState.Error(SignInData(), R.string.network_error_message)

            coEvery { resendEmailUseCase.invoke() } returns error

            // action
            viewModel.sendEmailVerification()

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should change ResendEmailState to UNKNOWN error when execute sendEmailVerification with unmapped exception`() =
        runBlocking {
            // arrange
            val error = BprResult.Failure(Exception(), BprErrorType.UNKNOWN)
            val expectedResult = LoginState.Error(SignInData(), R.string.login_error)

            coEvery { resendEmailUseCase.invoke() } returns error

            // action
            viewModel.sendEmailVerification()

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }

    @Test
    fun `should change ResendEmailState to Success when execute sendEmailVerification with success`() =
        runBlocking {
            // arrange
            val expectedResult = LoginState.EmailSent(SignInData())

            coEvery { resendEmailUseCase.invoke() } returns BprResult.Success(Unit)

            // action
            viewModel.sendEmailVerification()

            // assert
            assertThat(viewModel.state.first()).isEqualTo(expectedResult)
        }
}