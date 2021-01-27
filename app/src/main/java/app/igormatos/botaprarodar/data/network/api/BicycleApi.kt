package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BicycleApi {

    @GET("/communities/{communityId}/bicycles.json")
    fun getBicycles(@Path("communityId") communityId: String): Deferred<Map<String, Bike>>

    @POST("/communities/{communityId}/bicycles.json")
    suspend fun addNewBicycle(@Path("communityId") communityId: String, @Body bicycle: BicycleRequest): AddDataResponse

    @PATCH("/communities/{communityId}/bicycles/{bicycleId}.json")
    suspend fun updateBicycle(
        @Path("communityId") communityId: String,
        @Path("bicycleId") bicycleId: String,
        @Body bicycle: BicycleRequest): AddDataResponse
}