package app.igormatos.botaprarodar.authentication

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R


fun login(executeFun: AuthenticationRobot.() -> Unit) = AuthenticationRobot().apply { executeFun() }

class AuthenticationRobot : BaseRobot() {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    // Initialize UiDevice instance
    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    val launcherPackage: String = context.packageName

    infix fun verify(executeFun: AuthenticationRobot.() -> Unit) {
        executeFun()
    }

    fun clickSignIn() {
        clickButton(R.id.sign_in)
    }

    fun clickNext() {
        clickButton(R.id.next)
    }

    fun clickRecoveryPassword() {
        clickButton(R.id.forgotten_password)
    }

    fun clickSendPasswordEmail() {
        clickButton(R.id.save)
    }

    fun fillUserField(user: String) {
        fillFieldById(R.id.username, user)
    }

    fun fillPasswordField(password: String) {
        fillFieldById(R.id.password, password)
    }

    fun showLoginScreen() {
        waitViewByResId("fragmentSignInContainer")
    }

    fun doLogin(user: String, password: String) {
        fillUserField(user)
        clickNext()
        fillPasswordField(password)
        clickSignIn()
    }


    fun incorrectPasswordMessage(): Boolean? {
        val text = context.getString(R.string.sign_in_password_error)
        return waitViewByText(text)

    }

    private fun waitViewByText(text: String) = device.wait(
        Until.hasObject(
            By.text(text)
        ), 10000
    )

    fun successRegistrationDialog(): Boolean {
        return waitViewByResId("successRegistrationDialogContainer")
    }

    fun checkEmailIncorrectMessage() {
        checkMessage(context.getString(R.string.login_error))
    }

    fun checkRegistrationScreen() {
        checkViewById(R.id.fragmentRegistrationContainer)
    }

    fun checkMainScreen(): Boolean {
        return waitViewByResId("activityMainContainer")
    }

    private fun waitViewByResId(resId: String): Boolean {
        return device.wait(Until.hasObject(By.res(launcherPackage, resId)), 10000)
    }

    fun checkSiginInScreen() {
        checkViewById(R.id.fragmentSignInContainer)
    }

    fun checkRegistrationForm() {
        checkViewById(R.id.password)
        checkViewById(R.id.username)
        checkViewById(R.id.save)
    }
}
