package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import retrofit2.http.*

interface CommunityApiService {

    @GET("communities.json")
    suspend fun getCommunities(): Map<String, CommunityRequest>

    @PUT("communities/{communityId}.json")
    suspend fun addCommunity(
        @Body communityBody: CommunityRequest,
        @Path("communityId") communityId: String
    ): AddDataResponse

}