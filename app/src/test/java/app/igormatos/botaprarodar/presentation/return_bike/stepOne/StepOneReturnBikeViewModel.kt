package app.igormatos.botaprarodar.presentation.return_bike.stepOne

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.domain.usecase.bikes.BikesUseCase
import app.igormatos.botaprarodar.presentation.main.bikes.BikesViewModel
import io.mockk.mockk
import org.junit.Rule

class StepOneReturnBikeViewModel {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userCase = mockk<BikesUseCase>()
    private lateinit var viewModel: BikesViewModel
}