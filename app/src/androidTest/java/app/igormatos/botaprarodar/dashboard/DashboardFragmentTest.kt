package app.igormatos.botaprarodar.dashboard

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.igormatos.botaprarodar.accessapphome.selectCommunity
import app.igormatos.botaprarodar.authentication.login
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DashboardFragmentTest { // teste de snapshot

    private lateinit var scenario: ActivityScenario<WelcomeActivity>

    @Before
    fun setUp() {
        scenario = launchActivity()

        login {
            initAuthentication()
            fillUserField("brunotmg@gmail.com")
            clickNext()
            sleep(2000)
            fillPasswordField("abcd1234")
            clickSignIn()
            sleep(3000)
        }
        selectCommunity {
            selectAnyCommunity()
            sleep(3000)
        }
    }

    @Test
    fun `dashboardAccessUserJourney`() {
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