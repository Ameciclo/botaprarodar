package app.igormatos.botaprarodar.presentation.login.registration

import app.igormatos.botaprarodar.common.extensions.isValidEmail
import app.igormatos.botaprarodar.common.extensions.isValidPassword
import app.igormatos.botaprarodar.utils.loginRequestValid
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RegisterDataTest {

    @BeforeEach
    fun setUp() {
        mockkStatic("app.igormatos.botaprarodar.common.extensions.StringExtensionKt")
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    internal fun `should enable button`() {
        val data = RegisterData(
            email = loginRequestValid.email,
            password = loginRequestValid.password,
            confirmPassword = loginRequestValid.password
        )

        every { data.email.isValidEmail() } returns true
        every { data.password.isValidPassword() } returns true

        assertThat(data.isButtonEnable(), equalTo(true))
    }

    @Test
    internal fun `should not enable button with invalid email`() {
        val data = RegisterData(
            email = "",
            password = loginRequestValid.password,
            confirmPassword = loginRequestValid.password
        )

        every { data.email.isValidEmail() } returns false
        every { data.password.isValidPassword() } returns true

        assertThat(data.isButtonEnable(), equalTo(false))
    }

    @Test
    internal fun `should not enable button with invalid password`() {
        val data = RegisterData(
            email = loginRequestValid.email,
            password = "",
            confirmPassword = loginRequestValid.password
        )

        every { data.email.isValidEmail() } returns true
        every { data.password.isValidPassword() } returns false

        assertThat(data.isButtonEnable(), equalTo(false))
    }

    @Test
    internal fun `should not enable button with invalid confirm password`() {
        val data = RegisterData(
            email = loginRequestValid.email,
            password = loginRequestValid.password,
            confirmPassword = ""
        )

        every { data.email.isValidEmail() } returns true
        every { data.password.isValidPassword() } returns true

        assertThat(data.isButtonEnable(), equalTo(false))
    }
}