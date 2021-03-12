package app.igormatos.botaprarodar.customview

import android.app.Activity
import android.os.Build
import android.view.View
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.customview.BikeActionStepperView
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.layout_bike_action_stepper.view.*
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@Ignore
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class BikeActionStepperViewTest {

    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity
    private lateinit var stepper: BikeActionStepperView

    @Before
    fun setUp() {

        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()

        stepper = BikeActionStepperView(activity)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when addStepConfig items, child count should match with list size`() {
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_RETURN)
        stepper.addItems(mock)
        assert(stepper.stepperContainer.childCount == mock.size)
    }

    @Test
    fun `stepper title should be the same as passed in stepConfigType enum when going next`() {
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_RETURN)
        stepper.addItems(mock)

        stepper.setCurrentStep(StepConfigType.CONFIRM_RETURN)
        assertEquals(stepper.stepperTitle.text, activity.getString(StepConfigType.CONFIRM_RETURN.title))
    }

    @Test
    fun `stepper title should be the same as passed in stepConfigType enum when go back to the previous`() {
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_RETURN)
        stepper.addItems(mock)
        stepper.completeAllSteps()

        stepper.setCurrentStep(StepConfigType.QUIZ)
        assertEquals(stepper.stepperTitle.text, activity.getString(StepConfigType.QUIZ.title))
    }

    @Test
    fun `title text should be the same as previous if there is no next item`() {
        val mock = arrayListOf(StepConfigType.QUIZ)
        stepper.addItems(mock)

        assertEquals(stepper.stepperTitle.text, activity.getString(mock.first().title))
        stepper.setCurrentStep(StepConfigType.QUIZ)
        assertEquals(stepper.stepperTitle.text, activity.getString(mock.first().title))
    }

    @Test
    fun `text title should be empty when there is no items`() {
        val mock = arrayListOf<StepConfigType>()
        stepper.addItems(mock)
        assertEquals("", stepper.stepperTitle.text)
    }

    @Test
    fun `Check if last item does not have connector`() {
        val mock = arrayListOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.QUIZ,
            StepConfigType.CONFIRM_RETURN
        )
        stepper.addItems(mock)
        val lastPositionList = mock.size - 1
        val lastView = stepper.stepperContainer.getChildAt(lastPositionList)

        val connectorView = lastView.findViewById<View>(R.id.stepperConnector)
        assert(connectorView.visibility == View.GONE)
    }
}
