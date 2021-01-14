package app.igormatos.botaprarodar.createcommunity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.appendTimestamp
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.presentation.login.LoginActivity
import com.google.firebase.FirebaseApp
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddCommunityActivityTest {

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

        addCommunity {
            sleep(2000)
            saveCommunityWithNoData()
        } verify {
            checkMessage(mContext.getString(R.string.empties_fields_error))
        }

        addCommunity {
            fillCommunityDataWithWrongEmailFormat()
        } verify {
            checkMessage(mContext.getString(R.string.email_format_warning))
        }

    }

}