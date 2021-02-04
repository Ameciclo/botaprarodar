package app.igormatos.botaprarodar.presentation.user

import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.presentation.adduser.getGenderId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GenderHelperTest {

    @Test
    fun `when 'getGenderId' with male radio id should return 0`() {
        val expected = 0
        val value = getGenderId(R.id.rbGenderMale)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with female radio id should return 1`() {
        val expected = 1
        val value = getGenderId(R.id.rbGenderFemale)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with others radio id should return 2`() {
        val expected = 2
        val value = getGenderId(R.id.rbGenderOther)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with no answer radio id should return 3`() {
        val expected = 3
        val value = getGenderId(R.id.rbGenderNoAnswer)
        assertEquals(expected, value)
    }

    @Test
    fun `when 'getGenderId' with male radio id should return incorrect value`() {
        val expected = 2
        val value = getGenderId(R.id.rbGenderMale)
        assertNotEquals(expected, value)
    }
}