package app.igormatos.botaprarodar.main.users

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.Fixtures.user
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
        clickButton(R.id.btn_register_users)
    }

    fun checkUserFormFragmentIsVisible() {
        waitViewByResId(resId = "scrollContainerUser", timeout = 2000)
    }

    fun clickOnFirstUser() {
        selectAnyItemInRecyclerView(R.id.rv_users)
    }

    fun checkUserName() {
        findItemInRecyclerView(R.id.rv_users, user.name!!)
    }

    fun checkUserDate() {
        findItemInRecyclerView(R.id.rv_users, context.resources.getString(R.string.user_created_since, user.createdDate))
    }

}