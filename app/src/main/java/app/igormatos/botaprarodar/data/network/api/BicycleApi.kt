package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.domain.model.DevolutionRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BicycleApi {

    @GET("bikes.json")
    fun getBicycles(): Deferred<Map<String, BikeRequest>>

    @GET("/bikes/{bikeId}.json")
    suspend fun getBikeById(@Path("bikeId") bikeId: String): BikeRequest

    @POST("bikes.json")
    suspend fun addNewBike(
        @Body bike: BikeRequest
    ): AddDataResponse

    @PATCH("/bikes/{bikeId}.json")
    suspend fun updateBike(
        @Path("bikeId") bikeId: String,
        @Body bike: BikeRequest
    ): AddDataResponse
}