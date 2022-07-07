package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.User
import retrofit2.http.*

interface UserApi {
    @PUT("users/{userId}.json")
    suspend fun addUser(
        @Body user: User, @Path("userId") userId: String
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

    @GET("/users/{userId}.json")
    suspend fun getUserBy(@Path("userId") userId: String): User
}
