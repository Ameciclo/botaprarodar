package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.common.extensions.convertToBike
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.utils.bikeRequest
import app.igormatos.botaprarodar.utils.mapOfBikesRequest
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class BikesUseCaseTest {

    private val repository = mockk<BikeRepository>()
    private lateinit var useCase: BikesUseCase

    @BeforeEach
    fun setUp() {
        useCase = BikesUseCase(repository)
    }

    @Test
    fun `when getBikes() should return a not null response`() = runBlocking {
        coEvery { repository.getBikesByCommunityId(any()) } returns SimpleResult.Success(
            mapOfBikesRequest
        )

        val response = useCase.getBikes("123") as SimpleResult.Success

        assertNotNull(response.data)
    }

    @Test
    fun `when getBikes() should return a list of bikes`() = runBlocking {
        coEvery { repository.getBikesByCommunityId(any()) } returns SimpleResult.Success(
            mapOfBikesRequest
        )

        val response = useCase.getBikes("123") as SimpleResult.Success
        val expectedSizeList = mapOfBikesRequest.entries.size

        assertEquals(response.data.size, expectedSizeList)
    }

    @Test
    fun `when getBikes() verifiy if has a correct item`() = runBlocking {
        coEvery { repository.getBikesByCommunityId(any()) } returns SimpleResult.Success(
            mapOfBikesRequest
        )

        val response = useCase.getBikes("123") as SimpleResult.Success

        assertEquals(bikeRequest.convertToBike(), response.data.last())
    }

    @Test
    fun `when getBikes() should return a error`() = runBlocking {
        coEvery { repository.getBikesByCommunityId(any()) } returns SimpleResult.Error(Exception())

        val response = useCase.getBikes("123") as SimpleResult.Error

        assertThat(response, instanceOf(SimpleResult.Error::class.java))
    }
}