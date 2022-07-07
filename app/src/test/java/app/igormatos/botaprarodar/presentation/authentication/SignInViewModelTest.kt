package app.igormatos.botaprarodar.presentation.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.SignInViewModel
import com.brunotmgomes.ui.extensions.getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var passwordValidator: PasswordValidator

    lateinit var viewModel: SignInViewModel

    private val fakeValidPassword = "123456"
    private val fakeInvalidPassword = "12345"
    private val fakeEmail = "fake@fake.com"
    private val fakeTrailingSpacesEmail = " fake@fake.com "

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SignInViewModel(adminRepository, passwordValidator)
        every { passwordValidator.validate(fakeValidPassword) } returns true
        every { passwordValidator.validate(fakeInvalidPassword) } returns false
        viewModel.passwordField.value = fakeValidPassword
    }

    @Test
    fun `When password is valid, then sign in button should be enabled`() {
        viewModel.passwordField.value = fakeValidPassword

        assertEquals(true, viewModel.signInButtonEnabled.getOrAwaitValue())

    }

    @Test
    fun `When password is invalid, then sign in button should be disabled`() {
        viewModel.passwordField.value = fakeInvalidPassword

        assertEquals(false, viewModel.signInButtonEnabled.getOrAwaitValue())
    }

    @Test
    fun `When send form, then trim email`() {
        val mockAdmin = mockk<Admin>()

        coEvery {
            adminRepository.authenticateAdmin(
                fakeEmail,
                fakeValidPassword
            )
        } returns mockAdmin

        viewModel.sendForm(fakeTrailingSpacesEmail)

        coVerify { adminRepository.authenticateAdmin(fakeEmail, any()) }
    }

    @Test
    fun `When send form with valid credentials, then viewState should match success flow`() {
        val observer = spyk<Observer<SignInViewState>>()
        viewModel.viewState.observeForever(observer)

        val mockAdmin = mockk<Admin>()

        coEvery {
            adminRepository.authenticateAdmin(
                fakeEmail,
                fakeValidPassword
            )
        } returns mockAdmin

        viewModel.sendForm(fakeEmail)

        val secondStateSlot = slot<SignInViewState.SendLoading>()
        val thirdStateSlot = slot<SignInViewState.SendSuccess>()

        verifyOrder {
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
        }
    }

    @Test
    fun `When send form with invalid credentials, then viewState should match error flow`() {
        val observer = spyk<Observer<SignInViewState>>()
        viewModel.viewState.observeForever(observer)

        coEvery {
            adminRepository.authenticateAdmin(
                fakeEmail,
                fakeInvalidPassword
            )
        } throws UserAdminErrorException.AdminPasswordInvalid

        viewModel.sendForm(fakeEmail)

        val secondStateSlot = slot<SignInViewState.SendLoading>()
        val thirdStateSlot = slot<SignInViewState.SendError>()

        verifyOrder {
            observer.onChanged(capture(secondStateSlot))
            observer.onChanged(capture(thirdStateSlot))
        }
    }
}