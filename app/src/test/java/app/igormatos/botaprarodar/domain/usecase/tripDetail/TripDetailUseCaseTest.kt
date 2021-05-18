package app.igormatos.botaprarodar.domain.usecase.tripDetail

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailUseCase
import app.igormatos.botaprarodar.utils.bikeRequestWithMappers
import app.igormatos.botaprarodar.utils.bikeWithWithdraws
import app.igormatos.botaprarodar.utils.exception
import app.igormatos.botaprarodar.utils.userFake
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TripDetailUseCaseTest {

    private val repository = mockk<TripDetailRepository>()
    private lateinit var useCase: TripDetailUseCase

    @BeforeEach
    fun setUp() {
        useCase = TripDetailUseCase(repository)
    }

    @Test
    fun `should return bikeRequest when the repository execute with success`() {
        runBlocking {
            // arrange
            val bikeId = "1"
            val expectedResultSuccess = SimpleResult.Success(bikeRequestWithMappers)

            coEvery { repository.getBikeById(bikeId) } returns expectedResultSuccess

            // action
            val response: SimpleResult<BikeRequest> = useCase.getBikeById(bikeId)

            // assert
            assertEquals(response, expectedResultSuccess)
        }
    }

    @Test
    fun `should return exception when the repository execute with error`() {
        runBlocking {
            // arrange
            val bikeId = "1"
            val expectedResultError = SimpleResult.Error(exception)

            coEvery { repository.getBikeById(bikeId) } returns expectedResultError

            // action
            val response: SimpleResult<BikeRequest> = useCase.getBikeById(bikeId)

            // assert
            assertEquals(response, expectedResultError)
        }
    }

    @Test
    fun `should return withdraw when bike has any withdraw with the given id`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        val withdrawId = "999"
        val expectedWithdraw = Withdraws(id = withdrawId, date = "12/03/2021", user = userFake)
        bike.withdraws?.add(expectedWithdraw)

        // action
        val response: Withdraws? = useCase.getWithdrawById(bike, withdrawId)

        // assert
        assertEquals(response, expectedWithdraw)
    }

    @Test
    fun `should return devolution when bike has any devolution with the given id`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        val devolutionId = "999"
        val expectedDevolution = Devolution(id = devolutionId, date = "12/03/2021", user = userFake)
        bike.devolutions?.add(expectedDevolution)

        // action
        val response: Devolution? = useCase.getDevolutionById(bike, devolutionId)

        // assert
        assertEquals(response, expectedDevolution)
    }

    @Test
    fun `should return devolution when bike has any devolution with the given withdrawId`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        val withdrawId = "909"
        val expectedDevolution = Devolution(id = "999", date = "12/03/2021", user = userFake, withdrawId = withdrawId)
        bike.devolutions?.add(expectedDevolution)

        // action
        val response: Devolution? = useCase.getDevolutionByWithdrawId(bike, withdrawId)

        // assert
        assertEquals(response, expectedDevolution)
    }

    @Test
    fun `should return null when bike no has devolution with the given withdrawId`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        val withdrawId = "909"
        val devolution = Devolution(id = "999", date = "12/03/2021", user = userFake)
        bike.devolutions?.add(devolution)

        // action
        val response: Devolution? = useCase.getDevolutionByWithdrawId(bike, withdrawId)

        // assert
        assertEquals(response, null)
    }

    @Test
    fun `should return true when bike is in use`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        bike.inUse = true

        // action
        val response: Boolean = useCase.verifyIfBikeIsInUse(bike)

        // assert
        assertEquals(response, true)
    }

    @Test
    fun `should return false when bike is not in use`() {
        // arrange
        val bike: Bike = bikeWithWithdraws
        bike.inUse = false

        // action
        val response: Boolean = useCase.verifyIfBikeIsInUse(bike)

        // assert
        assertEquals(response, false)
    }
}