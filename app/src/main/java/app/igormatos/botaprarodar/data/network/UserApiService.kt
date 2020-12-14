package app.igormatos.botaprarodar.data.network

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.community.CommunityRequest
import app.igormatos.botaprarodar.domain.model.user.UserRequest
import retrofit2.http.*

interface UserApiService {

    @POST("/communities/{communityId}/users.json")
    suspend fun addUser(
        @Body userBody: UserRequest,
        @Path("communityId") communityId: String?
    ): AddDataResponse

    @PUT("/communities/{communityId}/users/{userId}.json")
    suspend fun updateUser(
        @Body userBody: UserRequest,
        @Path("userId") userId: String?,
        @Path("communityId") communityId: String?
    ): AddDataResponse

}