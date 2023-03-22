package app.igormatos.botaprarodar.presentation.login.registration

import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.BaseAndroidComposeTest
import org.junit.Test
import org.junit.runner.RunWith
import instrumentedRegisterScreen

@RunWith(AndroidJUnit4::class)
internal class RegisterScreenInstrumentedTest : BaseAndroidComposeTest() {

    @Test
    fun shouldShowUnknownErrorMessage_whenUnmappedExceptionOccursToRegister() {
        instrumentedRegisterScreen {
            emitUnkownErrorResult()
        } verify {
            isUnkownErrorVisible()
        }
    }

    @Test
    fun shouldShowNetworkErrorMessage_whenThereIsNoConnectionToRegister() {
        instrumentedRegisterScreen {
            emitNetworkErrorResult()
        } verify {
            isNetworkErrorVisible()
        }
    }
}