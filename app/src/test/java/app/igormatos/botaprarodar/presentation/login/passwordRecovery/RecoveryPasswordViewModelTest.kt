package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import app.igormatos.botaprarodar.utils.InstantExecutorExtension
import app.igormatos.botaprarodar.utils.loginRequestValid
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class RecoveryPasswordViewModelTest {

    private lateinit var useCase: PasswordRecoveryUseCase
    private lateinit var viewModel: RecoveryPasswordViewModel

    @BeforeEach
    fun setup() {
        useCase = mockk()
        viewModel = RecoveryPasswordViewModel(useCase)
    }

    @Test
    internal fun `should update state when email changes`() = runBlocking {
        viewModel.onEmailChanged(loginRequestValid.email)

        assertThat(viewModel.state.value).isInstanceOf(RecoveryPasswordState.Success::class.java)
        assertThat(viewModel.state.value.data.email).isEqualTo(loginRequestValid.email)
    }

    @Test
    fun `should emit loading state`() = runBlocking {
        // arrange
        coEvery { useCase.invoke(any()) } throws Exception()

        // action
        runCatching { viewModel.recoverPassword {} }

        //assert
        assertThat(viewModel.state.first()).isInstanceOf(RecoveryPasswordState.Loading::class.java)
    }

    @Test
    fun `should notify password recovery changed successfully`() {
        // arrange
        var called = false

        coEvery { useCase.invoke(any()) } returns BprResult.Success(Unit)

        // action
        viewModel.recoverPassword {
            called = true
        }

        //assert
        assertTrue(called)
    }

    @Test
    fun `should change passwordRecoveryState to NETWORK error when use case execute with network fail`() = runBlocking {
        // arrange
        val errorType = BprResult.Failure(Exception(), BprErrorType.NETWORK)

        coEvery { useCase.invoke(any()) } returns errorType

        // action
        viewModel.recoverPassword {}

        //assert
        assertThat(viewModel.state.first()).isInstanceOf(RecoveryPasswordState.Error::class.java)
        assertThat((viewModel.state.first() as RecoveryPasswordState.Error).message).isEqualTo(R.string.network_error_message)
    }

    @Test
    fun `should change passwordRecoveryState to INVALID_ACCOUNT error when use case execute with email invalid account`() = runBlocking {
        // arrange
        val errorType = BprResult.Failure(Exception(), BprErrorType.INVALID_ACCOUNT)
        val expectedResult = RecoveryPasswordData(emailError = R.string.sign_in_email_error)

        coEvery { useCase.invoke(any()) } returns errorType

        // action
        viewModel.recoverPassword {}

        //assert
        assertThat(viewModel.state.first()).isInstanceOf(RecoveryPasswordState.Success::class.java)
        assertThat(viewModel.state.first().data).isEqualTo(expectedResult)
    }

    @Test
    fun `should change passwordRecoveryState to UNKNOWN error when use case execute with email invalid account`() = runBlocking {
        // arrange
        val errorType = BprResult.Failure(Exception(), BprErrorType.UNKNOWN)

        coEvery { useCase.invoke(any()) } returns errorType

        // action
        viewModel.recoverPassword {}

        //assert
        assertThat(viewModel.state.first()).isInstanceOf(RecoveryPasswordState.Error::class.java)
        assertThat((viewModel.state.first() as RecoveryPasswordState.Error).message).isEqualTo(R.string.unkown_error)
    }
}