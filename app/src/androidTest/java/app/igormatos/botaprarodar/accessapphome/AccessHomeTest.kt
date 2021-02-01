package app.igormatos.botaprarodar.accessapphome

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.authentication.login
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessHomeTest {
    private lateinit var scenario: ActivityScenario<WelcomeActivity>

    @Before
    fun setup() {
        scenario = launchActivity()
    }

    @Test
    fun access_home_as_admin() {
        login {
            initAuthentication()
            successfulLoginSteps("brunotmg@gmail.com", "abcd1234")
        }
        selectCommunity {
            showCommunityList()
            selectAnyCommunity()
        } verify {
            assertThat(checkIfIsHome()).isTrue()
        }
    }
}
