package app.igormatos.botaprarodar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import java.lang.Exception

abstract class BaseRobot {

    fun clickButton(idButton: Int) {
        onView(withId(idButton)).perform(click())
    }

    fun clickButtonByText(text: String) {
        onView(withText(text)).perform(click())
    }

    fun fillFieldByHint(hint: String, content: String) {
        onView(withHint(hint)).perform(replaceText(content))
    }

    fun fillFieldById(resId: Int, content: String) {
        onView(withId(resId)).perform(typeText(content), closeSoftKeyboard())
    }

    fun checkMessage(message: String) {
        onView(withText(message)).check(matches(isDisplayed()))
    }

    fun sleep(times: Long) = apply {
        Thread.sleep(times)
    }

}