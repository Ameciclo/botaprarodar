package app.igormatos.botaprarodar.createcommunity

import android.util.Log
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.screens.login.LoginActivity
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
    }

    @Test
    fun shouldAddNewCommunity_fillDataFields_clickAddCommunity() {
        login {
            clickLogin()
            fillUserField("brunotmg@gmail.com")
            clickNext()
            sleep(2000)
            fillPasswordField("abcd1234")
            clickSignIn()
            sleep(4000)
        }
        addCommunity {
            saveNewCommunity()
            sleep(2000)
        }.verify {
            checkMessage("Nome Org Teste")
        }
    }

    @After
    fun deleteTestCommunity() {
        Log.i("DELET", "DELET")
    }

}