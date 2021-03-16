package app.igormatos.botaprarodar.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import app.igormatos.botaprarodar.utils.exception
import app.igormatos.botaprarodar.utils.flowError
import app.igormatos.botaprarodar.utils.flowSuccess
import app.igormatos.botaprarodar.utils.listBikeRequest
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BikesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userCase = mockk<BikesUseCase>()
    private lateinit var viewModel: BikesViewModel

    @Before
    fun setup() {
        viewModel = BikesViewModel(userCase)
    }

    @Test
    fun `when getBikes() capture Success should return success`(){
        coEvery { userCase.getBikes(any()) } returns flowSuccess

        viewModel.getBikes("123")

        assertTrue(viewModel.bikes.value is SimpleResult.Success)
    }

    @Test
    fun `when getBikes() capture Error should return an error`(){
        coEvery { userCase.getBikes(any()) } returns flowError

        viewModel.getBikes("123")

        assertTrue(viewModel.bikes.value is SimpleResult.Error)
    }

    @Test
    fun `when getBikes() capture Success should return a list of bikes`(){
        coEvery { userCase.getBikes(any()) } returns flowSuccess

        viewModel.getBikes("123")
        val actual = viewModel.bikes.value as SimpleResult.Success

        assertEquals(listBikeRequest, actual.data)
    }

    @Test
    fun `when getBikes() capture Error should return an exception`(){
        coEvery { userCase.getBikes(any()) } returns flowError

        viewModel.getBikes("123")
        val actual = viewModel.bikes.value as SimpleResult.Error

        assertEquals(exception, actual.exception)
    }
}