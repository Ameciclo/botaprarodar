package app.igormatos.botaprarodar.presentation.login.passwordRecovery

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.igormatos.botaprarodar.loginRequestValid
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RecoveryPasswordScreenKtTest {

    @RelaxedMockK
    private lateinit var viewModel: RecoveryPasswordViewModel

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val recoveryPasswordText
        get() = hasText(rule.activity.getString(app.igormatos.botaprarodar.R.string.enter_email_to_recover_password))
    private val recoveryPasswordInput
        get() = hasText(rule.activity.getString(app.igormatos.botaprarodar.R.string.prompt_email))
    private val recoveryPasswordButton
        get() =  hasText(rule.activity.getString(app.igormatos.botaprarodar.R.string.btn_send).uppercase()) and hasClickAction()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun shouldDisplayAllContent() {

        rule.setContent {
            RecoveryPasswordScreen(viewModel)
        }

        rule.onNode(recoveryPasswordText).assertIsDisplayed()
        rule.onNode(recoveryPasswordInput).assertIsDisplayed()
        rule.onNode(recoveryPasswordButton).assertIsNotEnabled()
    }

    @Test
    fun shouldEnableSendButton() {
        val data = RecoveryPasswordData(email = loginRequestValid.email)
        val state = RecoveryPasswordState.Success(data)

        every { viewModel.state } returns MutableStateFlow(state)

        rule.setContent {
            RecoveryPasswordScreen(viewModel)
        }

        rule.onNode(recoveryPasswordButton).assertIsEnabled()
    }

    @Test
    fun shouldShowErrorScreen() {
        val data = RecoveryPasswordData(email = loginRequestValid.email)
        val state = RecoveryPasswordState.Success(data)

        every { viewModel.state } returns MutableStateFlow(state)

        rule.setContent {
            RecoveryPasswordScreen(viewModel)
        }

        rule.onNode(recoveryPasswordButton).performClick()
    }
}