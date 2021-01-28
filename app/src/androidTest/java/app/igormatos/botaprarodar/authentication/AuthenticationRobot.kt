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

    // (UiAutomator) Initialize UiDevice instance
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val launcherPackage: String = context.packageName

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

    fun clickSaveButton() {
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

    fun showRegistrationScreen() {
        waitViewByResId("fragmentRegistrationContainer")
    }

    fun incorrectPasswordMessage(): Boolean? {
        val text = context.getString(R.string.sign_in_password_error)
        return waitViewByText(text)

    }

    fun incorrectRegistrationMessage(): Boolean? {
        val text = context.getString(R.string.login_error)
        return waitViewByText(text)
    }

    fun incorrectEmailPasswordResetMessage(): Boolean? {
        val text = context.getString(R.string.reset_password_email_sent_error)
        return waitViewByText(text)
    }

    fun correctEmailPasswordResetMessage(): Boolean? {
        val text = context.getString(R.string.reset_password_email_sent)
        return waitViewByText(text)
    }

    fun showPasswordRecoveryScreen(): Boolean {
        return waitViewByResId("passwordRecoveryContainer")
    }

    fun checkMainScreen(): Boolean {
        return waitViewByResId("activityMainContainer")
    }

    fun successRegistrationDialog(): Boolean {
        return waitViewByResId("successRegistrationDialogContainer")
    }


    private fun waitViewByResId(resId: String): Boolean {
        return device.wait(Until.hasObject(By.res(launcherPackage, resId)), 10000)
    }

    private fun waitViewByText(text: String) = device.wait(
        Until.hasObject(
            By.text(text)
        ), 10000
    )
}
