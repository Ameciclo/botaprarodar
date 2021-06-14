package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.presentation.authentication.EmailValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EmailValidatorTest: AutoCloseKoinTest() {
    private val emailValidator = EmailValidator()

    @Test
    fun `When validate valid email, then should return true`() {
        val email = "fake@fake.com"
        val result = emailValidator.validate(email)

        assertTrue(result)
    }

    @Test
    fun `When validate invalid email, then should return false`() {
        val email = "fake.com"
        val result = emailValidator.validate(email)

        assertFalse(result)
    }
}
