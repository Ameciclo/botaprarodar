package app.igormatos.botaprarodar

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule

abstract class BaseAndroidComposeTest {
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
}