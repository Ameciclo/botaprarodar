package app.igormatos.botaprarodar.home

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.MainActivity
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

    /*
    // TODO REMOVER DEPENDENCIAS DA API
    @Test
    fun showTripsScreen_whenClickInBottomMenuListIcon() {
        scenario.onActivity {
            val navController = NavController(it)
            Navigation.setViewNavController(it.activityMainContainer, navController)
        }
        onView(withId(R.id.navigationHome)).perform(click())

        onView(withId(R.id.listContainer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showUsersScreen_whenClickInBottomMenuListIcon() {

        scenario.onActivity {
            val navController = NavController(it)
            Navigation.setViewNavController(it.activityMainContainer, navController)
        }

        onView(withId(R.id.navigationUsers)).perform(click())

        onView(withId(R.id.cl_users)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun showBicyclesScreen_whenClickInBottomMenuListIcon() {
        scenario.onActivity {
            val navController = NavController(it)
            Navigation.setViewNavController(it.activityMainContainer, navController)
        }

        onView(withId(R.id.navigationBicycles)).perform(click())

        onView(withId(R.id.cl_bikes)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
     */
}
