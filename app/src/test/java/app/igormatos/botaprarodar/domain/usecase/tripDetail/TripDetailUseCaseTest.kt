package app.igormatos.botaprarodar.domain.usecase.tripDetail

import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailRepository
import app.igormatos.botaprarodar.presentation.main.trips.tripDetail.TripDetailUseCase
import app.igormatos.botaprarodar.utils.*
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TripDetailUseCaseTest {

    private val repository = mockk<TripDetailRepository>()
    private val withdrawId = "909"
    private val devolutionId = "999"
    private val bikeId = "1"
    private lateinit var useCase: TripDetailUseCase
    private lateinit var bike: Bike

    @BeforeEach
    fun setUp() {
        useCase = TripDetailUseCase(repository)
        bike = bikeWithWithdraws.copy(
            withdraws = bikeWithWithdraws.withdraws?.toMutableList(),
            devolutions = bikeWithWithdraws.devolutions?.toMutableList()
        )
    }

    @Test
    fun `should return bikeRequest when the repository execute with success`() {
        runBlocking {
            val expectedResultSuccess = SimpleResult.Success(bikeRequestWithMappers)
            coEvery { repository.getBikeById(bikeId) } returns expectedResultSuccess

            val response: SimpleResult<BikeRequest> = useCase.getBikeById(bikeId)

            assertEquals(response, expectedResultSuccess)
        }
    }

    @Test
    fun `should return exception when the repository execute with error`() {
        runBlocking {
            val expectedResultError = SimpleResult.Error(exception)
            coEvery { repository.getBikeById(bikeId) } returns expectedResultError

            val response: SimpleResult<BikeRequest> = useCase.getBikeById(bikeId)

            assertEquals(response, expectedResultError)
        }
    }

    @Test
    fun `should return withdraw when bike has any withdraw with the given id`() {
        val expectedWithdraw = Withdraws(id = withdrawId, date = withdrawDate, user = validUser)
        bike.withdraws?.add(expectedWithdraw)

        val response: Withdraws? = useCase.getWithdrawById(bike, withdrawId)

        assertEquals(response, expectedWithdraw)
    }

    @Test
    fun `should return devolution when bike has any devolution with the given id`() {
        val expectedDevolution =
            Devolution(id = devolutionId, date = devolutionDate, user = validUser)
        bike.devolutions?.add(expectedDevolution)

        val response: Devolution? = useCase.getDevolutionById(bike, devolutionId)

        assertEquals(response, expectedDevolution)
    }

    @Test
    fun `should return devolution when bike has any devolution with the given withdrawId`() {
        val expectedDevolution =
            Devolution(
                id = devolutionId,
                date = devolutionDate,
                user = validUser,
                withdrawId = withdrawId
            )
        bike.devolutions?.add(expectedDevolution)

        val response: Devolution? = useCase.getDevolutionByWithdrawId(bike, withdrawId)

        assertEquals(response, expectedDevolution)
    }

    @Test
    fun `should return null when bike no has devolution with the given withdrawId`() {
        val devolution = Devolution(id = devolutionId, date = devolutionDate, user = validUser)
        bike.devolutions?.add(devolution)

        val response: Devolution? = useCase.getDevolutionByWithdrawId(bike, withdrawId)

        assertEquals(response, null)
    }

    @Test
    fun `should return true when bike withdraw has devolution`() {
        val devolution = Devolution(id = devolutionId, withdrawId = withdrawId)
        bike.devolutions?.add(devolution)

        val response: Boolean = checkBikeWithdrawHasDevolution(bike)

        assertEquals(response, true)
    }

    @Test
    fun `should return false when bike withdraw no has devolution`() {
        val response: Boolean = checkBikeWithdrawHasDevolution(bike)

        assertEquals(response, false)
    }

    private fun checkBikeWithdrawHasDevolution(bike: Bike): Boolean {
        val devolutionFound: Devolution? = useCase.getDevolutionByWithdrawId(bike, withdrawId)
        return useCase.bikeWithdrawHasDevolution(devolutionFound)
    }
}