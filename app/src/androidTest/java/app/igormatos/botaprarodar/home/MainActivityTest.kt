package app.igormatos.botaprarodar.home

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.MainActivity
import io.mockk.mockk
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = launchActivity()
    }

    @Test
    fun showTripsScreen_whenClickInBottomMenuListIcon() {
        val navController = mockk<NavController>()

        scenario.onActivity {
            Navigation.setViewNavController(it.container, navController)
        }

        onView(withId(R.id.navigationHome)).perform(click())

        onView(withId(R.id.listContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showUsersScreen_whenClickInBottomMenuListIcon() {
        val navController = mockk<NavController>()

        scenario.onActivity {
            Navigation.setViewNavController(it.container, navController)
        }

        onView(withId(R.id.navigationUsers)).perform(click())

        onView(withId(R.id.listContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
