package app.igormatos.botaprarodar.home

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import app.igormatos.botaprarodar.presentation.main.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class MainActivityTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup(){
        scenario = launchActivity()
    }

    @Test
    fun showTripsScreen_whenClickInBottomMenuListIcon() {

    }
}