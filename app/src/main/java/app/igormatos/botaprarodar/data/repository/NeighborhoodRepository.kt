package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.network.api.NeighborhoodApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.neighborhood.NeighborhoodMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NeighborhoodRepository(
    private val neighborhoodApi: NeighborhoodApi,
    private val mapper: NeighborhoodMapper,
) {

    suspend fun getNeighborhoods() = withContext(Dispatchers.IO) {
        safeApiCall {
            neighborhoodApi.getNeighborhoods().let(mapper::transform)
        }
    }
}
