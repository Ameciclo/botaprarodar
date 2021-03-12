package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BicycleApi {

    @GET("bikes.json")
    fun getBicycles(): Deferred<Map<String, BikeRequest>>

    @POST("bikes.json")
    suspend fun addNewBike(
        @Body bicycle: Bike
    ): AddDataResponse

    @PATCH("/bikes/{bikeId}.json")
    suspend fun updateBike(
        @Path("bikeId") bikeId: String,
        @Body bicycle: Bike
    ): AddDataResponse
}