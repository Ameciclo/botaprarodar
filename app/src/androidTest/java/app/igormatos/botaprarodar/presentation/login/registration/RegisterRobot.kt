package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun registerActivity(executeFun: RegisterRobot.() -> Unit) =
    RegisterRobot().apply { executeFun() }

class RegisterRobot : BaseRobot() {

    infix fun verify(executeFun: RegisterRobot.() -> Unit) {
        executeFun()
    }

    fun fillEmailField(email: String) {
        fillFieldById(R.id.ietEmail, email)
    }

    fun fillPasswordField(email: String) {
        fillFieldById(R.id.ietPassword, email)
    }

    fun fillConfirmPasswordField(email: String) {
        fillFieldById(R.id.ietConfirmPassword, email)
    }

    fun checkBtnSendIsEnable() {
        checkViewIsEnable(R.id.btnSend)
    }

    fun checkBtnSendIsNotEnable() {
        checkViewIsNotEnable(R.id.btnSend)
    }

    fun clickBtnSend() {
        clickView(R.id.btnSend)
    }

    fun checkSuccessMessage() {
        waitViewByText(context.getString(R.string.message_register_completed))
    }

    fun checkInputEmailHasError() {
        checkInputLayoutHasErrorText(
            R.id.ilEmail,
            context.getString(R.string.email_already_registered_error)
        )
    }

    fun checkNetworkErrorMessage() {
        checkMessage(context.getString(R.string.network_error_message))
    }

    fun checkUnknownErrorMessage() {
        checkMessage(context.getString(R.string.unkown_error))
    }
}