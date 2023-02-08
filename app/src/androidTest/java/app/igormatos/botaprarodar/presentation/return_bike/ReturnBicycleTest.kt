package app.igormatos.botaprarodar.presentation.return_bike

import android.util.Log
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.igormatos.botaprarodar.Fixtures.bike
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.adapter.ReturnStepper
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicyclePage
import app.igormatos.botaprarodar.presentation.returnbicycle.ReturnBicycleViewModel
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
@ExperimentalCoroutinesApi
class ReturnBicycleTest {
    private lateinit var returnBicycleViewModel: ReturnBicycleViewModel

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        returnBicycleViewModel = ReturnBicycleViewModel(
            stepperAdapter = ReturnStepper(StepConfigType.SELECT_BIKE),
            stepOneReturnBikeUseCase = mockk(relaxed = true),
            stepFinalReturnBikeUseCase = mockk(relaxed = true),
            preferencesModule = mockk(relaxed = true),
            neighborhoodsUseCase = mockk(relaxed = true)
        )
        returnBicycleViewModel.setInitialStep()
        composeTestRule.setContent {
            ReturnBicyclePage(
                viewModel = returnBicycleViewModel,
                finish = { Log.i("backAction", "backAction executed") },
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

    @Test
    fun whenSelectABikeShouldGetToQuizStep() {
        returnBicycleViewModel.bikesAvailable.postValue(listOf(bike))
        val bikeButton =
            composeTestRule.onNodeWithText("name mock")
        Thread.sleep(3000)
        bikeButton.performClick()
        Thread.sleep(3000)
        composeTestRule.onNodeWithText("Responda ao questionário").assertIsDisplayed()
    }

}
