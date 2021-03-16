package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import app.igormatos.botaprarodar.R
import org.junit.Assert
import org.junit.Test

class ReturnBikeQuizHelperTest {

    private val WORK_REASON = "Seu local de trabalho"
    private val STUDY_REASON = "Seu local de estudo"
    private val RECREATION_REASON = "Seu local de lazer / convivência social"
    private val SHOPPING_REASON = "Seu local de compras"
    private val CHILD_REASON = "Local de estudo da criança"
    private val ANOTHER_REASON = "Outro motivo não especificado"
    private val YES = "Sim"
    private val NO = "Não"
    private val INVALID_RADIO_BUTTON_ID = -1

    @Test
    fun `when 'getReasonByRadioButton' with work reason radio id should return WORK_REASON`() {
        val expected = WORK_REASON
        val value = getReasonByRadioButton(R.id.workplaceRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getReasonByRadioButton' with study reason radio id should return STUDY_REASON`() {
        val expected = STUDY_REASON
        val value = getReasonByRadioButton(R.id.studyRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getReasonByRadioButton' with recreation reason radio id should return RECREATION_REASON`() {
        val expected = RECREATION_REASON
        val value = getReasonByRadioButton(R.id.entertainmentRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getReasonByRadioButton' with shopping reason radio id should return SHOPPING_REASON`() {
        val expected = SHOPPING_REASON
        val value = getReasonByRadioButton(R.id.shoppingRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getReasonByRadioButton' with child reason radio id should return CHILD_REASON`() {
        val expected = CHILD_REASON
        val value = getReasonByRadioButton(R.id.childStudyRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getReasonByRadioButton' with another reason radio id should return ANOTHER_REASON`() {
        val expected = ANOTHER_REASON
        val value = getReasonByRadioButton(R.id.otherRb)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getYesOrNoByRadioButton' with problems during riding radio id should return YES`() {
        val expected = YES
        val value = getYesOrNoByRadioButton(R.id.problemsDuringRidingYes)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getYesOrNoByRadioButton' with no problems during riding radio id should return YES`() {
        val expected = NO
        val value = getYesOrNoByRadioButton(R.id.problemsDuringRidingNo)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getYesOrNoByRadioButton' with need take ride radio id should return YES`() {
        val expected = YES
        val value = getYesOrNoByRadioButton(R.id.needTakeRideYes)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getYesOrNoByRadioButton' with no need take ride radio id should return YES`() {
        val expected = NO
        val value = getYesOrNoByRadioButton(R.id.needTakeRideNo)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with work reason should return workplaceRb id`() {
        val expected = R.id.workplaceRb
        val value = getRadioButtonIdByReason(WORK_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with study reason should return workplaceRb id`() {
        val expected = R.id.studyRb
        val value = getRadioButtonIdByReason(STUDY_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with recreation reason should return entertainmentRb id`() {
        val expected = R.id.entertainmentRb
        val value = getRadioButtonIdByReason(RECREATION_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with shopping reason should return shoppingRb id`() {
        val expected = R.id.shoppingRb
        val value = getRadioButtonIdByReason(SHOPPING_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with child reason should return childStudyRb id`() {
        val expected = R.id.childStudyRb
        val value = getRadioButtonIdByReason(CHILD_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with another reason should return otherRb id`() {
        val expected = R.id.otherRb
        val value = getRadioButtonIdByReason(ANOTHER_REASON)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByReason' with invalid reason should return INVALID_ID`() {
        val expected = INVALID_RADIO_BUTTON_ID
        val value = getRadioButtonIdByReason("")
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdBySufferedViolence' with YES value should return problemsDuringRidingYes id`() {
        val expected = R.id.problemsDuringRidingYes
        val value = getRadioButtonIdBySufferedViolence(YES)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdBySufferedViolence' with NO value should return problemsDuringRidingNo id`() {
        val expected = R.id.problemsDuringRidingNo
        val value = getRadioButtonIdBySufferedViolence(NO)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdBySufferedViolence' with invalid value should return INVALID_ID`() {
        val expected = INVALID_RADIO_BUTTON_ID
        val value = getRadioButtonIdBySufferedViolence("")
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByGiveRide' with YES value should return needTakeRideYes id`() {
        val expected = R.id.needTakeRideYes
        val value = getRadioButtonIdByGiveRide(YES)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByGiveRide' with NO value should return needTakeRideNo id`() {
        val expected = R.id.needTakeRideNo
        val value = getRadioButtonIdByGiveRide(NO)
        Assert.assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonIdByGiveRide' with invalid value should return INVALID_ID`() {
        val expected = INVALID_RADIO_BUTTON_ID
        val value = getRadioButtonIdByGiveRide("")
        Assert.assertEquals(expected, value)
    }
}