package app.igormatos.botaprarodar.common.extensions

import com.brunotmgomes.ui.extensions.*
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class CharSequenceExtensionTest {

    @Ignore
    @Test
    fun `when string is invalid email 'isValidEmail' should return false`() {
        val string = "emailInvalido"
        Assert.assertFalse(string.isValidEmail())
    }

    @Ignore
    @Test
    fun `when string is valid email 'isValidEmail' should return true`() {
        val string = "email@valido.com"
        Assert.assertTrue(string.isValidEmail())
    }

    @Ignore
    @Test
    fun `when string is null 'isValidEmail' should return false`() {
        val string : String? = null
        Assert.assertFalse(string.isValidEmail())
    }

    @Ignore
    @Test
    fun `when string is empty 'isValidEmail' should return false`() {
        val string  = ""
        Assert.assertFalse(string.isValidEmail())
    }

    @Test
    fun `when string is invalid password 'isValidPassword' should return false`() {
        val string = "snh"
        Assert.assertFalse(string.isValidPassword())
    }

    @Test
    fun `when string is valid password 'isValidPassword' should return true`() {
        val string = "SenhaValida123"
        Assert.assertTrue(string.isValidPassword())
    }

    @Test
    fun `when string is null 'isValidPassword' should return false`() {
        val string : String? = null
        Assert.assertFalse(string.isValidPassword())
    }


    @Test
    fun `when string is empty 'isValidPassword' should return false`() {
        val string = ""
        Assert.assertFalse(string.isValidPassword())
    }

    @Test
    fun `when string is null 'transformNullToEmpty' should return empty`() {
        val string : String? = null
        Assert.assertEquals(string.transformNullToEmpty(), "")
    }

    @Test
    fun `when string is some text 'transformNullToEmpty' should return the text`() {
        val string = "Some Text"
        Assert.assertEquals(string.transformNullToEmpty(), string)
    }

    @Test
    fun `when string is null 'isNotNullOrNotEmpty' should return the false`() {
        val string : String? = null
        Assert.assertFalse(string.isNotNullOrNotEmpty())
    }

    @Test
    fun `when string is notNull and not empty 'isNotNullOrNotEmpty' should return the true`() {
        val string = "NotNull NotEmpty"
        Assert.assertTrue(string.isNotNullOrNotEmpty())
    }

    @Test
    fun `when string is empty 'isNotNullOrNotEmpty' should return the false`() {
        val string = ""
        Assert.assertFalse(string.isNotNullOrNotEmpty())
    }

    @Test
    fun `when string is null 'isValidTelephone' should return the false`() {
        val string : String? = null
        Assert.assertFalse(string.isValidTelephone())
    }

    @Test
    fun `when string is empty 'isValidTelephone' should return the false`() {
        val string = ""
        Assert.assertFalse(string.isValidTelephone())
    }

    @Ignore
    @Test
    fun `when string is 12 char number 'isValidTelephone' should return the true`() {
        val string = "61 3333-9898"
        Assert.assertTrue(string.isValidTelephone())
    }

    @Ignore
    @Test
    fun `when string is 13 char number 'isValidTelephone' should return the true`() {
        val string = "61 99999-9999"
        Assert.assertTrue(string.isValidTelephone())
    }

    @Ignore
    @Test
    fun `when string is less than 12 char number 'isValidTelephone' should return the false`() {
        val string = "61 9909-999"
        Assert.assertFalse(string.isValidTelephone())
    }

    @Ignore
    @Test
    fun `when string is longer than 13 char number 'isValidTelephone' should return the false`() {
        val string = ""
        val result = string.isValidTelephone()
        Assert.assertFalse(result)
    }

    @Ignore
    @Test
    fun `when string is some text 'isValidTelephone' should return the false`() {
        val string = "+55 61 99999-9999"
        val result = string.isValidTelephone()
        Assert.assertFalse(result)
    }
}