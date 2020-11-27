package app.igormatos.botaprarodar.accessapphome

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun selectCommunity(executeFun: AccessHomeRobot.() -> Unit) =
    AccessHomeRobot().apply { executeFun() }

class AccessHomeRobot : BaseRobot() {

    infix fun verify(executeFun: AccessHomeRobot.() -> Unit) {
        executeFun()
    }

    fun selectAnyCommunity() {
        selectAnyItemInRecyclerView(R.id.rvCommunityList)
    }

    fun checkIfIsHome() {
        checkViewById(R.id.main_container)
    }


}