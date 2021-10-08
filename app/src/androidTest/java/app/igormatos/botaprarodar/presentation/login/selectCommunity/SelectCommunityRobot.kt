package app.igormatos.botaprarodar.presentation.login.selectCommunity

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun selectCommunityActivity(executeFun: SelectCommunityRobot.() -> Unit) =
    SelectCommunityRobot().apply { executeFun() }

class SelectCommunityRobot : BaseRobot() {

    infix fun verify(executeFun: SelectCommunityRobot.() -> Unit) {
        executeFun()
    }

    fun clickBtnAddCommunity() {
        clickView(R.id.btnAddCommunity)
    }

    fun checkBtnAddCommunityIsVisible() {
        checkViewIsDisplayed(R.id.btnAddCommunity)
    }

    fun findItemOnRecyclerView(name: String) {
        findItemInRecyclerView(R.id.recyclerviewCommunity, name)
    }

    fun selectAnyCommunity() {
        waitViewByResId("recyclerviewCommunity")
        selectAnyItemInRecyclerView(R.id.recyclerviewCommunity)
    }

    fun checkBtnAddCommunityIsNotVisible() {
        checkViewIsNotDisplayed(R.id.btnAddCommunity)
    }

    fun checkNetworkErrorMessage() {
        checkMessage(context.getString(R.string.network_error_message))
    }

    fun checkUnknownErrorMessage() {
        checkMessage(context.getString(R.string.unkown_error))
    }

    fun checkContactAmecicloMessage() {
        checkMessage(context.getString(R.string.login_no_communities_allowed))
    }

}