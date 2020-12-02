package app.igormatos.botaprarodar

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.UiSelector

abstract class BaseRobot {

    fun clickButton(idButton: Int) {
        onView(withId(idButton)).perform(click())
    }

    fun clickButtonByText(text: String) {
        onView(withText(text)).perform(click())
    }

    fun fillFieldByHint(hint: String, content: String) {
        performTypeTextWithCloseSoftKeyboard(onView(withHint(hint)), content)
    }

    fun fillFieldById(resId: Int, content: String) {
        performTypeTextWithCloseSoftKeyboard(onView(withId(resId)), content)
    }

    fun findItemInRecyclerView(recyclerId: Int, withText: String) {
        onView(withId(recyclerId))
            .perform(
                actionOnItem<ViewHolder>(
                    hasDescendant(withText(withText)),
                    scrollTo()
                )
            )
    }

    fun clickItemInRecyclerView(resId: Int, content: String){
        onView(withId(resId))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click()))
    }

    fun selectAnyItemInRecyclerView(recyclerId: Int): ViewInteraction =
        onView(withId(recyclerId))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click())
            )

    fun checkMessage(message: String) {
        onView(withText(message)).check(matches(isDisplayed()))
    }

    fun checkMessageOnHint(hintMessage: String) {
        onView(withHint(hintMessage)).check(matches(isDisplayed()))
    }

    fun checkViewById(resId: Int) {
        onView(withId(resId)).check(matches(isDisplayed()))
    }

    fun sleep(times: Long) = apply {
        Thread.sleep(times)
    }

    fun pressBack() {
        onView(isRoot()).perform(ViewActions.pressBack())
    }

    fun swipeUp(containerId: Int) {
        onView(withId(containerId)).perform(swipeUp());
    }

    fun takePhoto() {
        val device: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.executeShellCommand("input keyevent 27")
        val doneSelector = UiSelector().description("Done")
        val doneButton = device.findObject(doneSelector)
        doneButton.click()
    }

    fun performTypeTextWithCloseSoftKeyboard(view: ViewInteraction, content: String) {
        view.perform(replaceText(content), closeSoftKeyboard())
    }

    fun hideKeyboard() {
        onView(isRoot()).perform(closeSoftKeyboard())
    }

    fun scrollToViewById(resId: Int) {
        onView(withId(resId)).perform(scrollTo())
    }

}