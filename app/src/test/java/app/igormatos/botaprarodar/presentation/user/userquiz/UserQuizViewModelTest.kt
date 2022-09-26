package app.igormatos.botaprarodar.presentation.user.userquiz

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.usecase.userForm.UserFormUseCase
import app.igormatos.botaprarodar.utils.validUser
import app.igormatos.botaprarodar.utils.userSimpleSuccess
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserQuizViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val useCase = mockk<UserFormUseCase>()
    private lateinit var viewModel: UserQuizViewModel

    private val userMotivations: Map<Int, String> = mapOf(
        0 to "usar bicicleta é mais barato.",
        1 to "A bicicleta não polui o ambiente.",
        2 to "Outro"
    )


    @Before
    fun setup() {
        every { useCase.getUserMotivations() } returns userMotivations
        viewModel = UserQuizViewModel(useCase)
        viewModel.init(validUser, false, listOf())
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

    @Test
    fun `WHEN userMotivation is empty THEN index should be zero`() {
        viewModel.userMotivation.value = ""
        assertTrue(viewModel.getSelectedUserMotivationsIndex() == 0)
    }

    @Test
    fun `WHEN set user motivation index THEN selectedUserMotivationsIndex should be updated`() {
        val index = 1
        viewModel.setSelectedUserMotivationsIndex(index)
        assertEquals(index, viewModel.selectedUserMotivationIndex)
    }

    @Test
    fun `WHEN confirmUserMotivation is call THEN userMotivation is updated with list element from index`() {
        val index = 1
        viewModel.setSelectedUserMotivationsIndex(index)
        viewModel.confirmUserMotivation()
        assertEquals(userMotivations[index], viewModel.userMotivation.value)
    }
}
