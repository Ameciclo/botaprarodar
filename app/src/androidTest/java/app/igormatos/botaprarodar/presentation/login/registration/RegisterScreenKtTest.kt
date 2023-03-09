package app.igormatos.botaprarodar.presentation.login.registration

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.BaseAndroidComposeTest
import app.igormatos.botaprarodar.loginRequestValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import registerScreen

@RunWith(AndroidJUnit4::class)
internal class RegisterScreenKtTest : BaseAndroidComposeTest() {

    @Test
    fun shouldButtonSendDisable_whenEnter() {
        registerScreen {} verify {
            isRegisterDisabled()
        }
    }

    @Test
    fun shouldButtonSendEnable_whenEmailAndPasswordAndConfirmPasswordAreValid() {
        registerScreen {
            fillEmailField(loginRequestValid.email)
            fillPasswordField(loginRequestValid.password)
            fillConfirmPasswordField(loginRequestValid.password)

            fillEmailField(loginRequestValid.email)
        } verify {
            isRegisterEnabled()
        }
    }

    @Test
    fun shouldShowEmailAccountAlreadyExistsErrorMessage_whenEmailAlreadyRegisteredIsInformedToRegister() {
        registerScreen {
            fillEmailField("thiagomatheusms@gmail.com")
            fillPasswordField(loginRequestValid.password)
            fillConfirmPasswordField(loginRequestValid.password)
            fillConfirmPasswordField(loginRequestValid.password)

            clickRegister()
        } verify {
            isEmailAlreadyRegisteredVisible()
        }
    }
}