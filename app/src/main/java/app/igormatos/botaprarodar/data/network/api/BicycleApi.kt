package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bicycle
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BicycleApi {

    @GET("/communities/{communityId}/bicycles.json")
    fun getBicycles(@Path("communityId") communityId: String): Deferred<Map<String, Bicycle>>

    @POST("/communities/{communityId}/bicycles.json")
    suspend fun addNewBicycle(@Path("communityId") communityId: String, @Body bicycle: BicycleRequest): AddDataResponse

}