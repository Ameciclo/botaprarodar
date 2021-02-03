package app.igormatos.botaprarodar.home

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
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

        // Create a TestNavHostController
        val navController = NavController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.main_nav_graph)
        scenario.onActivity{
        // Set the NavController property on the fragment
            Navigation.setViewNavController(it.container, navController)

        }


        onView(withId(R.id.navigationHome)).perform(click())

        assertEquals(navController.currentDestination?.id, R.id.navigationHome)
    }
}