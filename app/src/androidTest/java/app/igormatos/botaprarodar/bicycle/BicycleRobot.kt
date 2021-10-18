package app.igormatos.botaprarodar.bicycle

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R
import org.hamcrest.core.AllOf.allOf

fun bicycle(executeFun: BicycleRobot.() -> Unit) = BicycleRobot().apply { executeFun() }

class BicycleRobot : BaseRobot() {

    fun clickBicycleNavigation() {
        clickView(R.id.navigationBicycles)
    }

    fun addBicycle() {
        onView(allOf(withId(R.id.btn_register_bikes), isDisplayed())).perform(click())
    }

    fun fillBicycleNumberSerie(content: String) {
        fillFieldByHint("Número de série da bicicleta", content)
    }

    fun fillBicycleName(content: String) {
        fillFieldByHint("Nome da bicicleta", content)
    }

    fun fillBicycleNumberOrder(content: String) {
        fillFieldByHint("Número de ordem", content)
    }

    fun clickRegisterBicycle() {
        onView(withId(R.id.saveButton)).perform(click())
    }

    fun clickTakeBicyclePhoto() {
        onView(withId(R.id.bikePhotoImageView)).perform(click())
    }

    fun swipeOnAddBicycle() {
        swipeUp(R.id.containerAddBike)
    }

    infix fun verify(executeFun: BicycleRobot.() -> Unit) {
        executeFun()
    }

}