package app.igormatos.botaprarodar.presentation.splash

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.presentation.login.signin.LoginActivity
import app.igormatos.botaprarodar.presentation.login.selectCommunity.SelectCommunityActivity
import app.igormatos.botaprarodar.presentation.main.HomeActivity
import app.igormatos.botaprarodar.presentation.splash.SplashRobot.Companion.HOME
import app.igormatos.botaprarodar.presentation.splash.SplashRobot.Companion.LOGIN
import app.igormatos.botaprarodar.presentation.splash.SplashRobot.Companion.SELECT_COMMUNITY
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.ext.getFullName

@OptIn(ExperimentalComposeUiApi::class)
@RunWith(AndroidJUnit4::class)
internal class SplashActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
        stopKoin()
        unmockkAll()
    }

    @Test
    fun shouldNavigateToHomeActivity() {
        splash(HOME) {
            val result = Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())

            Intents.intending(IntentMatchers.toPackage("app.igormatos.botaprarodar.test")).respondWith(result)

            Intents.intended(hasComponent(HomeActivity::class.getFullName()))
        }
    }

    @Test
    fun shouldNavigateToSelectCommunityActivity() {
        splash(SELECT_COMMUNITY) {
            val result = Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())

            Intents.intending(IntentMatchers.toPackage("app.igormatos.botaprarodar.test")).respondWith(result)

            Intents.intended(hasComponent(SelectCommunityActivity::class.getFullName()))
        }
    }

    @Ignore("Should work after login refactor")
    @Test
    fun shouldNavigateToLoginActivity() {
        splash(LOGIN) {
            val result = Instrumentation.ActivityResult(Activity.RESULT_OK, Intent())

            Intents.intending(IntentMatchers.toPackage("app.igormatos.botaprarodar.test")).respondWith(result)

            Intents.intended(hasComponent(LoginActivity::class.getFullName()))
        }
    }
}

