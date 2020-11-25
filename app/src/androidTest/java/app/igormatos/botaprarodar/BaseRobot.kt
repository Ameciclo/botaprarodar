package app.igormatos.botaprarodar

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.*
import app.igormatos.botaprarodar.network.Community
import app.igormatos.botaprarodar.screens.login.CommunityAdapter
import app.igormatos.botaprarodar.screens.login.CommunityAdapter.CommunityViewHolder

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

    fun findStringInRecyclerView(resId: Int, content: String) {
        onView(withId(resId))
            .perform(
                actionOnItem<ViewHolder>(hasDescendant(withText(content)),
                    ViewActions.scrollTo()
                )
            )
    }

    fun checkMessage(message: String) {
        onView(withText(message)).check(matches(isDisplayed()))
    }

    fun checkMessageOnHint(hintMessage: String) {
        onView(withHint(hintMessage)).check(matches(isDisplayed()))
    }

    fun sleep(times: Long) = apply {
        Thread.sleep(times)
    }

    fun pressBack(){
        onView(isRoot()).perform(ViewActions.pressBack())
    }

    fun performTypeTextWithCloseSoftKeyboard(view: ViewInteraction, content: String) {
        view.perform(replaceText(content), closeSoftKeyboard())
    }

}