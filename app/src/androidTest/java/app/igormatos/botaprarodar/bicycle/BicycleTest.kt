package app.igormatos.botaprarodar.bicycle

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import app.igormatos.botaprarodar.createcommunity.addCommunity
import app.igormatos.botaprarodar.login.login
import app.igormatos.botaprarodar.screens.login.LoginActivity
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class BicycleTest {
    @get:Rule
    val loginActivityRule = ActivityTestRule(LoginActivity::class.java)
    private lateinit var loginActivity: LoginActivity

    @Before
    fun setUp() {
        loginActivity = loginActivityRule.activity
    }

    @Test
    fun shouldValidateFilledFieldsWhenAddBicycle() {
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
        }
        addCommunity {
            selectCommunity("a") // No momento está clicando na posição 0 do RecyclerView
        }
        bicycle {
            clickBicycleNavigation()
            addBicycle()
            fillBicycleNumberSerie("123")
            fillBicycleName("Monaco")
            fillBicycleNumberOrder("098765")
            clickRegisterBicycle()
        } verify {
            // Realiza a validação da mensagem no toast
            checkMessage("Preencha todos os campos")
        }
    }

    @Test
    fun shouldAddBicycle() {
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
        }
        addCommunity {
            selectCommunity("a") // No momento está clicando na posição 0 do RecyclerView
        }
        bicycle {
            clickBicycleNavigation()
            addBicycle()
            fillBicycleNumberSerie("123")
            fillBicycleName("Monaco")
            fillBicycleNumberOrder("098765")
            clickRegisterBicycle()
        }
    }
}