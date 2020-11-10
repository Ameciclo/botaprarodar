package app.igormatos.botaprarodar.login

import androidx.test.espresso.NoMatchingViewException
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun login(executeFun: LoginRobot.() -> Unit) = LoginRobot().apply{ executeFun() }

class LoginRobot : BaseRobot() {

    fun clickLogin() {
        clickButton(R.id.login_button)
    }

    fun clickSignIn() {
        clickButtonByText("SIGN IN")
    }

    fun clickNext() {
        clickButtonByText("Next")
    }

    fun clickRecoveryPassword() {
        clickButtonByText("Trouble signing in?")
    }

    fun fillUserField(user: String) {
        fillFieldByHint("Email", user)
    }

    fun fillPasswordField(password: String) {
        fillFieldByHint("Password", password)
    }

    fun doLogin(user: String, password: String) {
        clickLogin()
        fillUserField(user)
        clickNext()
        fillPasswordField(password)
        clickSignIn()
    }

    infix fun verify(executeFun: LoginRobot.() -> Unit) {
        executeFun()
    }

    fun checkMessageIncorrectPassword() {
        try {
            checkMessage("Incorrect password.")
        } catch (e: NoMatchingViewException) {
            checkMessage("An unknown error occurred.")
        }
    }

}