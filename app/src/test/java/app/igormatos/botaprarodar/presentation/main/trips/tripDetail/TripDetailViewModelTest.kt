package app.igormatos.botaprarodar.presentation.main.trips.tripDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.extensions.convertToBike
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.returnbicycle.stepFinalReturnBike.UiState
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TripDetailViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: TripDetailUseCase
    private lateinit var viewModel: TripDetailViewModel

    @Before
    fun setup() {
        useCase = mockk()
        viewModel = TripDetailViewModel(useCase)
    }

    @Test
    fun `should return bike when the usecase execute with success`() = runBlocking {
        // arrange
        val bikeId = "1"
        val expectedSimpleResultBike = SimpleResult.Success(bikeRequest.convertToBike())
        coEvery {
            useCase.getBikeById(bikeId)
        } returns SimpleResult.Success(bikeRequest)

        // action
        viewModel.getBikeById(bikeId)
        val responseBike: SimpleResult<Bike>? = viewModel.bike.value
        val uiState: UiState? = viewModel.uiState.value

        // assert
        assertEquals(responseBike, expectedSimpleResultBike)
        assertEquals(uiState, UiState.Success)
    }

    @Test
    fun `should change uistate from loading to success when getBikeById execute with success`() = runBlocking {
        // arrange
        val observerUiStateMock = mockk<Observer<UiState>>(relaxed = true)
        val bikeId = "1"

        coEvery {
            useCase.getBikeById(bikeId)
        } returns SimpleResult.Success(bikeRequest)

        viewModel.uiState.observeForever(observerUiStateMock)

        // action
        viewModel.getBikeById(bikeId)

        // assert
        verifyOrder {
            observerUiStateMock.onChanged(UiState.Loading)
            observerUiStateMock.onChanged(UiState.Success)
        }
    }

    @Test
    fun `should change uistate from loading to error when getBikeById execute with fail`() = runBlocking {
        // arrange
        val observerUiStateMock = mockk<Observer<UiState>>(relaxed = true)
        val bikeId = "1"

        coEvery {
            useCase.getBikeById(bikeId)
        } returns SimpleResult.Error(exception)

        viewModel.uiState.observeForever(observerUiStateMock)

        // action
        viewModel.getBikeById(bikeId)

        // assert
        verifyOrder {
            observerUiStateMock.onChanged(UiState.Loading)
            observerUiStateMock.onChanged(UiState.Error(exception.message.toString()))
        }
    }

    @Test
    fun `should return error when the usecase execute with fail`() = runBlocking {
        // arrange
        val bikeId = "1"
        val expectedSimpleResultError = SimpleResult.Error(exception)
        coEvery {
            useCase.getBikeById(bikeId)
        } returns expectedSimpleResultError

        // action
        viewModel.getBikeById(bikeId)
        val responseBike: SimpleResult<Bike>? = viewModel.bike.value
        val uiState: UiState? = viewModel.uiState.value

        // assert
        assertEquals(responseBike, expectedSimpleResultError)
        assertEquals(uiState, UiState.Error(exception.message.toString()))
    }

    @Test
    fun `should return pair with withdraw valid and devolution null when historicStatus is Emprestimo and does not exists devolution`() {
        // arrange
        val bike = bikeWithWithdraws
        val withdrawID = "999"
        val withdraw = Withdraws(id = withdrawID, date = withdrawDate, user = userFake)
        bike.withdraws?.add(withdraw)

        every {
            useCase.getDevolutionByWithdrawId(any(), any())
        } returns null

        every {
            useCase.getWithdrawById(bike, withdrawID)
        } returns withdraw

        // action
        val response: Pair<Withdraws?, Devolution?> =
            viewModel.returnWithdrawAndDevolution(IS_WITHDRAW, withdrawID, bike)
        val withdrawResponse: Withdraws? = response.first
        val devolutionResponse: Devolution? = response.second

        // assert
        assertEquals(withdrawResponse, withdraw)
        assertEquals(devolutionResponse, null)
    }

    @Test
    fun `should return pair with withdraw valid and devolution valid when historicStatus is Emprestimo and exists devolution`() {
        // arrange
        val bike = bikeWithWithdraws
        val withdrawID = "999"
        val withdraw = Withdraws(id = withdrawID, date = withdrawDate, user = userFake)
        bike.withdraws?.add(withdraw)

        val devolutionId = "909"
        val devolution = Devolution(
            id = devolutionId,
            date = devolutionDate,
            user = userFake,
            withdrawId = withdrawID
        )
        bike.devolutions?.add(devolution)

        every {
            useCase.getWithdrawById(bike, withdrawID)
        } returns withdraw

        every {
            useCase.getDevolutionByWithdrawId(bike, withdrawID)
        } returns devolution

        // action
        val response: Pair<Withdraws?, Devolution?> =
            viewModel.returnWithdrawAndDevolution(IS_WITHDRAW, withdrawID, bike)
        val withdrawResponse: Withdraws? = response.first
        val devolutionResponse: Devolution? = response.second

        // assert
        assertEquals(withdrawResponse, withdraw)
        assertEquals(devolutionResponse, devolution)
    }

    @Test
    fun `should return pair with withdraw valid and devolution valid when historicStatus is Devolution`() {
        // arrange
        val bike = bikeWithWithdraws
        val withdrawID = "999"
        val withdraw = Withdraws(id = withdrawID, date = withdrawDate, user = userFake)
        bike.withdraws?.add(withdraw)

        val devolutionId = "909"
        val devolution = Devolution(
            id = devolutionId,
            date = devolutionDate,
            user = userFake,
            withdrawId = withdrawID
        )
        bike.devolutions?.add(devolution)

        every {
            useCase.getWithdrawById(bike, withdrawID)
        } returns withdraw

        every {
            useCase.getDevolutionById(bike, devolutionId)
        } returns devolution

        // action
        val response: Pair<Withdraws?, Devolution?> =
            viewModel.returnWithdrawAndDevolution(IS_DEVOLUTION, devolutionId, bike)
        val withdrawResponse: Withdraws? = response.first
        val devolutionResponse: Devolution? = response.second

        // assert
        assertEquals(withdrawResponse, withdraw)
        assertEquals(devolutionResponse, devolution)
    }

    companion object {
        const val IS_WITHDRAW = "empréstimo"
        const val IS_DEVOLUTION = "devolucao"
    }
}