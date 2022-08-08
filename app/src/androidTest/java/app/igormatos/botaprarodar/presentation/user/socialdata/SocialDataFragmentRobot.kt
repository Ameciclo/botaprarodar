package app.igormatos.botaprarodar.presentation.user.socialdata

import androidx.test.espresso.Espresso.pressBack
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun socialDataFragment(executeFun: SocialDataFragmentRobot.() -> Unit) =
    SocialDataFragmentRobot().apply { executeFun() }

class SocialDataFragmentRobot : BaseRobot() {

    infix fun verify(executeFun: SocialDataFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun clickRacialEditText() {
        clickView(R.id.userRacialCst)
    }

    fun clickGenderEditText() {
        clickView(R.id.userGenderCst)
    }

    fun clickIncomeEditText() {
        clickAndScrollView(R.id.userIncomeCst)
    }

    fun clickSchoolingEditText() {
        clickAndScrollView(R.id.userSchoolingCst)
    }

    fun clickSchoolingStatusComplete() {
        clickAndScrollView(R.id.schoolingStatusComplete)
    }

    fun clickSchoolingStatusIncomplete() {
        clickAndScrollView(R.id.schoolingStatusIncomplete)
    }

    fun clickSchoolingStatusStudying() {
        clickAndScrollView(R.id.schoolingStatusStudying)
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

    fun clickOptionOnSchoolingDialog(atPosition: Int) {
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

    fun clickOptionOnSocialDataFragmentDialog(atPosition: Int) {
        clickAtPositionInList(atPosition)
    }

    fun clickPositiveButton() {
        clickButtonByText(context.resources.getString(R.string.ok))
    }

    fun clickBackButton() {
        pressBack()
    }

    fun verifyGenderEditTextIsEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.gender_options)
        checkMessage(options[atPosition])
    }

    fun verifySchoolingEditTextIsEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.schooling_options)
        checkMessage(options[atPosition])
    }

    fun verifyIncomeEditTextIsEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.income_options)
        checkMessage(options[atPosition])
    }

    fun verifyRacialEditTextIsEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.racial_options)
        checkMessage(options[atPosition])
    }

    fun verifySchoolingEditTextNotEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.schooling_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }

    fun verifyIncomeEditTextIsNotEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.income_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }

    fun verifyRacialEditTextIsNotEqualSelected(atPosition: Int) {
        val options = context.resources.getStringArray(R.array.racial_options)
        checkMessageIsNotDisplayed(options[atPosition])
    }
}
