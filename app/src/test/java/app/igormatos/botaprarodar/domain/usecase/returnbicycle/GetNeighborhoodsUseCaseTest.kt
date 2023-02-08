package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.data.repository.NeighborhoodRepository
import com.brunotmgomes.ui.SimpleResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetNeighborhoodsUseCaseTest {


    @RelaxedMockK
    private lateinit var neighborhoodRepository: NeighborhoodRepository

    private lateinit var useCase: GetNeighborhoodsUseCase

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetNeighborhoodsUseCase(neighborhoodRepository)
    }

    @Test
    internal fun `should have valid neighborhoods`()= runBlocking {
        val repositoryData = listOf("Torre", "Afogados")
        val expectedNeighborhoods = listOf("Afogados", "Torre", "Outro Bairro")

        coEvery { neighborhoodRepository.getNeighborhoods() } returns SimpleResult.Success(repositoryData)

        val result = useCase.invoke()

        assertEquals(expectedNeighborhoods, result)
    }

    @Test
    internal fun `should have fallback neighborhoods`()= runBlocking {
        val expectedNeighborhoods = listOf(
            "Afogados",
            "Casa Amarela",
            "Casa Forte",
            "Cordeiro",
            "Madalena",
            "Torre", "Outro Bairro"
        )

        coEvery { neighborhoodRepository.getNeighborhoods() } returns SimpleResult.Error(Exception())

        val result = useCase.invoke()

        assertEquals(expectedNeighborhoods, result)
    }
}