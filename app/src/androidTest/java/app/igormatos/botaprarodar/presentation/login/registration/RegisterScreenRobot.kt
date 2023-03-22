import androidx.compose.ui.test.*
import app.igormatos.botaprarodar.BaseAndroidComposeTest
import app.igormatos.botaprarodar.BaseRobotScreen
import app.igormatos.botaprarodar.presentation.login.registration.RegisterScreen
import io.mockk.MockKAnnotations
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

inline fun BaseAndroidComposeTest.registerScreen(block: RegisterScreenRobot.() -> Unit) =
    RegisterScreenRobot(this).apply { block() }

class RegisterScreenRobot(
    baseAndroidComposeTest: BaseAndroidComposeTest
) : BaseRobotScreen(baseAndroidComposeTest.composeTestRule) {

    init {
        MockKAnnotations.init(this)

        composeTestRule.setContent { RegisterScreen() }
    }

    infix fun verify(block: RegisterScreenRobot.() -> Unit) {
        block()
    }

    fun fillEmailField(text: String) {
        composeTestRule.onNodeWithTag("email").performTextReplacement(text)
    }

    fun fillPasswordField(text: String) {
        composeTestRule.onNodeWithTag("password").performTextReplacement(text)
    }

    fun fillConfirmPasswordField(text: String) {
        composeTestRule.onNodeWithTag("confirmPassword").performTextReplacement(text)
    }

    fun clickRegister() {
        val button = (hasText(getString(app.igormatos.botaprarodar.R.string.btn_register).uppercase()) and hasClickAction())
        composeTestRule.onNode(button).performClick()
    }

    fun isRegisterEnabled() {
        val text = getString(app.igormatos.botaprarodar.R.string.btn_register).uppercase()
        val btnRegister = (hasText(text) and hasClickAction())

        composeTestRule.onNode(btnRegister).assertIsEnabled()
    }

    fun isRegisterDisabled() {
        val text = getString(app.igormatos.botaprarodar.R.string.btn_register).uppercase()
        val btnRegister = (hasText(text) and hasClickAction())

        composeTestRule.onNode(btnRegister).assertIsDisplayed()
    }

    fun isEmailAlreadyRegisteredVisible() {

        //TODO should use Espresso idling resources in order to await the response for the service and be more assertive
        runBlocking { delay(5000) }

        val text = getString(app.igormatos.botaprarodar.R.string.email_already_registered_error)
        composeTestRule.onNodeWithText(text).assertIsDisplayed()
    }
}