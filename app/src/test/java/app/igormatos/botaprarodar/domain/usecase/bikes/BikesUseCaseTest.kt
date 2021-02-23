package app.igormatos.botaprarodar.domain.usecase.bikes

import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.utils.bike
import app.igormatos.botaprarodar.utils.flowError
import app.igormatos.botaprarodar.utils.flowSuccess
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
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
    fun `when getBikes() should return a list of bikes`() = runBlocking {
        coEvery { repository.getBikes(any()) } returns flowSuccess

        val response = useCase.getBikes("123").toList()
        val expectedSizeList = 1

        assertNotNull(response)
        assertEquals(response.size, expectedSizeList)
    }

    @Test
    fun `when getBikes() verifiy if has a correct item`() = runBlocking {
        coEvery { repository.getBikes(any()) } returns flowSuccess

        val response = useCase.getBikes("123").first()
        val actual = response as SimpleResult.Success<List<Bike>>

        assertEquals(bike, actual.data[0])
    }

    @Test
    fun `when getBikes() should return a error`() = runBlocking {
        coEvery { repository.getBikes(any()) } returns flowError

        val response = useCase.getBikes("123").toList()

        assertNotNull(response)
        assertThat(response[0], instanceOf(SimpleResult.Error::class.java))
    }
}