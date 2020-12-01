package app.igormatos.botaprarodar.dashboard

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import app.igormatos.botaprarodar.accessapphome.selectCommunity
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.screens.login.LoginActivity
import com.google.firebase.FirebaseApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DashboardFragmentTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), LoginActivity::class.java)

    private lateinit var loginActivity: ActivityScenario<LoginActivity>

    private lateinit var mContext: Context

    @Before
    fun setUp() {
        loginActivity = launchActivity(intent)
        loginActivity.onActivity {
            FirebaseApp.initializeApp(it)
            mContext = it
        }

        login {
            clickLogin()
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
            scrollToAvalabilityChart()
        } verify {
            verifyAvailabilityChart()
        }
    }

}