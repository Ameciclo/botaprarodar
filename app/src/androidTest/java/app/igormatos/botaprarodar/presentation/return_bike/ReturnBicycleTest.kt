package app.igormatos.botaprarodar.presentation.return_bike

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicyclePage
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReturnBicycleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            ReturnBicyclePage(bikes = emptyList(), handleClick = {})
        }
    }

    @Test
    fun shouldShowBackButtonOnTopBar() {
        composeTestRule.onNodeWithText("VOLTAR").assertExists()
    }

}