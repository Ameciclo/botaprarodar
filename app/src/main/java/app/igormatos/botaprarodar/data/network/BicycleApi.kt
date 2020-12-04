package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.domain.model.Bicycle
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface BicycleApi {

    @GET("/communities/{communityId}/bicycles.json")
    fun getBicycles(@Path("communityId") communityId: String): Deferred<Map<String, Bicycle>>

}