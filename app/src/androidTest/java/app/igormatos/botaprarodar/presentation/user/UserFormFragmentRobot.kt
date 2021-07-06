package app.igormatos.botaprarodar.presentation.user

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R


fun userFormFragment(executeFun: UserFormFragmentRobot.() -> Unit) =
    UserFormFragmentRobot().apply { executeFun() }

class UserFormFragmentRobot : BaseRobot() {

    infix fun verify(executeFun: UserFormFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun clickProfileImage() {
        clickButton(R.id.profileImageView)
    }

    fun clickConfirm() {
        val buttonText = context.resources.getString(R.string.camera_dialog_positive_button_text)
        clickButtonByText(buttonText)
    }
}