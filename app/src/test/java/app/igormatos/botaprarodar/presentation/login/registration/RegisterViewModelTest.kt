package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.presentation.login.signin.BprResult
import app.igormatos.botaprarodar.utils.*
import com.google.common.truth.Truth
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class RegisterViewModelTest {

    private lateinit var useCase: RegisterUseCase
    private lateinit var viewModel: RegisterViewModel

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        viewModel = RegisterViewModel(useCase)
    }

    @Test
    fun `should change registerState to NETWORK error when register with network fail`() = runBlocking {
        // arrange
        val errorType = BprResult.Failure(mockk(), BprErrorType.NETWORK)

        coEvery { useCase.invoke(any(), any()) } returns errorType

        // action
        viewModel.register {}

        //assert
        val registerState = viewModel.state.first()
        assertThat(registerState, instanceOf(RegisterState.Error::class.java))
    }

    @Test
    fun `should change registerState to INVALID_ACCOUNT error when register with email already exists`() = runBlocking {
        // arrange
        val expectedErrorType = BprResult.Failure(mockk(), BprErrorType.INVALID_ACCOUNT)
        coEvery { useCase.invoke(any(), any()) } returns expectedErrorType

        // action
        viewModel.register {}

        //assert
        val registerState = viewModel.state.first()
        assertThat(registerState, instanceOf(RegisterState.Success::class.java))
        Truth.assertThat(viewModel.state.first().data.emailError).isEqualTo(R.string.email_already_registered_error)
    }

    @Test
    fun `should change registerState to UNKNOWN error when register with unkown error`()= runBlocking {
        // arrange
        val expectedErrorType = BprResult.Failure(mockk(), BprErrorType.UNKNOWN)

        coEvery { useCase.invoke(any(), any()) } returns expectedErrorType

        // action
        viewModel.register {}

        //assert
        val registerState = viewModel.state.first()
        assertThat(registerState, instanceOf(RegisterState.Error::class.java))
    }

    @Test
    fun `should change registerState to Success when register with with success`() {
        // arrange
        var called = false

        coEvery { useCase.invoke(any(), any()) } returns BprResult.Success(Unit)

        // action
        viewModel.register {
            called = true
        }

        //assert
        assertThat(called, `is`(true))
    }
}