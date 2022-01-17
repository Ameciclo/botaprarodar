package app.igormatos.botaprarodar.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.domain.usecase.trips.BikeActionUseCase
import app.igormatos.botaprarodar.presentation.main.viewModel.TripsViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TripsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: TripsViewModel
    private lateinit var bikeActionUseCase: BikeActionUseCase

    @Before
    fun setup() {
        bikeActionUseCase = mockk()
        viewModel = TripsViewModel(bikeActionUseCase)
    }

    @Test
    fun `when loadBikeActions() check size list equals  to enum size`() {
        val bikeActionsMenuTypeList = BikeActionsMenuType.values().toMutableList()
        every { bikeActionUseCase.getBikeActionsList() } returns bikeActionsMenuTypeList

        viewModel.getBikeActions().observeForever {
            assert(it == bikeActionsMenuTypeList)
        }

        viewModel.loadBikeActions()
    }
}