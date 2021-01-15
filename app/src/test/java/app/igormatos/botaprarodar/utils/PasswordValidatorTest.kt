package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.presentation.authentication.PasswordValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {
    private val passwordValidator = PasswordValidator()

    @Test
    fun `When validate valid password, then should return true`(){
        val password = "123456"
        val result = passwordValidator.validate(password)

        assertTrue(result)
    }

    @Test
    fun `When validate invalid password, then should return false`(){
        val password = "12345"
        val result = passwordValidator.validate(password)

        assertFalse(result)
    }
}