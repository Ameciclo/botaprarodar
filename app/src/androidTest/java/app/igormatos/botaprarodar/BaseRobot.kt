package app.igormatos.botaprarodar

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.brunotmgomes.ui.extensions.REQUEST_PHOTO
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

abstract class BaseRobot {
    val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    // (UiAutomator) Initialize UiDevice instance
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    private val launcherPackage: String = context.packageName

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

    fun selectAnyItemInRecyclerView(recyclerId: Int): ViewInteraction =
        onView(withId(recyclerId))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(0, click())
            )

    fun checkMessage(message: String) {
        onView(withText(message)).check(matches(isDisplayed()))
    }

    fun checkViewIsDisplayed(resId: Int) {
        onView(withId(resId)).check(matches(isDisplayed()))
    }

    fun checkViewIsNotDisplayed(resId: Int) {
        onView(withId(resId)).check(matches(not(isDisplayed())))
    }

    fun checkViewIsEnable(resId: Int) {
        onView(withId(resId)).check(matches(isEnabled()))
    }

    fun checkViewIsNotEnable(resId: Int) {
        onView(withId(resId)).check(matches(not(isEnabled())))
    }

    fun sleep(times: Long) = apply {
        Thread.sleep(times)
    }

    fun swipeUp(containerId: Int) {
        onView(withId(containerId)).perform(swipeUp());
    }

    fun captureImage() {
        device.pressKeyCode(KeyEvent.KEYCODE_CAMERA)
        device.pressKeyCode(KeyEvent.KEYCODE_BACK)
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

    fun waitViewByText(text: String, timeout: Long = 10000): Boolean = device.wait(
        Until.hasObject(
            By.text(text)
        ), timeout
    )

    fun checkInputLayoutHasErrorText(resId: Int, content: String) {
        onView(withId(resId)).check(matches(hasTextInputLayoutErrorText(content)))
    }

    private fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> =
        object : TypeSafeMatcher<View>() {

            override fun describeTo(description: Description?) {}

            override fun matchesSafely(item: View?): Boolean {
                if (item !is TextInputLayout) return false
                val error = item.error ?: return false
                val hint = error.toString()
                return expectedErrorText == hint
            }
        }

    fun waitViewByResId(resId: String, isGone: Boolean = false, timeout: Long = 10000): Boolean {
        if (isGone)
            return device.wait(Until.gone(By.res(launcherPackage, resId)), timeout)

        return device.wait(Until.hasObject(By.res(launcherPackage, resId)), timeout)
    }

    fun simulateCallbackFromCameraIntent(
        intentsTestRule: IntentsTestRule<*>,
        clickTakePhoto: () -> Unit
    ) {
        val activityResult = createImageToActivityResult(intentsTestRule)
        val expectedIntent: Matcher<Intent> = hasAction(MediaStore.ACTION_IMAGE_CAPTURE)
        intending(expectedIntent).respondWith(activityResult)

        clickTakePhoto()
        intending(expectedIntent)
    }

    fun createImageToActivityResult(intentsTestRule: IntentsTestRule<*>): Instrumentation.ActivityResult? {
        val bundle = Bundle()
        bundle.putParcelable(
            REQUEST_PHOTO.toString(), BitmapFactory.decodeResource(
                intentsTestRule.activity.resources,
                R.drawable.ic_check_dialog
            )
        )
        val resultData = Intent()
        resultData.putExtras(bundle)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }
}
