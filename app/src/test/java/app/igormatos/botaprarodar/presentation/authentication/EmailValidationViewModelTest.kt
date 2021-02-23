package app.igormatos.botaprarodar.presentation.authentication


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.BprError
import app.igormatos.botaprarodar.common.enumType.BprErrorType
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.EmailValidationViewModel
import com.brunotmgomes.ui.extensions.getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class EmailValidationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var emailValidator: Validator<String>

    private lateinit var viewModel: EmailValidationViewModel

    private val validFakeEmail = "fake@fake.com"
    private val invalidFakeEmail = "fake.com"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = EmailValidationViewModel(adminRepository, emailValidator)
        every { emailValidator.validate(validFakeEmail) } returns true
        every { emailValidator.validate(invalidFakeEmail) } returns false
        viewModel.emailField.value = validFakeEmail
    }

    @Test
    fun `Verify view state livedata completed`() {
        coEvery { adminRepository.isAdminRegistered(validFakeEmail) } returns true
        viewModel.sendForm()

        assertTrue(viewModel.viewState.value is EmailValidationState.Completed)
    }

    @Test
    fun `When email is valid, then next step button should be enabled`() {
        viewModel.emailField.value = validFakeEmail

        assertEquals(viewModel.nextButtonEnabled.getOrAwaitValue(), true)

    }

    @Test
    fun `When email is invalid, then next step button should be disabled`() {
        viewModel.emailField.value = invalidFakeEmail

        assertEquals(viewModel.nextButtonEnabled.getOrAwaitValue(), false)
    }

    @Test
    fun `When send form, then state should match registered user flow`() {
        val observer = spyk<Observer<EmailValidationState>>()
        viewModel.viewState.observeForever(observer)

        coEvery { adminRepository.isAdminRegistered(validFakeEmail) } returns true

        viewModel.sendForm()

        val firstStateSlot = slot<EmailValidationState.InitialState>()
        val secondStateSlot = slot<EmailValidationState.SendLoading>()
        val thirdStateSlot = slot<EmailValidationState.SendSuccess>()
        val fourthStateSlot = slot<EmailValidationState.Completed>()

        verifyOrder {
            observer.onChanged(capture(firstStateSlot))
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
            observer.onChanged(capture(fourthStateSlot))
        }

        assertTrue(thirdStateSlot.captured.isAdminRegisted)
    }

    @Test
    fun `When send form, then state should match newUser flow`() {
        val observer = spyk<Observer<EmailValidationState>>()
        viewModel.viewState.observeForever(observer)

        coEvery { adminRepository.isAdminRegistered(validFakeEmail) } returns false

        viewModel.sendForm()

        val firstStateSlot = slot<EmailValidationState.InitialState>()
        val secondStateSlot = slot<EmailValidationState.SendLoading>()
        val thirdStateSlot = slot<EmailValidationState.SendSuccess>()
        val fourthStateSlot = slot<EmailValidationState.Completed>()

        verifyOrder {
            observer.onChanged(capture(firstStateSlot))
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
            observer.onChanged(capture(fourthStateSlot))
        }
        assertFalse(thirdStateSlot.captured.isAdminRegisted)
    }

    @Test
    fun `When user have connection issues, then state should match NETWORK error flow`() {
        val observer = spyk<Observer<EmailValidationState>>()
        viewModel.viewState.observeForever(observer)

        coEvery { adminRepository.isAdminRegistered(validFakeEmail) } throws
                UserAdminErrorException.AdminNetwork

        viewModel.sendForm()

        val firstStateSlot = slot<EmailValidationState.InitialState>()
        val secondStateSlot = slot<EmailValidationState.SendLoading>()
        val thirdStateSlot = slot<EmailValidationState.SendError>()
        val fourthStateSlot = slot<EmailValidationState.Completed>()

        verifyOrder {
            observer.onChanged(capture(firstStateSlot))
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
            observer.onChanged(capture(fourthStateSlot))
        }

        assert((thirdStateSlot.captured as BprError).type == BprErrorType.NETWORK)
    }

    @Test
    fun `When user have unknown issues, then state should match UNKNWON error flow`() {
        val observer = spyk<Observer<EmailValidationState>>()
        viewModel.viewState.observeForever(observer)

        coEvery { adminRepository.isAdminRegistered(validFakeEmail) } throws
                Exception()

        viewModel.sendForm()

        val firstStateSlot = slot<EmailValidationState.InitialState>()
        val secondStateSlot = slot<EmailValidationState.SendLoading>()
        val thirdStateSlot = slot<EmailValidationState.SendError>()
        val fourthStateSlot = slot<EmailValidationState.Completed>()

        verifyOrder {
            observer.onChanged(capture(firstStateSlot))
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
            observer.onChanged(capture(fourthStateSlot))
        }

        assert((thirdStateSlot.captured as BprError).type == BprErrorType.UNKNOWN)
    }
}
