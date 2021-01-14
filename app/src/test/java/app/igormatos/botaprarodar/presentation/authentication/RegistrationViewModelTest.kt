package app.igormatos.botaprarodar.presentation.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.BprErrorType
import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.data.model.error.UserAdminErrorException
import app.igormatos.botaprarodar.data.repository.AdminRepository
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationState
import app.igormatos.botaprarodar.presentation.authentication.viewmodel.RegistrationViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.lang.Exception

class RegistrationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var adminRepository: AdminRepository

    @MockK
    private lateinit var admin: Admin

    private lateinit var viewModel: RegistrationViewModel

    private val email = "fake@email.com"
    private val password = "123456"
    private val invalidEmail = "fake"
    private val invalidPassword = "123"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = RegistrationViewModel(adminRepository)
    }

    @Test
    fun `When password and email are valid, then createAccount is able to complete`() {
        coEvery { adminRepository.createAdmin(email, password) } returns admin
        val observer = mockk<Observer<RegistrationState>>() { every { onChanged(any()) } just Runs}

        viewModel.registrationState.observeForever(observer)
        viewModel.createAccount(email, password)

        verifySequence {
            observer.onChanged(RegistrationState.SendSuccess)
            observer.onChanged(RegistrationState.Completed)
        }


        assertTrue(viewModel.registrationState.value is RegistrationState.Completed)
    }

    @Test
    fun `When createAccount is called, then loading state has correct order`() {
        coEvery { adminRepository.createAdmin(email, password) } returns admin
        val observer = mockk<Observer<Boolean>>() { every { onChanged(any()) } just Runs}

        viewModel.loading.observeForever(observer)
        viewModel.emailValid.observeForever(observer)
        viewModel.passwordValid.observeForever(observer)
        viewModel.createAccount(email, password)

        verifySequence {
            observer.onChanged(true)
            observer.onChanged(true)
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `When password is invalid, then account registration flow does not start`() {
        val observer = mockk<Observer<Boolean>>() { every { onChanged(any()) } just Runs}

        viewModel.emailValid.observeForever(observer)
        viewModel.passwordValid.observeForever(observer)
        viewModel.createAccount(email, invalidPassword)

        verifySequence {
            observer.onChanged(true)
            observer.onChanged(false)
        }

        assertFalse(viewModel.registrationState.value is RegistrationState.Completed)
    }

    @Test
    fun `When email is invalid, then account registration flow does not start`() {
        val observer = mockk<Observer<Boolean>>() { every { onChanged(any()) } just Runs}

        viewModel.emailValid.observeForever(observer)
        viewModel.passwordValid.observeForever(observer)
        viewModel.createAccount(invalidEmail, password)

        verifySequence {
            observer.onChanged(false)
            observer.onChanged(true)
        }

        assertFalse(viewModel.registrationState.value is RegistrationState.Completed)
    }

    @Test
    fun `When a network related exception occurs, then state should send an Network error type`() {
        coEvery { adminRepository.createAdmin(email, password) } throws UserAdminErrorException.AdminNetwork
        val observer = mockk<Observer<RegistrationState>>() { every { onChanged(any()) } just Runs}
        val errorSlot = slot<RegistrationState.SendError>()

        viewModel.registrationState.observeForever(observer)
        viewModel.createAccount(email, password)

        verifySequence {
            observer.onChanged(capture(errorSlot))
            observer.onChanged(RegistrationState.Completed)
        }

        assertEquals(errorSlot.captured.type.name, BprErrorType.NETWORK.name)
    }

    @Test
    fun `When an unknown exception occurs, then state should send an Unknown error type`() {
        coEvery { adminRepository.createAdmin(email, password) } throws Exception()
        val observer = spyk<Observer<RegistrationState>>()
        val errorSlot = slot<RegistrationState.SendError>()

        viewModel.registrationState.observeForever(observer)
        viewModel.createAccount(email, password)

        verifySequence {
            observer.onChanged(capture(errorSlot))
            observer.onChanged(RegistrationState.Completed)
        }

        assertEquals(errorSlot.captured.type.name, BprErrorType.UNKNOWN.name)
    }
}