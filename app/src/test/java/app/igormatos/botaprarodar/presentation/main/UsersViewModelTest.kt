package app.igormatos.botaprarodar.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.usecase.users.UsersUseCase
import app.igormatos.botaprarodar.presentation.main.users.UsersViewModel
import app.igormatos.botaprarodar.utils.listUsers
import app.igormatos.botaprarodar.utils.userException
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
        coEvery { userCase.getAvailableUsersByCommunityId(any()) } returns SimpleResult.Success(listUsers)

        viewModel.getUsers("123")

        Assert.assertTrue(viewModel.users.value is SimpleResult.Success)
    }

    @Test
    fun `when getUsers() capture Error should return an error`(){
        coEvery { userCase.getAvailableUsersByCommunityId(any()) } returns SimpleResult.Error(Exception())

        viewModel.getUsers("123")

        Assert.assertTrue(viewModel.users.value is SimpleResult.Error)
    }

    @Test
    fun `when getUsers() capture Success should return a list of bikes`(){
        coEvery { userCase.getAvailableUsersByCommunityId(any()) } returns SimpleResult.Success(listUsers)

        viewModel.getUsers("123")
        val actual = viewModel.users.value as SimpleResult.Success

        Assert.assertEquals(listUsers, actual.data)
    }

    @Test
    fun `when getUsers() capture Error should return an exception`(){
        coEvery { userCase.getAvailableUsersByCommunityId(any()) } returns SimpleResult.Error(userException)

        viewModel.getUsers("123")
        val actual = viewModel.users.value as SimpleResult.Error

        Assert.assertEquals(userException, actual.exception)
    }
}