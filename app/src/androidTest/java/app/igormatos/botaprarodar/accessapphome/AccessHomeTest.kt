package app.igormatos.botaprarodar.accessapphome

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import com.google.firebase.FirebaseApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessHomeTest {

    private val intent =
        Intent(ApplicationProvider.getApplicationContext(), LoginActivity::class.java)
    private lateinit var loginActivity: ActivityScenario<LoginActivity>
    private lateinit var mContext: Context

    @Before
    fun setUp() {
        loginActivity = launchActivity(intent)
        loginActivity.onActivity {
            FirebaseApp.initializeApp(it)
            mContext = it
        }
    }

    @Test
    fun access_home_as_admin() {
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
            sleep(3000)
        }
        selectCommunity {
            selectAnyCommunity()
            sleep(3000)
        } verify {
            checkIfIsHome()
        }
    }
}
