package app.igormatos.botaprarodar.main.bicycles

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun bicyclesFragment(executeFun: BicyclesFragmentRobot.() -> Unit) =
    BicyclesFragmentRobot().apply { executeFun() }

class BicyclesFragmentRobot : BaseRobot() {

    infix fun verify(executeFun: BicyclesFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun checkTitleIsVisible() {
        checkViewIsDisplayed(R.id.tv_title_bikes)
    }

    fun checkButtonIsVisible() {
        checkViewIsDisplayed(R.id.btn_register_bikes)
    }

    fun checkRecyclerIsVisible() {
        checkViewIsDisplayed(R.id.rv_bikes)
    }

    fun clickRegisterBikesButton() {
        clickButton(R.id.btn_register_bikes)
    }

    fun checkBikeFormActivityIsVisible() {
        waitViewByResId(resId = "containerAddBike", timeout = 2000)
    }

    fun delay(delay: Long = 1000) {
        Thread.sleep(delay)
    }

    fun clickOnFirstBike() {
        selectAnyItemInRecyclerView(R.id.rv_bikes)
    }

    fun checkBikeName() {
        findItemInRecyclerView(R.id.rv_bikes, "name mock")
    }

    fun checkBikeOrderNumber() {
        findItemInRecyclerView(R.id.rv_bikes, "Ordem: 123")
    }

    fun checkBikeSerialNumber() {
        findItemInRecyclerView(R.id.rv_bikes, "Série: serial mock")
    }
}