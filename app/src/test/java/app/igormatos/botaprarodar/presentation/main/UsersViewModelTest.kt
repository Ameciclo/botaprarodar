package app.igormatos.botaprarodar.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.presentation.main.users.UsersViewModel
import app.igormatos.botaprarodar.utils.listUsers
import app.igormatos.botaprarodar.utils.userException
import app.igormatos.botaprarodar.utils.userFlowError
import app.igormatos.botaprarodar.utils.userFlowSuccess
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UsersViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userCase = mockk<UsersUseCase>()
    private lateinit var viewModel: UsersViewModel

    @Before
    fun setup() {
        viewModel = UsersViewModel(userCase)
    }

    @Test
    fun `when getUsers() capture Success should return success`(){
        coEvery { userCase.getUsers(any()) } returns userFlowSuccess

        viewModel.getUsers("123")

        Assert.assertTrue(viewModel.users.value is SimpleResult.Success)
    }

    @Test
    fun `when getUsers() capture Error should return an error`(){
        coEvery { userCase.getUsers(any()) } returns userFlowError

        viewModel.getUsers("123")

        Assert.assertTrue(viewModel.users.value is SimpleResult.Error)
    }

    @Test
    fun `when getUsers() capture Success should return a list of bikes`(){
        coEvery { userCase.getUsers(any()) } returns userFlowSuccess

        viewModel.getUsers("123")
        val actual = viewModel.users.value as SimpleResult.Success

        Assert.assertEquals(listUsers, actual.data)
    }

    @Test
    fun `when getUsers() capture Error should return an exception`(){
        coEvery { userCase.getUsers(any()) } returns userFlowError

        viewModel.getUsers("123")
        val actual = viewModel.users.value as SimpleResult.Error

        Assert.assertEquals(userException, actual.exception)
    }
}