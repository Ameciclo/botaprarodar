package app.igormatos.botaprarodar.common.extensions

import androidx.core.util.PatternsCompat
import com.brunotmgomes.ui.extensions.*
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert.*
import org.junit.Test

class CharSequenceExtensionTest {

    @Test
    fun `when string is invalid email 'isValidEmail' should return false`() {
        val string = "emailInvalido"
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(string).matches() } returns false
        assertFalse(string.isValidEmail())
    }

    @Test
    fun `when string is valid email 'isValidEmail' should return true`() {
        val string = "email@valido.com"
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(string).matches() } returns true
        assertTrue(string.isValidEmail())
    }

    @Test
    fun `when string is null 'isValidEmail' should return false`() {
        val string: String? = null
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(string).matches() } returns false
        assertFalse(string.isValidEmail())
    }

    @Test
    fun `when string is empty 'isValidEmail' should return false`() {
        val string = ""
        mockkObject(PatternsCompat.EMAIL_ADDRESS)
        every { PatternsCompat.EMAIL_ADDRESS.matcher(string).matches() } returns false

        assertFalse(string.isValidEmail())
    }

    @Test
    fun `when string is invalid password 'isValidPassword' should return false`() {
        val string = "snh"
        assertFalse(string.isValidPassword())
    }

    @Test
    fun `when string is valid password 'isValidPassword' should return true`() {
        val string = "SenhaValida123"
        assertTrue(string.isValidPassword())
    }

    @Test
    fun `when string is null 'isValidPassword' should return false`() {
        val string: String? = null
        assertFalse(string.isValidPassword())
    }


    @Test
    fun `when string is empty 'isValidPassword' should return false`() {
        val string = ""
        assertFalse(string.isValidPassword())
    }

    @Test
    fun `when string is null 'transformNullToEmpty' should return empty`() {
        val string: String? = null
        assertEquals(string.transformNullToEmpty(), "")
    }

    @Test
    fun `when string is some text 'transformNullToEmpty' should return the text`() {
        val string = "Some Text"
        assertEquals(string.transformNullToEmpty(), string)
    }

    @Test
    fun `when string is null 'isNotNullOrNotEmpty' should return the false`() {
        val string: String? = null
        assertFalse(string.isNotNullOrNotBlank())
    }

    @Test
    fun `when string is notNull and not empty 'isNotNullOrNotEmpty' should return the true`() {
        val string = "NotNull NotEmpty"
        assertTrue(string.isNotNullOrNotBlank())
    }

    @Test
    fun `when string is empty 'isNotNullOrNotEmpty' should return the false`() {
        val string = ""
        assertFalse(string.isNotNullOrNotBlank())
    }

    @Test
    fun `when string is null 'isValidTelephone' should return the false`() {
        val string: String? = null
        assertFalse(string.isValidTelephone())
    }

    @Test
    fun `when string is 12 char number 'isValidTelephone' should return the true`() {
        val string = "61 3333-9898"
        assertTrue(string.isValidTelephone())
    }

    @Test
    fun `when string is 13 char number 'isValidTelephone' should return the true`() {
        val string = "61 99999-9999"
        assertTrue(string.isValidTelephone())
    }

    @Test
    fun `when string is less than 12 char number 'isValidTelephone' should return the false`() {
        val string = "61 9909-999"
        assertFalse(string.isValidTelephone())
    }

    @Test
    fun `when string is empty 'isValidTelephone' should return the false`() {
        val string = ""
        val result = string.isValidTelephone()
        assertFalse(result)
    }

    @Test
    fun `when string is longer than 13 char number 'isValidTelephone' should return the false`() {
        val string = "+55 61 99999-9999"
        val result = string.isValidTelephone()
        assertFalse(result)
    }
}