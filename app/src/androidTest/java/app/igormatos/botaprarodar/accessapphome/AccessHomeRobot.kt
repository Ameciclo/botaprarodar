package app.igormatos.botaprarodar.accessapphome

import app.igormatos.botaprarodar.BaseRobot

fun selectCommunity(executeFun: AccessHomeRobot.() -> Unit) = AccessHomeRobot().apply{ executeFun() }

class AccessHomeRobot : BaseRobot(){


}