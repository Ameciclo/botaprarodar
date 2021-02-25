package app.igormatos.botaprarodar.bicycle

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.authentication.login
import app.igormatos.botaprarodar.createcommunity.addCommunity
import app.igormatos.botaprarodar.presentation.welcome.WelcomeActivity
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class BikeTest {
    private lateinit var scenario: ActivityScenario<WelcomeActivity>

    @Before
    fun setup() {
        scenario = launchActivity()
    }

    @Test
    fun shouldAddBicycle() {
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
        addCommunity {
            showCommunityScreen()
            selectCommunity() // No momento está clicando na posição 0 do RecyclerView
        }
        bicycle {
            clickBicycleNavigation()
            addBicycle()
            hideKeyboard()
            clickTakeBicyclePhoto()
            captureImage()
            sleep(2000)
            hideKeyboard()
            fillBicycleNumberSerie("123")
            fillBicycleName("Monaco")
            fillBicycleNumberOrder("098765")
            swipeOnAddBicycle()
            clickRegisterBicycle()
            sleep(2000)
        } verify {
            val successText = context.getString(R.string.bicycle_update_success)
            waitViewByText(successText)
        }
    }
}
