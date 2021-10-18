package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.data.repository.DevolutionBikeRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.utils.bikeDevolutionQuizBuilder
import app.igormatos.botaprarodar.utils.bikeSimpleError
import app.igormatos.botaprarodar.utils.bikeSimpleSuccess
import app.igormatos.botaprarodar.utils.generateBikeHolder
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions

class StepFinalReturnBikeUseCaseTest {

    private val repository = mockk<DevolutionBikeRepository>()
    private val userRepository = mockk<UserRepository>()
    private lateinit var useCase: StepFinalReturnBikeUseCase

    @Before
    fun setup() {
        useCase = StepFinalReturnBikeUseCase(repository, userRepository)
    }

    @Test
    fun `when call addDevolution() should return a success`() = runBlocking {
        coEvery { repository.addDevolution(any()) } returns bikeSimpleSuccess
        coEvery { userRepository.updateUser(any()) } returns bikeSimpleSuccess

        val responseResult =
            useCase.addDevolution(
                "11/02/2021",
                generateBikeHolder(),
                bikeDevolutionQuizBuilder
            ) as SimpleResult.Success

        Assertions.assertEquals("New Bicycle", responseResult.data.name)
    }

    @Test
    fun `when call addDevolution() should return an error`() = runBlocking {
        coEvery {
            repository.addDevolution(any())
        } returns bikeSimpleError

        val responseResult =
            useCase.addDevolution(
                "15/02/2021",
                generateBikeHolder(),
                bikeDevolutionQuizBuilder
            )

        Assertions.assertTrue(responseResult is SimpleResult.Error)
    }

}