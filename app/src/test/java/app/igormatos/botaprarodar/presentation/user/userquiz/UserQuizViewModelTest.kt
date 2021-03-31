package app.igormatos.botaprarodar.presentation.user.userquiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.utils.userFake
import app.igormatos.botaprarodar.utils.userSimpleSuccess
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserQuizViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val useCase = mockk<UserFormUseCase>()
    private lateinit var viewModel: UserQuizViewModel

    @Before
    fun setup() {
        viewModel = UserQuizViewModel(useCase)
        viewModel.init(userFake, false)
    }

    @Test
    fun `when 'registerUser' should return success`() {
        val userFake = slot<User>()
        coEvery {
            useCase.addUser(capture(userFake))
        } returns userSimpleSuccess

        viewModel.registerUser()
        assertTrue(viewModel.status.value is ViewModelStatus.Success)
    }

    @Test
    fun `when 'registerUser' should return error`() {
        val userFake = slot<User>()
        coEvery {
            useCase.addUser(capture(userFake))
        } returns SimpleResult.Error(Exception())

        viewModel.registerUser()
        assertTrue(viewModel.status.value is ViewModelStatus.Error)
    }

    @Test
    fun `when 'registerUser' and 'isEditableAvailable' is true should return success`() {
        viewModel.editMode = true
        val userFake = slot<User>()
        coEvery {
            useCase.startUpdateUser(capture(userFake))
        } returns userSimpleSuccess

        viewModel.registerUser()

        assertTrue(viewModel.status.value is ViewModelStatus.Success)
    }

    @Test
    fun `when 'registerUser' and 'IsEditableAvailable' is true should return error`() {
        viewModel.editMode = true
        val userFake = slot<User>()
        coEvery {
            useCase.startUpdateUser(capture(userFake))
        } returns SimpleResult.Error(Exception())

        viewModel.registerUser()
        assertTrue(viewModel.status.value is ViewModelStatus.Error)
    }
}