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
        clickView(R.id.profileImageView)
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
        clickView(R.id.etRacial)
    }

    fun clickGenderEditText() {
        clickView(R.id.etGender)
    }

    fun clickIncomeEditText() {
        clickView(R.id.ietIncome)
    }

    fun clickResidenceProofImage() {
        clickAndScrollView(R.id.ivEditResidencePhoto)
    }

    fun clickDeleteButtonOnDialogImage() {
        clickView(R.id.submitButton)
    }

    fun clickSchoolingEditText() {
        clickView(R.id.etSchooling)
    }

    fun clickSchoolingStatusComplete() {
        clickView(R.id.schoolingStatusComplete)
    }

    fun clickSchoolingStatusIncomplete() {
        clickView(R.id.schoolingStatusIncomplete)
    }

    fun clickSchoolingStatusStudying() {
        clickView(R.id.schoolingStatusStudying)
    }

    fun verifySchoolingDialogIsShowing() {
        checkMessage(context.resources.getString(R.string.add_user_schooling))
    }

    fun verifySchoolingStatusComplete() {
        checkMessage(context.resources.getString(R.string.add_user_schooling_status_complete))
    }

    fun verifySchoolingStatusIncompleteg() {
        checkMessage(context.resources.getString(R.string.add_user_schooling_status_incomplete))
    }

    fun verifySchoolingStatusStudying() {
        checkMessage(context.resources.getString(R.string.add_user_schooling_status_studying))
    }

    fun verifyDialogEditResidenceImageIsShowing() {
        checkMessage(context.resources.getString(R.string.title_dialog_change_image))
    }

    fun verifyDialogDeleteResidenceImageIsShowing() {
        checkMessage(context.resources.getString(R.string.title_dialog_delete_image))
    }

    fun clickOptionOnSchoolingDialog(atPosition: Int){
        clickAtPositionInList(atPosition)
    }

    fun clickSchoolingPositiveButton() {
        clickButtonByText(context.resources.getString(R.string.ok))
    }

    fun verifySchoolingOptionIsShowing() {
        val options = context.resources.getStringArray(R.array.schooling_options)
        for (option in options) {
            checkMessage(option)
        }
    }

    fun verifyGenderDialogIsShowing() {
        checkMessage(context.resources.getString(R.string.add_user_gender))
    }

    fun verifyRacialDialogIsShowing() {
        checkMessage(context.resources.getString(R.string.add_user_racial))
    }

    fun verifyIncomeDialogIsShowing() {
        checkMessage(context.resources.getString(R.string.add_user_income))
    }

    fun verifyGenderOptionIsShowing() {
        val options = context.resources.getStringArray(R.array.gender_options)
        for (option in options) {
            checkMessage(option)
        }
    }
    fun verifyRacialOptionIsShowing() {
        val options = context.resources.getStringArray(R.array.racial_options)
        for (option in options) {
            checkMessage(option)
        }
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

    fun verifyGenderEditTextIsEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.gender_options)
        checkMessage(options[atPosition])
    }

    fun verifySchoolingEditTextIsEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.schooling_options)
        checkMessage(options[atPosition])
    }

    fun verifyIncomeEditTextIsEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.income_options)
        checkMessage(options[atPosition])
    }

    fun verifyRacialEditTextIsEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.racial_options)
        checkMessage(options[atPosition])
    }

    fun verifySchoolingEditTextNotEqualSelected(atPosition:Int) {
        val options = context.resources.getStringArray(R.array.schooling_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }

    fun verifyIncomeEditTextIsNotEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.income_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }

    fun verifyRacialEditTextIsNotEqualSelected(atPosition:Int){
        val options = context.resources.getStringArray(R.array.racial_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }
}