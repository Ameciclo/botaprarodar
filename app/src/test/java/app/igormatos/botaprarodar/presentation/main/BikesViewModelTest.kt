package app.igormatos.botaprarodar.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

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
    fun `when getBikes() capture Success should return success`() {
        coEvery { userCase.getBikes(any()) } returns SimpleResult.Success(listBikes)

        viewModel.getBikes("123")

        assertTrue(viewModel.bikes.value is SimpleResult.Success)
    }

    @Test
    fun `when getBikes() capture Error should return an error`() {
        coEvery { userCase.getBikes(any()) } returns SimpleResult.Error(Exception())

        viewModel.getBikes("123")

        assertTrue(viewModel.bikes.value is SimpleResult.Error)
    }

    @Test
    fun `when getBikes() capture Success should return a list of bikes`() {
        coEvery { userCase.getBikes(any()) } returns SimpleResult.Success(listBikes)

        viewModel.getBikes("123")
        val actual = viewModel.bikes.value as SimpleResult.Success

        assertEquals(listBikes, actual.data)
    }

    @Test
    fun `when getBikes() capture Error should return an exception`() {
        coEvery { userCase.getBikes(any()) } returns SimpleResult.Error(Exception())

        viewModel.getBikes("123")
        val actual = viewModel.bikes.value

        assertThat(
            (actual as SimpleResult.Error).exception,
            CoreMatchers.instanceOf(Exception::class.java)
        )
    }
}