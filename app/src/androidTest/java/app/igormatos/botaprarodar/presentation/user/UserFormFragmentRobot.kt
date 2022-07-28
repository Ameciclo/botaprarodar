package app.igormatos.botaprarodar.presentation.user

import androidx.test.espresso.Espresso.pressBack
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun userFormFragment(executeFun: UserFormFragmentRobot.() -> Unit) =
    UserFormFragmentRobot().apply { executeFun() }

class UserFormFragmentRobot : BaseRobot() {

    infix fun verify(executeFun: UserFormFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun clickProfileImage() {
        clickAndScrollView(R.id.cpp_perfil_picture)
    }

    fun clickConfirmDialog() {
        val buttonText = context.resources.getString(R.string.camera_dialog_positive_button_text)
        clickButtonByText(buttonText)
    }

    fun fillUserPhone(phoneNumber: String) {
        fillFieldByHint(context.getString(R.string.user_form_user_phone_hint), phoneNumber)
    }

    fun checkPhoneNumberMaxLength() {
        checkViewHasLength(R.string.user_form_user_phone_hint, R.integer.max_size_user_phone_number)
    }

    fun clickOptionOnUserFormFragmentDialog(atPosition: Int){
        clickAtPositionInList(atPosition)
    }

    fun clickPositiveButton() {
        clickButtonByText(context.resources.getString(R.string.ok))
    }

    fun clickBackButton() {
        pressBack()
    }
}
