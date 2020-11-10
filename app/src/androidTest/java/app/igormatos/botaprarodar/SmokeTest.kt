package app.igormatos.botaprarodar

import android.view.View
import androidx.core.view.isVisible
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import app.igormatos.botaprarodar.screens.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SmokeTest {

    @get:Rule
    val loginActivityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var loginActivity: LoginActivity

    @Before
    fun setUp() {
        loginActivity = loginActivityRule.activity
    }

    @Test
    @LargeTest
    fun userJourney_loginToAddComunity() {
        try {
            onView(withId(R.id.loginButton)).perform(click())
            onView(withHint("Email")).perform(replaceText("teste"))
            onView(withText("Next")).perform(click())
            onView(withText("That email address isn't correct")).check(matches(isDisplayed()))
            onView(withHint("Email")).perform(replaceText("brunotmg@gmail.com"))
            onView(withText("Next")).perform(click())
            onView(isRoot()).perform(waitId(2000))
            onView(withHint("Password")).perform(replaceText("abcd1234"))
            onView(withText("SIGN IN")).perform(click())
            onView(isRoot()).perform(waitId(4000))
            onView(withText("Bota pra Rodar")).check(matches(isDisplayed()))
            onView(withText("ADICIONAR COMUNIDADE")).perform(click())
            onView(isRoot()).perform(waitId(2000))
            onView(withHint("Nome")).perform(replaceText("Nome teste"))
            onView(withHint("Descrição")).perform(replaceText("Descrição teste"))
            onView(withHint("Endereço")).perform(replaceText("Rua Teste, 123"))
            onView(withHint("Nome do responsável")).perform(replaceText("Responsável teste"))
            onView(withHint("Email do responsável")).perform(replaceText("teste@respnsavel.com"))
            pressBack()
            onView(isRoot()).perform(waitId(2000))
        } catch (exception: NoMatchingViewException){
            if (exception.message?.contains("loginButton")!!) {
                onView(isRoot()).perform(waitId(4000))
                onView(withText("Bota pra Rodar")).check(matches(isDisplayed()))
                onView(withText("ADICIONAR COMUNIDADE")).perform(click())
                onView(isRoot()).perform(waitId(2000))
                onView(withHint("Nome")).perform(replaceText("Nome teste"))
                onView(withHint("Descrição")).perform(replaceText("Descrição teste"))
                onView(withHint("Endereço")).perform(replaceText("Rua Teste, 123"))
                onView(withHint("Nome do responsável")).perform(replaceText("Responsável teste"))
                onView(withHint("Email do responsável")).perform(replaceText("teste@respnsavel.com"))
                pressBack()
                onView(isRoot()).perform(waitId(2000))
            } else {
                throw exception
            }
        }
    }

    fun waitId(delay: Long) : ViewAction {
        return object: ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Time delay: $delay"
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }

        }
    }

}