package app.igormatos.botaprarodar.dashboard

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.NoMatchingViewException
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.accessapphome.selectCommunity
import app.igormatos.botaprarodar.authentication.login
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    private lateinit var scenario: ActivityScenario<WelcomeActivity>

    @Before
    fun setUp() {
        scenario = launchActivity()

        login {
            try {
                logout()
            } catch (e: NoMatchingViewException) {
                // skip
            } finally {
                initAuthentication()
                successfulLoginSteps("brunotmg@gmail.com", "abcd1234")
            }
        }
        selectCommunity {
            selectAnyCommunity()
            sleep(3000)
        }
    }

    @Test
    fun dashboardAccessUserJourney() {
        dashboard {
            selectDashboardTab()
        } verify {
            verifyTravelsChart()
        }
        dashboard {
            scrollToGenderProportionChart()
        } verify {
            verifyGenderProportionChart()
        }
        dashboard {
            scrollToAvailabilityChart()
        } verify {
            verifyAvailabilityChart()
        }
    }

}