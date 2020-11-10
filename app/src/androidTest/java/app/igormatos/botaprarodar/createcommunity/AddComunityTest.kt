package app.igormatos.botaprarodar.createcommunity

import android.util.Log
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.screens.login.LoginActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class AddComunityTest {

    @get:Rule
    val loginActivityRule = ActivityTestRule(LoginActivity::class.java)

    private lateinit var loginActivity: LoginActivity

    @Before
    fun setUp() {
        loginActivity = loginActivityRule.activity
        FirebaseApp.initializeApp(loginActivity)
    }

    @Test
    fun shouldAddNewCommunity_fillDataFields_clickAddCommunity() {
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
            sleep(3000)
        }
        addCommunity {
            saveNewCommunity()
            sleep(2000)
        } verify {
            checkMessage("Nome Teste")
        }
    }

    @Test
    fun shouldVerifyRequiredField_whenClickAddCommunity() {
        addCommunity {
            sleep(2000)
            saveCommunityWithNoData()
        } verify {
            checkMessage(loginActivity.getString(R.string.empties_fields_error))
        }
    }

    @After
    fun deleteTestCommunity() {
        Log.i("DELETE", "DELETE")
    }

}