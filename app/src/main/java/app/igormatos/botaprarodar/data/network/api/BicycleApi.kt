package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BicycleApi {

    @GET("/communities/{communityId}/bicycles.json")
    fun getBicycles(@Path("communityId") communityId: String): Deferred<Map<String, Bike>>

    @POST("bike.json")
    suspend fun addNewBike(
        @Path("communityId") communityId: String,
        @Body bicycle: BicycleRequest
    ): AddDataResponse

    @PATCH("/bikes/{bikeId}.json")
    suspend fun updateBike(
        @Path("bikeId") bikeId: String,
        @Body bicycle: BicycleRequest
    ): AddDataResponse
}