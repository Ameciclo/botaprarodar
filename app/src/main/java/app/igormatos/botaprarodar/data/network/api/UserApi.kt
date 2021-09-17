package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import retrofit2.http.*

interface UserApi {
    @POST("users.json")
    suspend fun addUser(
        @Body user: User
    ): AddDataResponse

    @PATCH("/users/{userId}.json")
    suspend fun updateUser(
        @Path("userId") userId: String,
        @Body user: User
    ): AddDataResponse

    @GET("/users.json?orderBy=\"communityId\"")
    suspend fun getUsersByCommunityId(
        @Query("equalTo") communityId: String
    ): Map<String, User>
}