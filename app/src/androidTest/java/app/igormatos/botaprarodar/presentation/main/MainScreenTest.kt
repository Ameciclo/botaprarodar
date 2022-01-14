package app.igormatos.botaprarodar.presentation.main

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Ignore
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
                users = emptyList(),
                bikes = emptyList()
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
    fun shouldShowActivitiesScreenWhenActivitiesBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Atividades").performClick()

        composeTestRule.onNodeWithText("Histórico de Atividades").assertIsDisplayed()
    }

    @Test
    fun shouldShowUsersScreenWhenUsersBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Usuárias").performClick()

        composeTestRule.onNodeWithText("Users").assertIsDisplayed()
    }

    @Test
    fun shouldShowBikesScreenWhenBikesBottomBarItemClicked() {
        composeTestRule.onNodeWithText("Bicicletas").performClick()

        composeTestRule.onNodeWithText("Bikes").assertIsDisplayed()
    }
}