package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun passwordRecoveryActivity(executeFun: PasswordRecoveryRobot.() -> Unit) =
    PasswordRecoveryRobot().apply { executeFun() }

class PasswordRecoveryRobot : BaseRobot() {

    infix fun verify(executeFun: PasswordRecoveryRobot.() -> Unit) {
        executeFun()
    }

    fun fillEmailField(email: String) {
        fillFieldById(R.id.ietEmail, email)
    }

    fun checkBtnSendIsEnable() {
        checkViewIsEnable(R.id.btnSend)
    }

    fun checkBtnSendIsNotEnable() {
        checkViewIsNotEnable(R.id.btnSend)
    }

    fun clickBtnSend() {
        clickButton(R.id.btnSend)
    }

    fun checkSuccessMessage() {
        waitViewByText(context.getString(R.string.message_password_recovery_link_send))
    }

    fun checkInputEmailHasError() {
        checkInputLayoutHasErrorText(R.id.ilEmail, context.getString(R.string.sign_in_email_error))
    }

    fun checkNetworkErrorMessage() {
        checkMessage(context.getString(R.string.network_error_message))
    }

    fun checkUnknownErrorMessage() {
        checkMessage(context.getString(R.string.unkown_error))
    }
}