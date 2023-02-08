package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.NeighborhoodApi
import app.igormatos.botaprarodar.domain.model.neighborhood.NeighborhoodMapper
import com.brunotmgomes.ui.SimpleResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NeighborhoodRepositoryTest {

    @RelaxedMockK
    private lateinit var api: NeighborhoodApi

    @RelaxedMockK
    private lateinit var mapper: NeighborhoodMapper

    private lateinit var repository: NeighborhoodRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)

        repository = NeighborhoodRepository(api, mapper)
    }

    @Test
    internal fun `should fetch data from api`() = runBlocking {
        repository.getNeighborhoods()

        coVerify { api.getNeighborhoods() }
        verify { mapper.transform(any()) }
    }

    @Test
    internal fun `should handle any exception`() = runBlocking {
        val exception = Exception()

        coEvery { api.getNeighborhoods() } throws exception

        val result = repository.getNeighborhoods()

        assertEquals(SimpleResult.Error(exception), result)
    }
}