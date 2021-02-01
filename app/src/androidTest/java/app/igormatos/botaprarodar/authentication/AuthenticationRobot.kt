package app.igormatos.botaprarodar.authentication

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R


fun login(executeFun: AuthenticationRobot.() -> Unit) = AuthenticationRobot().apply { executeFun() }

class AuthenticationRobot : BaseRobot() {

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
        val text = context.getString(R.string.admin_registration_error)
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

    fun checkCommunityScreen(): Boolean {
        return waitViewByResId("rvCommunityList")
    }

    fun successRegistrationDialog(): Boolean {
        return waitViewByResId("successRegistrationDialogContainer")
    }

    fun successfulLoginSteps(email: String, password: String){
        fillUserField(email)
        clickNext()
        showLoginScreen()
        fillPasswordField(password)
        clickSignIn()
    }

    fun initAuthentication(){
        clickButton(R.id.loginButton)
    }
}
