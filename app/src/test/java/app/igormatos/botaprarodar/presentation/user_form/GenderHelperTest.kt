package app.igormatos.botaprarodar.presentation.user_form

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.userForm.getGenderId
import app.igormatos.botaprarodar.presentation.userForm.getRadioButtonId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GenderHelperTest {

    private val MALE_ID = 0
    private val FEMALE_ID = 1
    private val OTHER_ID = 2
    private val NO_ANSWER = 3

    @Test
    fun `when 'getGenderId' with male radio id should return 0`() {
        val expected = MALE_ID
        val value = getGenderId(R.id.rbGenderMale)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with female radio id should return 1`() {
        val expected = FEMALE_ID
        val value = getGenderId(R.id.rbGenderFemale)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with others radio id should return 2`() {
        val expected = OTHER_ID
        val value = getGenderId(R.id.rbGenderOther)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with no answer radio id should return 3`() {
        val expected = NO_ANSWER
        val value = getGenderId(R.id.rbGenderNoAnswer)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with male radio id should return incorrect value`() {
        val expected = OTHER_ID
        val value = getGenderId(R.id.rbGenderMale)
        assertNotEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonId' with male id should return rbGenderMale`() {
        val expected = R.id.rbGenderMale
        val value = getRadioButtonId(MALE_ID)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonId' with female id should return rbGenderFemale`() {
        val expected = R.id.rbGenderFemale
        val value = getRadioButtonId(FEMALE_ID)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonId' with others id should return rbGenderOther`() {
        val expected = R.id.rbGenderOther
        val value = getRadioButtonId(OTHER_ID)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonId' with no answer id should return rbGenderNoAnswer`() {
        val expected = R.id.rbGenderNoAnswer
        val value = getRadioButtonId(NO_ANSWER)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getRadioButtonId' with male id should return incorrect value`() {
        val expected = R.id.rbGenderFemale
        val value = getGenderId(MALE_ID)
        assertNotEquals(expected, value)
    }
}