package app.igormatos.botaprarodar.presentation.login

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun loginActivity(executeFun: LoginRobot.() -> Unit) = LoginRobot().apply { executeFun() }

class LoginRobot : BaseRobot() {

    infix fun verify(executeFun: LoginRobot.() -> Unit) {
        executeFun()
    }

    fun fillEmailField(email: String) {
        fillFieldById(R.id.ietEmail, email)
    }

    fun fillPasswordField(email: String) {
        fillFieldById(R.id.ietPassword, email)
    }

    fun checkBtnSignInIsEnable() {
        checkViewIsEnable(R.id.btnLogin)
    }

    fun checkBtnSignInIsNotEnable() {
        checkViewIsNotEnable(R.id.btnLogin)
    }

    fun clickBtnSignIn() {
        clickView(R.id.btnLogin)
    }

    fun checkInputEmailHasError() {
        checkInputLayoutHasErrorText(R.id.ilEmail, context.getString(R.string.sign_in_email_error))
    }

    fun checkInputPasswordHasError() {
        checkInputLayoutHasErrorText(
            R.id.ilPassword,
            context.getString(R.string.sign_in_password_error)
        )
    }

    fun checkNetworkErrorMessage() {
        checkMessage(context.getString(R.string.network_error_message))
    }

    fun checkUnknownErrorMessage() {
        checkMessage(context.getString(R.string.login_error))
    }

    fun checkUnverifiedEmailErrorMessage() {
        checkMessage(context.getString(R.string.login_confirm_email_error))
    }

    fun checkSelectCommunityActivityIsVisible() {
        waitViewByResId(resId = "selectCommunityBarLayout", timeout = 2000)
    }
}