package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.BikeRequest
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BicycleApi {

    @GET("bikes.json")
    fun getBicycles(): Deferred<Map<String, BikeRequest>>

    @GET("/bikes/{bikeId}.json")
    suspend fun getBikeById(@Path("bikeId") bikeId: String): BikeRequest

    @GET("/bikes.json?orderBy=\"communityId\"")
    suspend fun getBikesByCommunityId(
        @Query("equalTo") communityId: String
    ): Map<String, BikeRequest>

    @GET("/bikes.json?orderBy=\"withdrawToUser\"")
    suspend fun getBikeWithWithdrawByUserId(@Query("equalTo") userId: String): Map<String, BikeRequest>

    @PUT("bikes/{bikeId}.json")
    suspend fun addNewBike(
        @Body bike: BikeRequest, @Path("bikeId") bikeId: String
    ): AddDataResponse

    @PATCH("/bikes/{bikeId}.json")
    suspend fun updateBike(
        @Path("bikeId") bikeId: String,
        @Body bike: BikeRequest
    ): AddDataResponse
}