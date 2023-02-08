package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.neighborhood.NeighborhoodRequest
import retrofit2.http.*

interface NeighborhoodApi {

    @GET("/neighborhoods.json")
    suspend fun getNeighborhoods(): List<NeighborhoodRequest>
}