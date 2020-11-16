package app.igormatos.botaprarodar.createcommunity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.screens.login.LoginActivity
import com.google.firebase.FirebaseApp
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddCommunityActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), LoginActivity::class.java)

    @get:Rule
    val loginActivityRule = activityScenarioRule<LoginActivity>(intent)

    private lateinit var loginActivity: ActivityScenario<LoginActivity>

    private lateinit var mContext: Context

    @Before
    fun setUp() {
        loginActivity = loginActivityRule.scenario
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
        val communityName = "Comunidade " + System.currentTimeMillis()
        addCommunity {
            saveNewCommunity(communityName)
            sleep(2000)
            checkTextInItemList(communityName)
        } verify {
            checkMessage(communityName)
        }
        addCommunity {
            sleep(2000)
            saveCommunityWithNoData()
        } verify {
            checkMessage(mContext.getString(R.string.empties_fields_error))
        }

    }

//    @Test
//    fun shouldAddNewCommunity_fillDataFields_clickAddCommunity() {
//        val communityName = "Comunidade " + System.currentTimeMillis()
//        login {
//            doLogin("brunotmg@gmail.com", "abcd1234")
//            sleep(3000)
//        }
//        addCommunity {
//            saveNewCommunity(name = communityName)
//            sleep(2000)
//        } verify {
//            checkTextInItemsList(communityName)
//            checkMessage("Bota pra Rodar")
//            checkMessage("ADICIONAR COMUNIDADE")
//        }
//    }

    @After
    fun deleteTestCommunity() {

    }

}