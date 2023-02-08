package app.igormatos.botaprarodar.domain.usecase.returnbicycle

import app.igormatos.botaprarodar.data.repository.NeighborhoodRepository
import com.brunotmgomes.ui.SimpleResult

class GetNeighborhoodsUseCase(private val neighborhoodRepository: NeighborhoodRepository) {

    suspend operator fun invoke(): List<String> {
        val neighborhoods = when (val result = neighborhoodRepository.getNeighborhoods()) {
            is SimpleResult.Error -> getFallbackNeighborhoods()
            is SimpleResult.Success -> result.data.sortedBy { it }
        }

        return listOf(*neighborhoods.toTypedArray(), OTHER_NEIGHBORHOOD)
    }

    private fun getFallbackNeighborhoods() = listOf(
        "Afogados",
        "Casa Amarela",
        "Casa Forte",
        "Cordeiro",
        "Madalena",
        "Torre",
    )

    companion object {
        const val OTHER_NEIGHBORHOOD = "Outro Bairro"
    }
}