package app.igormatos.botaprarodar.bicycle

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.MainActivity
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import org.hamcrest.core.AllOf.allOf

fun bicycle(executeFun: BicycleRobot.() -> Unit) = BicycleRobot().apply { executeFun() }

class BicycleRobot : BaseRobot() {

    fun clickBicycleNavigation() {
        clickButton(R.id.navigationBicycles)
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

    fun fillBicyclePhoto() {
        onView(withId(R.id.bikePhotoImageView)).perform()
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

    private fun createImageToActivityResult(): Instrumentation.ActivityResult? {
        val bundle = Bundle()
        bundle.putParcelable(
            REQUEST_PHOTO.toString(), BitmapFactory.decodeResource(
                IntentsTestRule(MainActivity::class.java).activity.resources,
                R.drawable.ic_check_dialog
            )
        )
        val resultData = Intent()
        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

}