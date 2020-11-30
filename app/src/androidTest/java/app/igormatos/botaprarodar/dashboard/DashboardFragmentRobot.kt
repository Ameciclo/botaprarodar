package app.igormatos.botaprarodar.dashboard

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.withId
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun dashboard(executeFun: DashboardFragmentRobot.() -> Unit) =
    DashboardFragmentRobot().apply { executeFun() }

class DashboardFragmentRobot : BaseRobot() {

    fun selectDashboardTab() {
        onView(withId(R.id.navigation_dashboard)).perform(click())
    }

    fun scrollToGenderProportionChart() {
        onView(withId(R.id.genderChart)).perform(scrollTo())
    }

    fun scrollToAvalabilityChart() {
        onView(withId(R.id.availableBikesChart)).perform(scrollTo())
    }

    infix fun verify(executeFun: DashboardFragmentRobot.() -> Unit) {
        executeFun()
    }

    fun verifyTravelsChart() {
        checkViewById(R.id.tripsChart)
    }

    fun verifyGenderProportionChart() {
        checkViewById(R.id.genderChart)
    }

    fun verifyAvailabilityChart() {
        checkViewById(R.id.availableBikesChart)
    }


}