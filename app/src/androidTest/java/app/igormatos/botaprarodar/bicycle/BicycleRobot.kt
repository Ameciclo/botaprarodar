package app.igormatos.botaprarodar.bicycle

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormActivity
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import org.hamcrest.Matcher
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