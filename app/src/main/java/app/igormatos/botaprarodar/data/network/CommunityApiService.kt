package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.CommunityBody
import app.igormatos.botaprarodar.domain.model.community.CommunityResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunityApiService {

    @GET("communities.json")
    suspend fun getCommunities() : List<CommunityResponse>

    @POST("communities.json")
    suspend fun addCommunity(@Body communityBody: CommunityBody) : AddDataResponse

}