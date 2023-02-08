package app.igormatos.botaprarodar.domain.model.neighborhood

class NeighborhoodMapper {
    fun transform(neighborhoods: List<NeighborhoodRequest>) = neighborhoods.map { it.name }
}