package app.igormatos.botaprarodar.domain.usecase.users

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.data.repository.UserRepository
import app.igormatos.botaprarodar.utils.buildMapStringAndBicycle
import app.igormatos.botaprarodar.utils.userFake
import app.igormatos.botaprarodar.utils.userSimpleSuccessEdit
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ValidateUserWithdrawTest {
    private val bikeRepository = mockk<BikeRepository>()
    private val userRepository = mockk<UserRepository>()
    private val testUser = userFake.copy()
    private lateinit var validateUserWithdraw: ValidateUserWithdraw

    @BeforeEach
    fun setup() {
        validateUserWithdraw = ValidateUserWithdraw(bikeRepository, userRepository)
    }

    @Test
    fun `when user has active withdraw but bike hasn't should allow user to new withdraws`() {
        coEvery { bikeRepository.getBikeWithWithdrawByUser(any()) } returns SimpleResult.Success(emptyMap())
        coEvery { userRepository.updateUser(any()) } returns userSimpleSuccessEdit
        testUser.hasActiveWithdraw = true

        runBlocking {
            val userHasWithdraw = validateUserWithdraw.execute(testUser)

            assertThat(testUser.hasActiveWithdraw, equalTo(false))
            assertThat(userHasWithdraw, equalTo(false))
        }
    }

    @Test
    fun `when user and bike has active withdraw should not allow user to new withdraws`() {
        val mapStringAndBicycle = buildMapStringAndBicycle(1)
        coEvery { bikeRepository.getBikeWithWithdrawByUser(any()) } returns
                SimpleResult.Success(mapStringAndBicycle)
        testUser.hasActiveWithdraw = true

        runBlocking {
            val userHasWithdraw = validateUserWithdraw.execute(testUser)

            assertThat(testUser.hasActiveWithdraw, equalTo(true))
            assertThat(userHasWithdraw, equalTo(true))
        }
    }

    @Test
    fun `when user has no active withdraw should do nothing`() {
        testUser.hasActiveWithdraw = false

        runBlocking {
            val userHasWithdraw = validateUserWithdraw.execute(testUser)

            assertThat(testUser.hasActiveWithdraw, equalTo(false))
            assertThat(userHasWithdraw, equalTo(false))
        }
    }
}