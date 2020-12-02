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
        clickButton(R.id.navigation_dashboard)
    }

    fun scrollToGenderProportionChart() {
        scrollToViewById(R.id.genderChart)
    }

    fun scrollToAvalabilityChart() {
        scrollToViewById(R.id.availableBikesChart)
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