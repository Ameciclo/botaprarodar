package app.igormatos.botaprarodar.presentation.return_bike

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleActivity
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicyclePage
import io.mockk.internalSubstitute
import io.mockk.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class ReturnBicycleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ReturnBicyclePage(
                viewModel = viewModel(),
                navHostController = rememberNavController(),
                bikes = listOf(bike),
                handleClick = {},
                backAction = { Log.i("backAction", "backAction executed") }
            )
        }
    }

    @Test
    fun shouldShowBackButtonOnTopBar() {
        composeTestRule.onNodeWithText(ReturnBicycleFixures.TOPBAR_BACK_BUTTON_TITLE).assertExists()
    }

    @Ignore("Reason: Because we can't back to home in assert")
    @Test
    fun shouldBackToHomeWhenClickInBackButton() {
        val backButton =
            composeTestRule.onNodeWithText(ReturnBicycleFixures.TOPBAR_BACK_BUTTON_TITLE)
        backButton.performClick()
        composeTestRule.onNodeWithText(ReturnBicycleFixures.TOPBAR_HOME_TITLE).assertIsDisplayed()
    }

//    @Ignore("Reason: We'll implements this functions to click on the bicycle")
    @Test
    fun whenSelectABikeShouldGetToQuizStep() {
        val bikeButton =
            composeTestRule.onNodeWithText("name mock")
        bikeButton.performClick()
        composeTestRule.onNodeWithText("Responda ao questionário").assertIsDisplayed()
    }

}