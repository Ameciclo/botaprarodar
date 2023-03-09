package app.igormatos.botaprarodar

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

abstract class BaseRobotScreen(
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>
) {

    protected fun getString(@StringRes resourceId: Int) =
        composeTestRule.activity.getString(resourceId)
}