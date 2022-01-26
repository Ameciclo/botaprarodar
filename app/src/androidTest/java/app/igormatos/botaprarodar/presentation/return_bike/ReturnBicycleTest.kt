package app.igormatos.botaprarodar.presentation.return_bike

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicyclePage
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
            ReturnBicyclePage(bikes = listOf(bike), handleClick = {})
        }
    }

    @Test
    fun shouldShowBackButtonOnTopBar() {
        composeTestRule.onNodeWithText(ReturnBicycleFixures.TOPBAR_BACK_BUTTON_TEXT).assertExists()
    }

    @Ignore("Reason: We're working on the implementation of this test")
    @Test
    fun shouldBackToHomeWhenClickInBackButton() {
        val backButton =
            composeTestRule.onNodeWithText(ReturnBicycleFixures.TOPBAR_BACK_BUTTON_TEXT)
        backButton.performClick()
        composeTestRule.onNodeWithText("Bota Pra Rodar").assertIsDisplayed()
    }

}