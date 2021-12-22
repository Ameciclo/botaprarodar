package app.igormatos.botaprarodar.main.users

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.Fixtures.validUser
import app.igormatos.botaprarodar.R

fun usersFragment(executeFun: UsersFragmentRobot.() -> Unit) =
    UsersFragmentRobot().apply { executeFun() }

class UsersFragmentRobot : BaseRobot() {

    infix fun verify(executeFun: UsersFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun checkTitleIsVisible() {
        checkViewIsDisplayed(R.id.tv_title_users)
    }

    fun checkSaveButtonIsEnable() {
        checkViewIsEnable(R.id.saveButton)
    }

    fun checkRecyclerIsVisible() {
        checkViewIsDisplayed(R.id.rv_users)
    }

    fun clickRegisterUserButton() {
        clickView(R.id.btn_register_users)
    }

    fun checkUserFormFragmentIsVisible() {
        waitViewByResId(resId = "scrollContainerUser", timeout = 2000)
    }

    fun clickOnFirstUser() {
        selectAnyItemInRecyclerView(R.id.rv_users)
    }

    fun checkUserName() {
        findItemInRecyclerView(R.id.rv_users, validUser.name!!)
    }

    fun checkUserPhoneNumber() {
        findItemInRecyclerView(
            R.id.rv_users,
            context.resources.getString(R.string.user_phone_number, validUser.telephoneHide4Chars())
        )
    }

    fun checkUserIconBlockedIsVisible(position: Int) {
        findItemInRecyclerViewAndVerifyUserIconBlockedIsVisible(R.id.rv_users, position)
    }

    fun checkUserIconBlockedNotIsVisible(position: Int) {
        findItemInRecyclerViewAndVerifyUserIconBlockedNotIsVisible(R.id.rv_users, position)
    }

}