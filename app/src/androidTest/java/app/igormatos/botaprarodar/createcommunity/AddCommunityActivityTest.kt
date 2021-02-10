package app.igormatos.botaprarodar.createcommunity

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.NoMatchingViewException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.appendTimestamp
import app.igormatos.botaprarodar.authentication.login
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddCommunityActivityTest {

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
    }

    @Test
    @LargeTest
    fun shouldAddNewCommunityUserJourney() {
        val communityName = appendTimestamp("Comunidade")
        addCommunity {
            saveNewCommunity(communityName)
            sleep(2000)
            findItemOnRecyclerView(communityName)
        } verify {
            checkMessage(communityName)
        }
    }
}
