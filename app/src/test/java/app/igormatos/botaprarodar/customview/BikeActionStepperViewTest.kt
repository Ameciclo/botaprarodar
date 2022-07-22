package app.igormatos.botaprarodar.customview

import android.app.Activity
import android.os.Build
import android.view.View
import android.widget.TextView
import app.igormatos.botaprarodar.common.customview.BikeActionStepperView
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.databinding.ItemStepperBinding
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.android.synthetic.main.item_stepper.view.*
import kotlinx.android.synthetic.main.layout_bike_action_stepper.view.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

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
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_DEVOLUTION)
        stepper.addItems(mock)
        assert(stepper.stepperContainer.childCount == mock.size)
    }

    @Test
    fun `stepper text should be the same as passed in stepConfigType enum when going next`() {
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_DEVOLUTION)
        stepper.addItems(mock)
        val stepInfoTxt = getStepInfoTxtComponent(mock.size -1)
        stepper.setCurrentStep(StepConfigType.CONFIRM_DEVOLUTION)
        assertEquals(
            stepInfoTxt.text,
            activity.getString(StepConfigType.CONFIRM_DEVOLUTION.title)
        )
    }

    @Test
    fun `Stepper text should be the same as passed in stepConfigType enum when go back to the previous`() {
        val mock = arrayListOf(StepConfigType.QUIZ, StepConfigType.CONFIRM_DEVOLUTION)
        stepper.addItems(mock)
        stepper.completeAllSteps()
        val stepInfoTxt = getStepInfoTxtComponent(0)
        stepper.setCurrentStep(StepConfigType.QUIZ)
        assertEquals(stepInfoTxt.text, activity.getString(StepConfigType.QUIZ.title))
    }

    @Test
    fun `Stepper text should be the same as previous when there is not next item`() {
        val mock = arrayListOf(StepConfigType.QUIZ)
        stepper.addItems(mock)

        val stepInfoTxt = getStepInfoTxtComponent(0)

        assertEquals(stepInfoTxt.text, activity.getString(mock.first().title))
        stepper.setCurrentStep(StepConfigType.QUIZ)
        assertEquals(stepInfoTxt.text, activity.getString(mock.first().title))
    }

    private fun getStepInfoTxtComponent(index:Int): TextView {
        val stepContainer = stepper.stepperContainer.getChildAt(index)
        return stepContainer.stepInfoTxt
    }

    @Test
    fun `Container Steps aren't should be show when there is not items`() {
        val mock = arrayListOf<StepConfigType>()
        stepper.addItems(mock)
        val stepContainer = stepper.stepperContainer.getChildAt(0)
        assertNull(stepContainer)
    }

    @Test
    fun `Check if last item does not have connector`() {
        val mock = arrayListOf(
            StepConfigType.SELECT_BIKE,
            StepConfigType.QUIZ,
            StepConfigType.CONFIRM_DEVOLUTION
        )
        stepper.addItems(mock)
        val lastPositionList = mock.size - 1
        val lastView = stepper.stepperContainer.getChildAt(lastPositionList)
        val lastViewBinding = ItemStepperBinding.bind(lastView)
        val connectorView = lastViewBinding.stepperConnector

        assert(connectorView.visibility == View.GONE)
    }
}
