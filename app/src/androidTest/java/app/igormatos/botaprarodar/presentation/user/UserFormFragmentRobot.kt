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

    fun clickConfirmDialog() {
        val buttonText = context.resources.getString(R.string.camera_dialog_positive_button_text)
        clickButtonByText(buttonText)
    }

    fun fillUserDocNumber(docNumber: String) {
        swipeUp(R.id.scrollContainerUser)
        fillFieldById(R.id.ietCpf, docNumber)
    }

    fun fillUserPhone(phoneNumber: String) {
        swipeUp(R.id.scrollContainerUser)
        fillFieldById(R.id.ietTelephone, phoneNumber)
    }

    fun checkDocNumberMaxLength() {
        checkViewHasLength(R.id.ietCpf, R.integer.max_size_user_doc_number)
    }

    fun checkPhoneNumberMaxLength() {
        checkViewHasLength(R.id.ietTelephone, R.integer.max_size_user_phone_number)
    }

    fun clickRacialEditText() {
        clickButton(R.id.etRacial)
    }

    fun verifyRacialDialogIsShowing() {
        checkMessage(context.resources.getString(R.string.add_user_racial))
    }

    fun verifyRacialOptionIsShowing() {
        val options = context.resources.getStringArray(R.array.racial_options)
        for (option in options) {
            checkMessage(option)
        }
    }

    fun clickOptionOnRacialDialog(atPosition: Int){
        clickAtPositionInList(atPosition)
    }

    fun clickRacialPositiveButton() {
        clickButtonByText(context.resources.getString(R.string.ok))
    }

    fun verifyRacialEditTextIsEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.racial_options)
        checkMessage(options[atPosition])
    }
}