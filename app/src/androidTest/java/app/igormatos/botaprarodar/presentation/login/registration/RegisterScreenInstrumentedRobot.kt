import androidx.compose.ui.test.*
import app.igormatos.botaprarodar.BaseAndroidComposeTest
import app.igormatos.botaprarodar.BaseRobotScreen
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.loginRequestValid
import app.igormatos.botaprarodar.presentation.login.registration.*
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert

inline fun BaseAndroidComposeTest.instrumentedRegisterScreen(block: RegisterScreenInstrumentedRobot.() -> Unit) =
    RegisterScreenInstrumentedRobot(this).apply { block() }

class RegisterScreenInstrumentedRobot(
    baseAndroidComposeTest: BaseAndroidComposeTest
) : BaseRobotScreen(baseAndroidComposeTest.composeTestRule) {

    @RelaxedMockK
    private lateinit var viewModel: RegisterViewModel

    private val stateFlow = MutableStateFlow<RegisterState>(RegisterState.Empty)
    private var callback: RegisterEvent? = null

    init {
        MockKAnnotations.init(this)

        every { viewModel.state } returns stateFlow

        composeTestRule.setContent { RegisterScreen(viewModel, onEvent = { callback = it}) }
    }

    infix fun verify(block: RegisterScreenInstrumentedRobot.() -> Unit) {
        block()
    }

    fun emitSuccessResult() = runBlocking {
        val data = RegisterData(
            email = loginRequestValid.email,
            password = loginRequestValid.password
        )
        stateFlow.emit(RegisterState.Success(data))
    }

    fun emitNetworkErrorResult() = runBlocking {
        stateFlow.emit(RegisterState.Error(RegisterData(), R.string.network_error_message))
    }

    fun emitUnkownErrorResult() = runBlocking {
        stateFlow.emit(RegisterState.Error(RegisterData(), R.string.unkown_error))
    }

    fun clickRegister() {
        val button = (hasText(getString(app.igormatos.botaprarodar.R.string.btn_register).uppercase()) and hasClickAction())
        composeTestRule.onNode(button).performClick()
    }

    fun isNetworkErrorVisible() {
        val text = getString(app.igormatos.botaprarodar.R.string.network_error_message)

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun isUnkownErrorVisible() {
        val text = getString(app.igormatos.botaprarodar.R.string.unkown_error)

        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }

    fun isSuccess() {
        Assert.assertEquals(callback, RegisterEvent.RegisterSuccessful)
    }
}