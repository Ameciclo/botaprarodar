package app.igormatos.botaprarodar.presentation.login.signin

import app.igormatos.botaprarodar.common.extensions.isValidEmail
import app.igormatos.botaprarodar.common.extensions.isValidPassword
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class SignInDataTest {

    private lateinit var signInData: SignInData

    @Before
    fun setUp() {
        mockkStatic("app.igormatos.botaprarodar.common.extensions.StringExtensionKt")
        signInData = SignInData()
    }

    @Test
    fun `When pass invalid email data, then should return false`() {
        every { signInData.email.isValidEmail() } returns false

        assertFalse(signInData.isSignInButtonEnable())
    }

    @Test
    fun `When pass invalid password data, then should return false`() {
        every { signInData.password.isValidEmail() } returns false

        assertFalse(signInData.isSignInButtonEnable())
    }

    @Test
    fun `When pass valid data, then should return true`() {
        every { signInData.email.isValidEmail() } returns true
        every { signInData.password.isValidPassword() } returns true

        assertTrue(signInData.isSignInButtonEnable())
    }
}