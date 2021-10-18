package app.igormatos.botaprarodar.accessapphome

import app.igormatos.botaprarodar.BaseRobot

fun accessHome(executeFun: AccessHomeRobot.() -> Unit) =
    AccessHomeRobot().apply { executeFun() }

class AccessHomeRobot : BaseRobot() {

    infix fun verify(executeFun: AccessHomeRobot.() -> Unit) {
        executeFun()
    }

    fun checkIfIsHome(): Boolean {
        return waitViewByResId("activityMainContainer")
    }

}
