package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CommunityApiService {

    @GET("communities.json")
    suspend fun getCommunities(): Map<String, CommunityRequest>

    // TODO - should add comunity instead of community_preview
    @POST("communities_preview.json")
    suspend fun addCommunity(@Body communityBody: CommunityRequest): AddDataResponse

}