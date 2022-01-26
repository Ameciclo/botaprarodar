package app.igormatos.botaprarodar.presentation.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.Fixtures.validUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState(),
                users = listOf(validUser),
                bikes = listOf(bike)
            )
        }
    }

    @Test
    fun shouldShowTopBar() {
        composeTestRule.onNodeWithText("Bota Pra Rodar").assertExists()
    }

    @Test
    fun shouldShowLogoutButton() {
        val logoutButton = composeTestRule.onNodeWithContentDescription("Logout")
        logoutButton.assertIsDisplayed()
    }

    @Test
    fun shouldShowHomeScreenWhenHomeBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Início").performClick()

        composeTestRule.onNodeWithText("Emprestar\nbicicleta".uppercase()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Devolver\nBicicleta".uppercase()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Cadastrar\nbicicleta".uppercase()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Cadastrar\nUsuária".uppercase()).assertIsDisplayed()

        composeTestRule.onNodeWithText("Total de bicicletas".uppercase()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Emprestadas".uppercase()).assertIsDisplayed()

        composeTestRule.onNodeWithText("Bicicletas disponíveis".uppercase()).assertIsDisplayed()
    }

    @Test
    fun shouldShowUsersScreenWhenUsersBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Usuárias").performClick()
        Thread.sleep(4000)
        composeTestRule.onNodeWithText("Capitão América").assertIsDisplayed()
    }

    @Test
    fun shouldShowBikesScreenWhenBikesBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Bicicletas").performClick()

        composeTestRule.onNodeWithText("name mock").assertIsDisplayed()
    }
}