package app.igormatos.botaprarodar.presentation.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.BprError
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.PasswordRecoveryViewModel
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SendPasswordRecoveryViewState
import com.brunotmgomes.ui.extensions.getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PasswordRecoveryViewModelTest {

    @MockK
    private lateinit var emailValidator: Validator<String>

    @MockK
    private lateinit var adminRepository: AdminRepository

    private lateinit var viewModel: PasswordRecoveryViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val validUsername = "fake@fake.com"
    private val invalidUsername = "fake.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = PasswordRecoveryViewModel(emailValidator, adminRepository = adminRepository)
        every { emailValidator.validate(invalidUsername) } returns false
        every { emailValidator.validate(validUsername) } returns true
        coEvery {
            adminRepository.sendPasswordResetEmail(
                validUsername
            )
        } returns true

        coEvery {
            adminRepository.sendPasswordResetEmail(
                invalidUsername
            )
        } returns false
    }

    @Test
    fun `when username field is invalid, then save button is disabled`() {
        viewModel.usernameField.value = invalidUsername
        assertEquals(viewModel.saveButtonEnabled.getOrAwaitValue(), false)
    }

    @Test
    fun `when username field is valid, then save button is enabled`() {
        viewModel.usernameField.value = validUsername
        assertEquals(viewModel.saveButtonEnabled.getOrAwaitValue(), true)
    }

    @Test
    fun `when send reset password email, then view state should be success`() {
        viewModel.usernameField.value = validUsername
        viewModel.sendPasswordResetEmail()

        assertEquals(SendPasswordRecoveryViewState.SendSuccess, viewModel.viewState.value)
    }

    @Test
    fun `when send reset password email, then view state should be Network error`() {
        viewModel.usernameField.value = invalidUsername

        coEvery { adminRepository.sendPasswordResetEmail(invalidUsername) } throws
                UserAdminErrorException.AdminNetwork

        viewModel.sendPasswordResetEmail()

        val viewStateValue = viewModel.viewState.value
        assertEquals(BprErrorType.NETWORK, (viewStateValue as BprError).type)
    }

    @Test
    fun `when send reset password email, then view state should be Unknown error`() {
        viewModel.usernameField.value = invalidUsername
        viewModel.sendPasswordResetEmail()

        val viewStateValue = viewModel.viewState.value
        assertEquals(BprErrorType.UNKNOWN, (viewStateValue as BprError).type)
    }

    @Test
    fun `When send reset password email, then viewState should match Success flow`() {
        viewModel.usernameField.value = validUsername

        val observer = spyk<Observer<SendPasswordRecoveryViewState>>()
        viewModel.viewState.observeForever(observer)

        viewModel.sendPasswordResetEmail()

        val firstState = slot<SendPasswordRecoveryViewState.SendLoading>()
        val secondState = slot<SendPasswordRecoveryViewState.SendSuccess>()

        verifyOrder {
            observer.onChanged(capture(firstState))
            observer.onChanged(capture(secondState))
        }
    }


    @Test
    fun `When send reset password email, then viewState should match Error flow`() {
        viewModel.usernameField.value = invalidUsername

        val observer = spyk<Observer<SendPasswordRecoveryViewState>>()
        viewModel.viewState.observeForever(observer)

        viewModel.sendPasswordResetEmail()

        val firstState = slot<SendPasswordRecoveryViewState.SendLoading>()
        val secondState = slot<SendPasswordRecoveryViewState.SendError>()

        verifyOrder {
            observer.onChanged(capture(firstState))
            observer.onChanged(capture(secondState))
        }
    }
}
