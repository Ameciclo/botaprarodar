package app.igormatos.botaprarodar.presentation.returnbicycle.stepQuizReturnBike

import app.igormatos.botaprarodar.R
import org.junit.Assert
import org.junit.Test

class ReturnBikeQuizHelperTest {

    private val YES = "Sim"
    private val NO = "Não"
    private val INVALID_RADIO_BUTTON_ID = -1

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